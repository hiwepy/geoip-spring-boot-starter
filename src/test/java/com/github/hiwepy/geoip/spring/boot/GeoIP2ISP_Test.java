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
package com.github.hiwepy.geoip.spring.boot;

import java.io.File;
import java.net.InetAddress;

import org.junit.Test;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.IspResponse;

public class GeoIP2ISP_Test {
	
	@Test
	public void testName() throws Exception {
		
		// A File object pointing to your GeoIP2 ISP database
		File database = new File("/path/to/GeoIP2-ISP.mmdb");

		// This creates the DatabaseReader object. To improve performance, reuse
		// the object across lookups. The object is thread-safe.
		DatabaseReader reader = new DatabaseReader.Builder(database).build();

		InetAddress ipAddress = InetAddress.getByName("128.101.101.101");

		IspResponse response = reader.isp(ipAddress);

		System.out.println(response.getAutonomousSystemNumber());       // 217
		System.out.println(response.getAutonomousSystemOrganization()); // 'University of Minnesota'
		System.out.println(response.getIsp());                          // 'University of Minnesota'
		System.out.println(response.getOrganization());                 // 'University of Minnesota'
		
	}
	

}
