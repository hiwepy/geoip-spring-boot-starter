/*
 * Copyright (c) 2018, vindell (https://github.com/vindell).
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
package com.github.vindell.geoip.spring.boot;

import java.io.File;
import java.net.InetAddress;

import org.junit.Test;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.EnterpriseResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Postal;
import com.maxmind.geoip2.record.Subdivision;

public class GeoIP2Domain_Test {
	
	@Test
	public void testName() throws Exception {
		
		// A File object pointing to your GeoIP2 Enterprise database
		File database = new File("/path/to/GeoIP2-Enterprise.mmdb");

		// This creates the DatabaseReader object. To improve performance, reuse
		// the object across lookups. The object is thread-safe.
		try (DatabaseReader reader = new DatabaseReader.Builder(database).build()) {
		    InetAddress ipAddress = InetAddress.getByName("128.101.101.101");

		    //  Use the enterprise(ip) method to do a lookup in the Enterprise database
		    EnterpriseResponse response = reader.enterprise(ipAddress);

		    Country country = response.getCountry();
		    System.out.println(country.getIsoCode());            // 'US'
		    System.out.println(country.getName());               // 'United States'
		    System.out.println(country.getNames().get("zh-CN")); // '美国'
		    System.out.println(country.getConfidence());         // 99

		    Subdivision subdivision = response.getMostSpecificSubdivision();
		    System.out.println(subdivision.getName());           // 'Minnesota'
		    System.out.println(subdivision.getIsoCode());        // 'MN'
		    System.out.println(subdivision.getConfidence());     // 77

		    City city = response.getCity();
		    System.out.println(city.getName());       // 'Minneapolis'
		    System.out.println(city.getConfidence()); // 11

		    Postal postal = response.getPostal();
		    System.out.println(postal.getCode()); // '55455'
		    System.out.println(postal.getConfidence()); // 5

		    Location location = response.getLocation();
		    System.out.println(location.getLatitude());  // 44.9733
		    System.out.println(location.getLongitude()); // -93.2323
		    System.out.println(location.getAccuracyRadius()); // 50
		}
		
	}
	

}
