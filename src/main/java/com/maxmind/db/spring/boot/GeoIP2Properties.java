package com.maxmind.db.spring.boot;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * GeoIP2 Configuration Properties
 * @author [@Loong Wan](https://github.com/loong10k)
 */
@ConfigurationProperties(GeoIP2Properties.PREFIX)
public class GeoIP2Properties {

	public static final String PREFIX = "geoip2";

	/** GeoIP2 or GeoLite2 Database Location */
	private String location = "classpath:GeoLite2-Country.mmdb";

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
