package com.maxmind.db.spring.boot.ext;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link RegionAddress}.
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class RegionAddressTest {

    @Test
    void defaultConstructorShouldCreateEmptyObject() {
        RegionAddress address = new RegionAddress();
        assertThat(address.getCountry()).isNull();
        assertThat(address.getProvince()).isNull();
        assertThat(address.getCity()).isNull();
        assertThat(address.getArea()).isNull();
        assertThat(address.getISP()).isNull();
    }

    @Test
    void fullConstructorShouldSetAllFields() {
        RegionAddress address = new RegionAddress("China", "Jiangsu", "Nanjing", "East", "Telecom");
        assertThat(address.getCountry()).isEqualTo("China");
        assertThat(address.getProvince()).isEqualTo("Jiangsu");
        assertThat(address.getCity()).isEqualTo("Nanjing");
        assertThat(address.getArea()).isEqualTo("East");
        assertThat(address.getISP()).isEqualTo("Telecom");
    }

    @Test
    void arrayConstructorShouldParseCorrectly() {
        // format: country|area|province|city|ISP
        String[] region = {"China", "East", "Jiangsu", "Nanjing", "Telecom"};
        RegionAddress address = new RegionAddress(region);
        assertThat(address.getCountry()).isEqualTo("China");
        assertThat(address.getProvince()).isEqualTo("Jiangsu");
        assertThat(address.getCity()).isEqualTo("Nanjing");
        assertThat(address.getArea()).isEqualTo("East");
        assertThat(address.getISP()).isEqualTo("Telecom");
    }

    @Test
    void settersShouldWork() {
        RegionAddress address = new RegionAddress();
        address.setCountry("US");
        address.setProvince("California");
        address.setCity("San Francisco");
        address.setArea("West");
        address.setISP("AT&T");

        assertThat(address.getCountry()).isEqualTo("US");
        assertThat(address.getProvince()).isEqualTo("California");
        assertThat(address.getCity()).isEqualTo("San Francisco");
        assertThat(address.getArea()).isEqualTo("West");
        assertThat(address.getISP()).isEqualTo("AT&T");
    }

    @Test
    void toStringShouldContainAllFields() {
        RegionAddress address = new RegionAddress("China", "Jiangsu", "Nanjing", "East", "Telecom");
        String result = address.toString();
        assertThat(result).contains("China");
        assertThat(result).contains("Jiangsu");
        assertThat(result).contains("Nanjing");
        assertThat(result).contains("East");
        assertThat(result).contains("Telecom");
    }
}
