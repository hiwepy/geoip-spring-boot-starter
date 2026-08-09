package com.maxmind.db.spring.boot;

import com.maxmind.db.Network;
import com.maxmind.db.spring.boot.ext.RegionAddress;
import com.maxmind.db.spring.boot.ext.RegionEnum;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.AnonymousIpResponse;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.InetAddress;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link GeoIP2Template}.
 * @author [@Loong Wan](https://github.com/loong10k)
 */
@ExtendWith(MockitoExtension.class)
class GeoIP2TemplateTest {

    @Mock
    private DatabaseReader databaseReader;

    private GeoIP2Template template;

    private InetAddress testIp;

    @BeforeEach
    void setUp() throws Exception {
        template = new GeoIP2Template(databaseReader);
        testIp = InetAddress.getByName("128.101.101.101");
    }

    // --- Country tests ---

    @Test
    void getCountryByStringShouldReturnResponse() throws IOException, GeoIp2Exception {
        CountryResponse mockResponse = createMockCountryResponse();
        when(databaseReader.country(any(InetAddress.class))).thenReturn(mockResponse);

        CountryResponse response = template.getCountry("128.101.101.101");
        assertThat(response).isNotNull();
        assertThat(response.getCountry().getIsoCode()).isEqualTo("US");
    }

    @Test
    void getCountryByInetAddressShouldReturnResponse() throws IOException, GeoIp2Exception {
        CountryResponse mockResponse = createMockCountryResponse();
        when(databaseReader.country(testIp)).thenReturn(mockResponse);

        CountryResponse response = template.getCountry(testIp);
        assertThat(response).isNotNull();
        assertThat(response.getCountry().getName()).isEqualTo("United States");
    }

    @Test
    void getCountryShouldPropagateIoException() throws IOException, GeoIp2Exception {
        when(databaseReader.country(any(InetAddress.class))).thenThrow(new IOException("db error"));

        assertThatThrownBy(() -> template.getCountry("128.101.101.101"))
            .isInstanceOf(IOException.class)
            .hasMessage("db error");
    }

    // --- City tests ---

    @Test
    void getCityByStringShouldReturnResponse() throws IOException, GeoIp2Exception {
        CityResponse mockResponse = createMockCityResponse();
        when(databaseReader.city(any(InetAddress.class))).thenReturn(mockResponse);

        CityResponse response = template.getCity("128.101.101.101");
        assertThat(response).isNotNull();
        assertThat(response.getCity().getName()).isEqualTo("Minneapolis");
    }

    @Test
    void getCityByInetAddressShouldReturnResponse() throws IOException, GeoIp2Exception {
        CityResponse mockResponse = createMockCityResponse();
        when(databaseReader.city(testIp)).thenReturn(mockResponse);

        CityResponse response = template.getCity(testIp);
        assertThat(response).isNotNull();
        assertThat(response.getMostSpecificSubdivision().getName()).isEqualTo("Minnesota");
    }

    @Test
    void getCityShouldPropagateIoException() throws IOException, GeoIp2Exception {
        when(databaseReader.city(any(InetAddress.class))).thenThrow(new IOException("db error"));

        assertThatThrownBy(() -> template.getCity("128.101.101.101"))
            .isInstanceOf(IOException.class);
    }

    // --- Location tests ---

    @Test
    void getLocationByInetAddressShouldReturnLocation() throws Exception {
        CityResponse mockResponse = createMockCityResponse();
        when(databaseReader.city(testIp)).thenReturn(mockResponse);

        Location location = template.getLocation(testIp);
        assertThat(location).isNotNull();
        assertThat(location.getLatitude()).isEqualTo(44.9733);
        assertThat(location.getLongitude()).isEqualTo(-93.2323);
    }

    // --- AnonymousIp tests ---

    @Test
    void anonymousIpByInetAddressShouldReturnResponse() throws IOException, GeoIp2Exception {
        AnonymousIpResponse mockResponse = new AnonymousIpResponse(
            "128.101.101.101", false, false, false, false, false, false,
            new Network(InetAddress.getByName("128.101.101.0"), 24)
        );
        when(databaseReader.tryAnonymousIp(testIp)).thenReturn(Optional.of(mockResponse));

        Optional<AnonymousIpResponse> result = template.anonymousIp(testIp);
        assertThat(result).isPresent();
        assertThat(result.get().getIpAddress()).isEqualTo("128.101.101.101");
    }

    @Test
    void anonymousIpByStringShouldReturnResponse() throws IOException, GeoIp2Exception {
        AnonymousIpResponse mockResponse = new AnonymousIpResponse(
            "128.101.101.101", false, false, false, false, false, false,
            new Network(InetAddress.getByName("128.101.101.0"), 24)
        );
        when(databaseReader.tryAnonymousIp(any(InetAddress.class))).thenReturn(Optional.of(mockResponse));

        Optional<AnonymousIpResponse> result = template.anonymousIp("128.101.101.101");
        assertThat(result).isPresent();
    }

    // --- RegionAddress tests ---

    @Test
    void getRegionAddressShouldReturnAddress() throws IOException, GeoIp2Exception {
        CityResponse mockResponse = createMockCityResponse();
        when(databaseReader.city(any(InetAddress.class))).thenReturn(mockResponse);

        RegionAddress address = template.getRegionAddress("128.101.101.101");
        assertThat(address).isNotNull();
        assertThat(address.getCountry()).isEqualTo("美国");
        assertThat(address.getProvince()).isEqualTo("Minnesota");
        assertThat(address.getCity()).isEqualTo("Minneapolis");
    }

    @Test
    void getRegionAddressShouldReturnNotMatchOnException() throws Exception {
        // databaseReader.city() will throw since it's not stubbed for this IP
        when(databaseReader.city(any(InetAddress.class))).thenThrow(new IOException("error"));

        RegionAddress address = template.getRegionAddress("invalid");
        assertThat(address).isNotNull();
        assertThat(address.getCountry()).isEqualTo("未分配或者内网IP");
    }

    // --- RegionByIp tests ---

    @Test
    void getRegionByIpShouldReturnRegion() throws IOException, GeoIp2Exception {
        CountryResponse mockResponse = createMockCountryResponse();
        when(databaseReader.country(any(InetAddress.class))).thenReturn(mockResponse);

        RegionEnum region = template.getRegionByIp("128.101.101.101");
        assertThat(region).isEqualTo(RegionEnum.US);
    }

    @Test
    void getRegionByIpShouldReturnUnknownOnException() throws Exception {
        when(databaseReader.country(any(InetAddress.class))).thenThrow(new IOException("error"));

        RegionEnum region = template.getRegionByIp("128.101.101.101");
        assertThat(region).isEqualTo(RegionEnum.UK);
    }

    @Test
    void getRegionByIpShouldReturnUnknownForNonIpv4() {
        RegionEnum region = template.getRegionByIp("not-an-ip");
        assertThat(region).isEqualTo(RegionEnum.UK);
    }

    // --- isMainlandIp tests ---

    @Test
    void isMainlandIpShouldReturnTrueForChinaIp() throws IOException, GeoIp2Exception {
        CountryResponse mockResponse = createMockCountryResponse("CN", "China", "中国");
        when(databaseReader.country(any(InetAddress.class))).thenReturn(mockResponse);

        boolean result = template.isMainlandIp("1.2.3.4");
        assertThat(result).isTrue();
    }

    @Test
    void isMainlandIpShouldReturnFalseForUsIp() throws IOException, GeoIp2Exception {
        CountryResponse mockResponse = createMockCountryResponse("US", "United States", "美国");
        when(databaseReader.country(any(InetAddress.class))).thenReturn(mockResponse);

        boolean result = template.isMainlandIp("128.101.101.101");
        assertThat(result).isFalse();
    }

    @Test
    void isMainlandIpShouldReturnFalseForHkIp() throws IOException, GeoIp2Exception {
        CountryResponse mockResponse = createMockCountryResponse("HK", "Hong Kong", "中国香港");
        when(databaseReader.country(any(InetAddress.class))).thenReturn(mockResponse);

        boolean result = template.isMainlandIp("1.2.3.4");
        assertThat(result).isFalse();
    }

    @Test
    void isMainlandIpShouldReturnFalseForException() throws Exception {
        when(databaseReader.country(any(InetAddress.class))).thenThrow(new IOException("error"));

        boolean result = template.isMainlandIp("1.2.3.4");
        assertThat(result).isFalse();
    }

    // --- Helper methods ---

    private CountryResponse createMockCountryResponse() {
        return createMockCountryResponse("US", "United States", "美国");
    }

    private static final List<String> EN_LOCALE = List.of("en");
    private static final List<String> EN_ZH_LOCALE = List.of("en", "zh-CN");

    private CountryResponse createMockCountryResponse(String code2, String name, String cnName) {
        Map<String, String> names = new HashMap<>();
        names.put("zh-CN", cnName);
        names.put("en", name);

        Country country = new Country(EN_ZH_LOCALE, 1, 1L, false, code2, names);
        Continent continent = new Continent(EN_LOCALE, "NA", 1L, Collections.emptyMap());
        MaxMind maxMind = new MaxMind(1);
        Country registeredCountry = new Country(EN_ZH_LOCALE, 1, 1L, false, code2, names);
        RepresentedCountry representedCountry = new RepresentedCountry();
        Traits traits = new Traits();

        return new CountryResponse(continent, country, maxMind, registeredCountry, representedCountry, traits);
    }

    private CityResponse createMockCityResponse() {
        Map<String, String> countryNames = new HashMap<>();
        countryNames.put("zh-CN", "美国");
        countryNames.put("en", "United States");

        Country country = new Country(EN_ZH_LOCALE, 1, 1L, false, "US", countryNames);
        Continent continent = new Continent(EN_LOCALE, "NA", 1L, Collections.emptyMap());
        MaxMind maxMind = new MaxMind(1);
        Country registeredCountry = new Country(EN_ZH_LOCALE, 1, 1L, false, "US", countryNames);
        RepresentedCountry representedCountry = new RepresentedCountry();

        Map<String, String> cityNames = new HashMap<>();
        cityNames.put("en", "Minneapolis");
        City city = new City(EN_LOCALE, 1, 1L, cityNames);

        Location location = new Location(1, 1, 44.9733, -93.2323, 55455, 1, "America/Chicago");

        Postal postal = new Postal("55455", 1);

        Map<String, String> subNames = new HashMap<>();
        subNames.put("en", "Minnesota");
        Subdivision subdivision = new Subdivision(EN_LOCALE, 1, 1L, "MN", subNames);
        ArrayList<Subdivision> subdivisions = new ArrayList<>();
        subdivisions.add(subdivision);

        Traits traits = new Traits();

        return new CityResponse(
            city, continent, country, location, maxMind, postal,
            registeredCountry, representedCountry, subdivisions, traits
        );
    }
}
