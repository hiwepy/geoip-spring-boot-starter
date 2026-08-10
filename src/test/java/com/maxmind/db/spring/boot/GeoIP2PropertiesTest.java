package com.maxmind.db.spring.boot;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link GeoIP2Properties}.
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
class GeoIP2PropertiesTest {

    @Test
    void defaultLocationShouldBeClasspathResource() {
        GeoIP2Properties properties = new GeoIP2Properties();
        assertThat(properties.getLocation()).isEqualTo("classpath:GeoLite2-Country.mmdb");
    }

    @Test
    void shouldSetAndGetLocation() {
        GeoIP2Properties properties = new GeoIP2Properties();
        properties.setLocation("/opt/geoip/GeoLite2-City.mmdb");
        assertThat(properties.getLocation()).isEqualTo("/opt/geoip/GeoLite2-City.mmdb");
    }

    @Test
    void prefixShouldBeGeoip2() {
        assertThat(GeoIP2Properties.PREFIX).isEqualTo("geoip2");
    }

    @Test
    void shouldSetLocationToNull() {
        GeoIP2Properties properties = new GeoIP2Properties();
        properties.setLocation(null);
        assertThat(properties.getLocation()).isNull();
    }
}
