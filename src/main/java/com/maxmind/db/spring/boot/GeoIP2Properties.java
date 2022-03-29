package com.maxmind.db.spring.boot;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(GeoIP2Properties.PREFIX)
@Data
public class GeoIP2Properties {

	public static final String PREFIX = "geoip2";

	/** GeoIP2 or GeoLite2 Database Location */
	private String location = "classpath:GeoLite2-Country.mmdb";

}
