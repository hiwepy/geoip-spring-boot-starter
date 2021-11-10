package com.maxmind.db.spring.boot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.maxmind.db.NodeCache;
import com.maxmind.db.spring.boot.cache.GuavaCache;
import com.maxmind.geoip2.DatabaseReader;

/**
 *
 */
@Configuration
@ConditionalOnClass(DatabaseReader.class)
@EnableConfigurationProperties({ GeoIPProperties.class })
public class GeoIPAutoConfiguration {

	protected ResourceLoader resourceLoader = new PathMatchingResourcePatternResolver();

	@Bean
	@ConditionalOnMissingBean
	public NodeCache nodeCache() {
		return new GuavaCache();
	}
	
	@Bean
	public DatabaseReader geoip2Reader(NodeCache nodeCache, GeoIPProperties properties) throws FileNotFoundException, IOException {
		// A File object pointing to your GeoIP2 or GeoLite2 database
		File database = new File(properties.getLocation());
		if(database.exists()) {
			// the object across lookups. The object is thread-safe.
			DatabaseReader reader = new DatabaseReader.Builder(database).withCache(nodeCache).build();
			return reader;
		} else {
			// 查找resource
			Resource resource = resourceLoader.getResource(properties.getLocation());
			if(resource.exists()){
				return new DatabaseReader.Builder(resource.getInputStream()).withCache(nodeCache).build();
			}
			throw new IOException("not found db form : " + properties.getLocation());
		}
	}

	@Bean
	@ConditionalOnMissingBean
	public GeoIPTemplate geoip2Template(DatabaseReader dbReader) {
		return new GeoIPTemplate(dbReader);
	}

}
