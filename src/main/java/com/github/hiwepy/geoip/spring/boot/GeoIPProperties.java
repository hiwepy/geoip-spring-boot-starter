package com.github.hiwepy.geoip.spring.boot;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(GeoIPProperties.PREFIX)
public class GeoIPProperties {

	public static final String PREFIX = "geoip2";
    
	/** Whether Enable H2 Server. */
	private boolean enabled = false;
	/** TCP port for remote connections(default: 9092) */
	private String location = "classpath:qqwry.dat";
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}