/**
 * Copyright (C) 2018 Jeebiz (http://jeebiz.net).
 * All Rights Reserved.
 */
package com.maxmind.db.spring.boot;

import com.maxmind.db.spring.boot.ext.RegionAddress;
import com.maxmind.db.spring.boot.ext.RegionEnum;
import com.maxmind.db.spring.boot.util.IpUtils;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.*;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

/**
 * IP地址解析
 * http://whois.pconline.com.cn/
 */
@Slf4j
public class GeoIP2RegionTemplate {

	private static final String NOT_MATCH = "未分配或者内网IP|0|0|0|0";
	private static final RegionAddress NOT_MATCH_REGION_ADDRESS = new RegionAddress(NOT_MATCH.split("\\|"));
	private final DatabaseReader geoip2Reader;

	public GeoIP2RegionTemplate(DatabaseReader geoip2Reader) {
		this.geoip2Reader = geoip2Reader;
	}

	public RegionAddress getRegionAddress(String ip) {
		try {

			// Replace "city" with the appropriate method for your database, e.g.,
			// "country".
			CityResponse response = geoip2Reader.city(InetAddress.getByName(ip));

			Country country = response.getCountry();

			log.info(" IP : {} >> Country IsoCode : {}", ip, country.getIsoCode()); // 'US'
			log.info(" IP : {} >> Country Name : {}",  ip, country.getName()); // 'United States'
			log.info(" IP : {} >> Country CnName : {}",  ip, country.getNames().get("zh-CN")); // '美国'

			Subdivision subdivision = response.getMostSpecificSubdivision();
			log.info(" IP : {} >> Subdivision Name : {}",  ip, subdivision.getName()); // 'Minnesota'
			log.info(" IP : {} >> Subdivision IsoCode : {}",  ip, subdivision.getIsoCode()); // 'MN'

			City city = response.getCity();
			log.info(" IP : {} >> City Name : {}",  ip, city.getName()); // 'Minneapolis'

			Postal postal = response.getPostal();
			log.info(" IP : {} >> Postal Code : {}", ip, postal.getCode()); // '55455'

			Location location = response.getLocation();
			log.info(" IP : {} >> Latitude : {}", ip, location.getLatitude()); // 44.9733
			log.info(" IP : {} >> Longitude : {}", ip, location.getLongitude()); // -93.2323

			return new RegionAddress(country.getNames().get("zh-CN"), subdivision.getName(), city.getName(), "", "");

		} catch (Exception e) {
			log.error("IP : {} >> Country/Region Parser Error：{}", ip, e.getMessage());
			return NOT_MATCH_REGION_ADDRESS;
		}
	}

	public RegionEnum getRegionByIp(String ip) {
		try {
			if(!IpUtils.isIpv4(ip)){
				return RegionEnum.UK;
			}

			// Replace "city" with the appropriate method for your database, e.g., "country".
			CountryResponse response = geoip2Reader.country(InetAddress.getByName(ip));

			Country country = response.getCountry();

			log.info(" IP : {} >> Country/Region IsoCode : {}", ip, country.getIsoCode()); // 'US'
			log.info(" IP : {} >> Country/Region Name : {}", ip ,country.getName()); // 'United States'
			log.info(" IP : {} >> Country/Region CnName : {}", ip, country.getNames().get("zh-CN")); // '美国'

			return RegionEnum.getByCode2(country.getIsoCode());

		} catch (Exception e) {
			log.error("IP : {} >> Country/Region Parser Error：{}", ip, e.getMessage());
			return RegionEnum.UK;
		}
	}

	public boolean isMainlandIp(String ip) {
		RegionEnum regionEnum = this.getRegionByIp(ip);
		return RegionEnum.CN.compareTo(regionEnum) == 0 &&
				RegionEnum.HK.compareTo(regionEnum) != 0 &&
				RegionEnum.MO.compareTo(regionEnum) != 0 &&
				RegionEnum.TW.compareTo(regionEnum) != 0;
	}

	public static void main(String[] args) throws IOException {

		// A File object pointing to your GeoIP2 or GeoLite2 database
		File database = new File("D:\\geoip\\GeoLite2-City.mmdb");

		// This creates the DatabaseReader object. To improve performance, reuse
		// the object across lookups. The object is thread-safe.
		DatabaseReader reader = new DatabaseReader.Builder(database).build();

		GeoIP2RegionTemplate template = new GeoIP2RegionTemplate(reader);

		RegionAddress mapLL2 = template.getRegionAddress("183.128.136.82"); // lng：116.86380647644208  lat：38.297615350325717
		System.out.println(mapLL2);
	}

}
