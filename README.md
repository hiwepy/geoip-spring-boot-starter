# geoip-spring-boot-starter

### 组件简介

> 最新IP数据下载地址

https://github.com/maxmind/GeoIP2-java

[GeoLite2 开源数据库](https://github.com/gongruihua/geoip2)

[GeoLite2 开源数据库](https://github.com/P3TERX/GeoLite.mmdb)


### 使用说明

##### 1、Spring Boot 项目添加 Maven 依赖

``` xml
<dependency>
	<groupId>com.github.hiwepy</groupId>
	<artifactId>geoip-spring-boot-starter</artifactId>
	<version>2.0.0.RELEASE</version>
</dependency>
```

##### 2、在`application.yml`文件中增加如下配置

```yaml
################################################################################################################
###geoip基本配置：
################################################################################################################
geoip2:
  location: classpath:GeoLite2-Country.mmdb
```

##### 3、使用示例

```java

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
```

## Jeebiz 技术社区

Jeebiz 技术社区 **微信公共号**、**小程序**，欢迎关注反馈意见和一起交流，关注公众号回复「Jeebiz」拉你入群。

|公共号|小程序|
|---|---|
| ![](https://raw.githubusercontent.com/hiwepy/static/main/images/qrcode_for_gh_1d965ea2dfd1_344.jpg)| ![](https://raw.githubusercontent.com/hiwepy/static/main/images/gh_09d7d00da63e_344.jpg)|





