package com.maxmind.db.spring.boot;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(GeoIPProperties.PREFIX)
@Data
public class GeoIPProperties {

	public static final String PREFIX = "geoip2";
    
	/** GeoIP2 Database Location */
	private String location = "classpath:GeoLite2-City.mmdb";

}