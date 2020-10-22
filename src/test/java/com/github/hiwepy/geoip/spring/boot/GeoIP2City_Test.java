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
package com.github.hiwepy.geoip.spring.boot;

import java.io.File;
import java.net.InetAddress;

import org.junit.Test;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.AnonymousIpResponse;
import com.maxmind.geoip2.model.ConnectionTypeResponse;
import com.maxmind.geoip2.model.ConnectionTypeResponse.ConnectionType;

/**
 * https://github.com/maxmind/GeoIP2-java
 */
public class GeoIP2City_Test {
	
	@Test
	public void testName() throws Exception {
		
		// A File object pointing to your GeoIP2 Connection-Type database
		File database = new File("/path/to/GeoIP2-Connection-Type.mmdb");

		// This creates the DatabaseReader object. To improve performance, reuse
		// the object across lookups. The object is thread-safe.
		DatabaseReader reader = new DatabaseReader.Builder(database).build();

		InetAddress ipAddress = InetAddress.getByName("128.101.101.101");

		ConnectionTypeResponse response = reader.connectionType(ipAddress);

		// getConnectionType() returns a ConnectionType enum
		ConnectionType type = response.getConnectionType();

		System.out.println(type); // 'Corporate'
		
	}
	

}
