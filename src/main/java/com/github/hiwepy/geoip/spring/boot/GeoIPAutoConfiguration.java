package com.github.hiwepy.geoip.spring.boot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.maxmind.db.CHMCache;
import com.maxmind.db.NodeCache;
import com.maxmind.geoip2.DatabaseReader;

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
		// A File object pointing to your GeoIP2 or GeoLite2 database
		File database = new File(properties.getLocation());
		if(database.exists()) {
			// the object across lookups. The object is thread-safe.
			DatabaseReader reader = new DatabaseReader.Builder(database).withCache(nodeCache).build();
			return reader;
		} else {
			// 查找resource
			Resource resource = resourceLoader.getResource(properties.getLocation());
			if (resource.isFile() && resource.exists()) {
				return new DatabaseReader.Builder(resource.getFile()).withCache(nodeCache).build();
			} else {
				return new DatabaseReader.Builder(resource.getInputStream()).withCache(nodeCache).build();
			}
		}
	}

	@Bean
	public GeoIPTemplate geoIPTemplate(DatabaseReader geoip2Reader) {
		return new GeoIPTemplate(geoip2Reader);
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

}
