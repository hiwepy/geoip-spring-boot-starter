package com.maxmind.db.spring.boot;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(GeoIPProperties.PREFIX)
@Data
public class GeoIPProperties {

	public static final String PREFIX = "geoip2";

	/** GeoLite2 ASN Database Location */
	private String asnLocation = "classpath*:GeoLite2-ASN.mmdb";

	/** GeoIP2 Country Database Location */
	private String countryLocation = "classpath*:GeoLite2-Country.mmdb";

	/** GeoIP2 City Database Location */
	private String cityLocation = "classpath*:GeoLite2-City.mmdb";

}
