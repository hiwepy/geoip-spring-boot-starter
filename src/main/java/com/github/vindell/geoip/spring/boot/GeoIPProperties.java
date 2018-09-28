package com.github.vindell.geoip.spring.boot;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(GeoIPProperties.PREFIX)
public class GeoIPProperties {

	public static final String PREFIX = "qqwry";
	// 一些固定常量，比如记录长度等等  
	public static final int IP_RECORD_LENGTH = 7;  
    
	/** Whether Enable H2 Server. */
	private boolean enabled = false;
	/** TCP port for remote connections(default: 9092) */
	private String qqwryDat = "classpath:qqwry.dat";
	private int ipRecordLength = IP_RECORD_LENGTH;
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getQqwryDat() {
		return qqwryDat;
	}

	public void setQqwryDat(String qqwryDat) {
		this.qqwryDat = qqwryDat;
	}

	public int getIpRecordLength() {
		return ipRecordLength;
	}

	public void setIpRecordLength(int ipRecordLength) {
		this.ipRecordLength = ipRecordLength;
	}
	
	

}