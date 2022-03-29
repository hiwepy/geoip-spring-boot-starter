package com.maxmind.db.spring.boot;

import com.maxmind.db.spring.boot.ext.RegionAddress;
import com.maxmind.geoip2.DatabaseReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Autowired
	GeoIP2Template geoIP2Template;

	@Test
	public void contextLoads() throws Exception {

		// QQ qqwry.dat API 根据IP获取地址位置  离线获取IP的位置
		System.out.println(geoIP2Template.getCountry("115.159.94.190"));
		System.out.println(geoIP2Template.getCity("115.159.94.190"));
		System.out.println(geoIP2Template.getLocation("115.159.94.190"));
		System.out.println(geoIP2Template.getCity("115.159.94.190"));
		System.out.println(geoIP2Template.getCity("115.159.94.190"));

		RegionAddress mapLL2 = geoIP2Template.getRegionAddress("183.128.136.82"); // lng：116.86380647644208  lat：38.297615350325717
		System.out.println(mapLL2);

	}

	public static void main(String[] args) throws IOException {

		// A File object pointing to your GeoIP2 or GeoLite2 database
		File database = new File("D:\\geoip\\GeoLite2-City.mmdb");

		// This creates the DatabaseReader object. To improve performance, reuse
		// the object across lookups. The object is thread-safe.
		DatabaseReader reader = new DatabaseReader.Builder(database).build();

		GeoIP2Template template = new GeoIP2Template(reader);

		RegionAddress mapLL2 = template.getRegionAddress("183.128.136.82"); // lng：116.86380647644208  lat：38.297615350325717
		System.out.println(mapLL2);
	}

}
