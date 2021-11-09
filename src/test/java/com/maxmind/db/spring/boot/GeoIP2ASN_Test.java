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

import com.maxmind.geoip2.model.AsnResponse;
import org.junit.Test;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Postal;
import com.maxmind.geoip2.record.Subdivision;

public class GeoIP2ASN_Test {

	@Test
	public void testName() throws Exception {

		// A File object pointing to your GeoLite2 ASN database
		File database = new File("D:\\geoip\\GeoLite2-ASN.mmdb");

		// This creates the DatabaseReader object. To improve performance, reuse
		// the object across lookups. The object is thread-safe.
		try (DatabaseReader reader = new DatabaseReader.Builder(database).build()) {

			InetAddress ipAddress = InetAddress.getByName("128.101.101.101");

			AsnResponse response = reader.asn(ipAddress);

			System.out.println(response.getNetwork());       // 128.101.0.0/16
			System.out.println(response.getAutonomousSystemNumber());       // 217
			System.out.println(response.getAutonomousSystemOrganization()); // 'UMN-SYSTEM'
		}

	}


}
