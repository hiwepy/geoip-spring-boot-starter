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

import java.io.IOException;
import java.net.InetAddress;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.AnonymousIpResponse;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Postal;
import com.maxmind.geoip2.record.Subdivision;

public class GeoIPTemplate {

	private final ObjectMapper objectMapper;
	
	public GeoIPTemplate(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
	/**
	 * TODO
	 * @param ipAddress IPv4 or IPv6 address to lookup.
	 * @return
	 * @throws IOException
	 * @throws GeoIp2Exception
	 */
	public Optional<AnonymousIpResponse> anonymousIp(DatabaseReader dbReader, String ipAddress) throws IOException, GeoIp2Exception {
		return this.anonymousIp(dbReader, InetAddress.getByName(ipAddress));
	}
	
	public Optional<AnonymousIpResponse> anonymousIp(DatabaseReader dbReader, InetAddress ipAddress) throws IOException, GeoIp2Exception {
		return dbReader.tryAnonymousIp(ipAddress);
	}
	
	/**
	 * TODO
	 * @param ipAddress IPv4 or IPv6 address to lookup.
	 * @return
	 * @throws IOException
	 * @throws GeoIp2Exception
	 */
	public CityResponse searchResponse(DatabaseReader dbReader, String ipAddress) throws IOException, GeoIp2Exception {
		
		
		// Replace "city" with the appropriate method for your database, e.g., "country".
		CityResponse response = dbReader.city(InetAddress.getByName(ipAddress));

		Country country = response.getCountry();
		System.out.println(country.getIsoCode()); // 'US'
		System.out.println(country.getName()); // 'United States'
		System.out.println(country.getNames().get("zh-CN")); // '美国'

		Subdivision subdivision = response.getMostSpecificSubdivision();
		System.out.println(subdivision.getName()); // 'Minnesota'
		System.out.println(subdivision.getIsoCode()); // 'MN'

		City city = response.getCity();
		System.out.println(city.getName()); // 'Minneapolis'

		Postal postal = response.getPostal();
		System.out.println(postal.getCode()); // '55455'

		Location location = response.getLocation();
		System.out.println(location.getLatitude()); // 44.9733
		System.out.println(location.getLongitude()); // -93.2323

		return response;
	}
	
	/**
     * 
     * @description: 获得国家 
     * @param dbReader
     * @param ip
     * @return
     * @throws Exception
     */
    public static String getCountry(DatabaseReader dbReader, String ip) throws Exception {
        return dbReader.city(InetAddress.getByName(ip)).getCountry().getNames().get("zh-CN");
    }

    /**
     * 
     * @description: 获得省份 
     * @param dbReader
     * @param ip
     * @return
     * @throws Exception
     */
    public static String getProvince(DatabaseReader dbReader, String ip) throws Exception {
        return dbReader.city(InetAddress.getByName(ip)).getMostSpecificSubdivision().getNames().get("zh-CN");
    }

    /**
     * 
     * @description: 获得城市 
     * @param dbReader
     * @param ip
     * @return
     * @throws Exception
     */
    public static String getCity(DatabaseReader dbReader, String ip) throws Exception {
        return dbReader.city(InetAddress.getByName(ip)).getCity().getNames().get("zh-CN");
    }
    
    /**
     * 
     * @description: 获得经度 
     * @param dbReader
     * @param ip
     * @return
     * @throws Exception
     */
    public static Double getLongitude(DatabaseReader dbReader, String ip) throws Exception {
        return dbReader.city(InetAddress.getByName(ip)).getLocation().getLongitude();
    }
    
    /**
     * 
     * @description: 获得纬度
     * @param dbReader
     * @param ip
     * @return
     * @throws Exception
     */
    public static Double getLatitude(DatabaseReader dbReader, String ip) throws Exception {
        return dbReader.city(InetAddress.getByName(ip)).getLocation().getLatitude();
    }
    
	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}
	
}
