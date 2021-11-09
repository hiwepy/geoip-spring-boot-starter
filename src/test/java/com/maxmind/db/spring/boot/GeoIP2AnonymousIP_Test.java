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

import java.io.File;
import java.net.InetAddress;

import com.maxmind.geoip2.model.AnonymousIpResponse;
import org.junit.Test;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Postal;
import com.maxmind.geoip2.record.Subdivision;
/**
 * https://github.com/maxmind/GeoIP2-java
 */
public class GeoIP2AnonymousIP_Test {

	@Test
	public void testName() throws Exception {

		// A File object pointing to your GeoIP2 Anonymous IP database
		File database = new File("/path/to/GeoIP2-Anonymous-IP.mmdb");

		// This creates the DatabaseReader object. To improve performance, reuse
		// the object across lookups. The object is thread-safe.
		DatabaseReader reader = new DatabaseReader.Builder(database).build();

		try {
			InetAddress ipAddress = InetAddress.getByName("85.25.43.84");

			AnonymousIpResponse response = reader.anonymousIp(ipAddress);

			System.out.println(response.isAnonymous()); // true
			System.out.println(response.isAnonymousVpn()); // false
			System.out.println(response.isHostingProvider()); // false
			System.out.println(response.isPublicProxy()); // false
			System.out.println(response.isResidentialProxy()); // false
			System.out.println(response.isTorExitNode()); //true
		} finally {
			reader.close();
		}

	}


}
