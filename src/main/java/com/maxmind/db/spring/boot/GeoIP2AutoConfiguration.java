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
import com.maxmind.db.spring.boot.cache.CaffeineNodeCache;
import com.maxmind.geoip2.DatabaseReader;

/**
 *
 */
@Configuration
@ConditionalOnClass(DatabaseReader.class)
@EnableConfigurationProperties({ GeoIP2Properties.class })
/**
 * <p>Auto-configuration for GeoIP2AutoConfiguration.</p>
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class GeoIP2AutoConfiguration {

	protected ResourceLoader resourceLoader = new PathMatchingResourcePatternResolver();

	@Bean
	@ConditionalOnMissingBean
	/**
	 * <p>Node cache.</p>
	 * @return the result
	 */
	public NodeCache nodeCache() {
		return new CaffeineNodeCache();
	}

	@Bean
	/**
	 * <p>Geoip2 reader.</p>
	 * @param nodeCache
	 * @param properties
	 * @return the result
	 */
	public DatabaseReader geoip2Reader(NodeCache nodeCache, GeoIP2Properties properties) throws FileNotFoundException, IOException {
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
	/**
	 * <p>Geoip2 template.</p>
	 * @param dbReader
	 * @return the result
	 */
	public GeoIP2Template geoip2Template(DatabaseReader dbReader) {
		return new GeoIP2Template(dbReader);
	}

}
