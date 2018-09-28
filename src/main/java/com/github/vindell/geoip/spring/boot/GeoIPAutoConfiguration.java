package com.github.vindell.geoip.spring.boot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetAddress;

import org.apache.commons.lang3.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.FileCopyUtils;

import com.maxmind.db.CHMCache;
import com.maxmind.db.NodeCache;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Postal;
import com.maxmind.geoip2.record.Subdivision;

/**
 * 
 */
@Configuration
@ConditionalOnProperty(prefix = GeoIPProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ GeoIPProperties.class })
public class GeoIPAutoConfiguration implements ResourceLoaderAware {

	private ResourceLoader resourceLoader;

	@Autowired
	private GeoIPProperties properties;

	@Bean
	public NodeCache nodeCache() {
		return new CHMCache();
	}

	@Bean
	public DatabaseReader geoip2Reader(NodeCache nodeCache) throws FileNotFoundException, IOException {
		// 查找resource
		Resource resource = resourceLoader.getResource(properties.getQqwryDat());
		if (resource.isFile() && resource.exists()) {
			return new DatabaseReader.Builder(resource.getFile()).withCache(nodeCache).build();
		} else {
			return new DatabaseReader.Builder(resource.getInputStream()).withCache(nodeCache).build();
		}
	}

	@Bean
	public GeoIPTemplate cityTemplate(RandomAccessFile qqwryFile) throws IOException, GeoIp2Exception {

		// A File object pointing to your GeoIP2 or GeoLite2 database
		File database = new File("/path/to/GeoIP2-City.mmdb");

		// This creates the DatabaseReader object. To improve performance, reuse
		// the object across lookups. The object is thread-safe.
		DatabaseReader reader = new DatabaseReader.Builder(database).build();

		InetAddress ipAddress = InetAddress.getByName("128.101.101.101");

		// Replace "city" with the appropriate method for your database, e.g.,
		// "country".
		CityResponse response = reader.city(ipAddress);

		Country country = response.getCountry();
		System.out.println(country.getIsoCode()); // 'US'
		System.out.println(country.getName()); // 'United States'
		System.out.println(country.getNames().get("zh-CN")); // '美国'

		Subdivision subdivision = response.getMostSpecificSubdivision();
		System.out.println(subdivision.getName()); // 'Minnesota'
		System.out.println(subdivision.getIsoCode()); // 'MN'

		City city = response.getCity();
		System.out.println(city.getName()); // 'Minneapolis'

		Postal postal = response.getPostal();
		System.out.println(postal.getCode()); // '55455'

		Location location = response.getLocation();
		System.out.println(location.getLatitude()); // 44.9733
		System.out.println(location.getLongitude()); // -93.2323

		return new GeoIPTemplate();
	}

	@Bean
	public GeoIPTemplate qqwryTemplate(RandomAccessFile qqwryFile) throws IOException {

		return new GeoIPTemplate();
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

}
