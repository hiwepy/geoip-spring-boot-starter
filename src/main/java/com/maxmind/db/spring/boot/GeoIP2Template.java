/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.maxmind.db.spring.boot;

import com.maxmind.db.spring.boot.ext.RegionAddress;
import com.maxmind.db.spring.boot.ext.RegionEnum;
import com.maxmind.db.spring.boot.util.IpUtils;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.AnonymousIpResponse;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Optional;

/**
 * GeoIP2 IP地址解析
 */
@Slf4j
public class GeoIP2Template {

	private static final String NOT_MATCH = "未分配或者内网IP|0|0|0|0";
	private static final RegionAddress NOT_MATCH_REGION_ADDRESS = new RegionAddress(NOT_MATCH.split("\\|"));
	private final DatabaseReader geoip2Reader;

	public GeoIP2Template(DatabaseReader geoip2Reader) {
		this.geoip2Reader = geoip2Reader;
	}

	/**
	 * Get Anonymous Ip by IPv4 or IPv6 address
	 * @param ipAddress IPv4 or IPv6 address to lookup.
	 * @return The AnonymousIpResponse
	 * @throws IOException  IO Exception
	 * @throws GeoIp2Exception GeoIp2 Exception
	 */
	public Optional<AnonymousIpResponse> anonymousIp(String ipAddress) throws IOException, GeoIp2Exception {
		return this.anonymousIp(InetAddress.getByName(ipAddress));
	}

	/**
	 * Get Anonymous Ip by IPv4 or IPv6 address
	 * @param ipAddress IPv4 or IPv6 address to lookup.
	 * @return The AnonymousIpResponse
	 * @throws IOException  IO Exception
	 * @throws GeoIp2Exception GeoIp2 Exception
	 */
	public Optional<AnonymousIpResponse> anonymousIp(InetAddress ipAddress) throws IOException, GeoIp2Exception {
		return geoip2Reader.tryAnonymousIp(ipAddress);
	}

	/**
	 * Get Country info by IPv4 or IPv6 address
	 * @param ipAddress IPv4 or IPv6 address to lookup.
	 * @return The CountryResponse
	 * @throws IOException  IO Exception
	 * @throws GeoIp2Exception GeoIp2 Exception
	 */
	public CountryResponse getCountry(String ipAddress) throws IOException, GeoIp2Exception {
		return this.getCountry(InetAddress.getByName(ipAddress));
	}

	/**
	 * Get Country info by IPv4 or IPv6 address
	 * @param ipAddress IPv4 or IPv6 address to lookup.
	 * @return The CountryResponse
	 * @throws IOException  IO Exception
	 * @throws GeoIp2Exception GeoIp2 Exception
	 */
	public CountryResponse getCountry(InetAddress ipAddress) throws IOException, GeoIp2Exception {

		log.info(" ================= Country Info For : {} ====================", ipAddress.toString());

		// Replace "city" with the appropriate method for your database, e.g., "country".
		CountryResponse response = geoip2Reader.country(ipAddress);

		Country country = response.getCountry();

		log.info(" Country IsoCode : {}", country.getIsoCode()); // 'US'
		log.info(" Country Name : {}", country.getName()); // 'United States'
		log.info(" Country CnName : {}", country.getNames().get("zh-CN")); // '美国'

		return response;
	}


	/**
	 * Get City info by IPv4 or IPv6 address
	 * @param ipAddress IPv4 or IPv6 address to lookup.
	 * @return The CityResponse
	 * @throws IOException  IO Exception
	 * @throws GeoIp2Exception GeoIp2 Exception
	 */
	public CityResponse getCity(String ipAddress) throws IOException, GeoIp2Exception {
		return this.getCity(InetAddress.getByName(ipAddress));
	}

	/**
	 * Get City info by IPv4 or IPv6 address
	 * @param ipAddress IPv4 or IPv6 address to lookup.
	 * @return The CityResponse
	 * @throws IOException  IO Exception
	 * @throws GeoIp2Exception GeoIp2 Exception
	 */
	public CityResponse getCity(InetAddress ipAddress) throws IOException, GeoIp2Exception {

		log.info(" ================= City Info For : {} ====================", ipAddress.toString());

		// Replace "city" with the appropriate method for your database, e.g., "country".
		CityResponse response = geoip2Reader.city(ipAddress);

		Country country = response.getCountry();

		log.info(" Country IsoCode : {}", country.getIsoCode()); // 'US'
		log.info(" Country Name : {}", country.getName()); // 'United States'
		log.info(" Country CnName : {}", country.getNames().get("zh-CN")); // '美国'

		Subdivision subdivision = response.getMostSpecificSubdivision();
		log.info(" Subdivision Name : {}", subdivision.getName()); // 'Minnesota'
		log.info(" Subdivision IsoCode : {}", subdivision.getIsoCode()); // 'MN'

		City city = response.getCity();
		log.info(" City Name : {}", city.getName()); // 'Minneapolis'

		Postal postal = response.getPostal();
		log.info(" Postal Code : {}",postal.getCode()); // '55455'

		Location location = response.getLocation();
		log.info(" Latitude : {}",location.getLatitude()); // 44.9733
		log.info(" Longitude : {}",location.getLongitude()); // -93.2323

		return response;
	}

    public Location getLocation(String ipAddress) throws Exception {
        return this.getLocation(ipAddress);
    }

	public Location getLocation(InetAddress ipAddress) throws Exception {

		log.info(" ================= Location Info For : {} ====================", ipAddress.toString());

		// Replace "city" with the appropriate method for your database, e.g., "country".
		CityResponse response = geoip2Reader.city(ipAddress);

		Location location = response.getLocation();
		log.info(" Latitude : {}",location.getLatitude()); // 44.9733
		log.info(" Longitude : {}",location.getLongitude()); // -93.2323

		return location;
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

}
