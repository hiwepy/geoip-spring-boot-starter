package com.maxmind.db.spring.boot.ext;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link RegionEnum}.
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
class RegionEnumTest {

    @Test
    void getByCode2ShouldReturnCorrectRegion() {
        assertThat(RegionEnum.getByCode2("CN")).isEqualTo(RegionEnum.CN);
        assertThat(RegionEnum.getByCode2("US")).isEqualTo(RegionEnum.US);
        assertThat(RegionEnum.getByCode2("JP")).isEqualTo(RegionEnum.JP);
    }

    @Test
    void getByCode2ShouldBeCaseInsensitive() {
        assertThat(RegionEnum.getByCode2("cn")).isEqualTo(RegionEnum.CN);
        assertThat(RegionEnum.getByCode2("us")).isEqualTo(RegionEnum.US);
    }

    @Test
    void getByCode2ShouldReturnUnknownForInvalid() {
        assertThat(RegionEnum.getByCode2("XX")).isEqualTo(RegionEnum.UK);
    }

    @Test
    void getByCode3ShouldReturnCorrectRegion() {
        assertThat(RegionEnum.getByCode3("CHN")).isEqualTo(RegionEnum.CN);
        assertThat(RegionEnum.getByCode3("USA")).isEqualTo(RegionEnum.US);
    }

    @Test
    void getByCode3ShouldReturnUnknownForInvalid() {
        assertThat(RegionEnum.getByCode3("XXX")).isEqualTo(RegionEnum.UK);
    }

    @Test
    void getByNumberShouldReturnCorrectRegion() {
        assertThat(RegionEnum.getByNumber("156")).isEqualTo(RegionEnum.CN);
        assertThat(RegionEnum.getByNumber("840")).isEqualTo(RegionEnum.US);
    }

    @Test
    void getByNumberShouldReturnUnknownForInvalid() {
        assertThat(RegionEnum.getByNumber("9999")).isEqualTo(RegionEnum.UK);
    }

    @Test
    void getByIsoCodeShouldReturnCorrectRegion() {
        assertThat(RegionEnum.getByIsoCode("ISO 3166-2:CN")).isEqualTo(RegionEnum.CN);
        assertThat(RegionEnum.getByIsoCode("ISO 3166-2:US")).isEqualTo(RegionEnum.US);
    }

    @Test
    void getByIsoCodeShouldReturnUnknownForInvalid() {
        assertThat(RegionEnum.getByIsoCode("INVALID")).isEqualTo(RegionEnum.UK);
    }

    @Test
    void getByIsoNameShouldReturnCorrectRegion() {
        assertThat(RegionEnum.getByIsoName("China")).isEqualTo(RegionEnum.CN);
        assertThat(RegionEnum.getByIsoName("United States of America (USA)")).isEqualTo(RegionEnum.US);
    }

    @Test
    void getByIsoNameShouldReturnUnknownForInvalid() {
        assertThat(RegionEnum.getByIsoName("NonExistent")).isEqualTo(RegionEnum.UK);
    }

    @Test
    void getByCnNameShouldReturnCorrectRegion() {
        assertThat(RegionEnum.getByCnName("中国")).isEqualTo(RegionEnum.CN);
        assertThat(RegionEnum.getByCnName("美国")).isEqualTo(RegionEnum.US);
    }

    @Test
    void getByCnNameShouldReturnUnknownForInvalid() {
        assertThat(RegionEnum.getByCnName("不存在")).isEqualTo(RegionEnum.UK);
    }

    @Test
    void gettersShouldReturnCorrectValues() {
        RegionEnum cn = RegionEnum.CN;
        assertThat(cn.getNumber()).isEqualTo("156");
        assertThat(cn.getCode2()).isEqualTo("CN");
        assertThat(cn.getCode3()).isEqualTo("CHN");
        assertThat(cn.getIsoCode()).isEqualTo("ISO 3166-2:CN");
        assertThat(cn.getIsoName()).isEqualTo("China");
        assertThat(cn.getCname()).isEqualTo("中国");
    }

    @Test
    void getByRegionAddressShouldMatchCountry() {
        RegionAddress address = new RegionAddress("中国", "江苏省", "南京市", "华东", "电信");
        RegionEnum region = RegionEnum.getByRegionAddress(address);
        assertThat(region).isEqualTo(RegionEnum.CN);
    }

    @Test
    void getByRegionAddressShouldReturnUnknownForNoMatch() {
        RegionAddress address = new RegionAddress("NonExistent", "Province", "City", "Area", "ISP");
        RegionEnum region = RegionEnum.getByRegionAddress(address);
        assertThat(region).isEqualTo(RegionEnum.UK);
    }

    @Test
    void unknownEnumShouldHaveCorrectValues() {
        RegionEnum uk = RegionEnum.UK;
        assertThat(uk.getCode2()).isEqualTo("UK");
        assertThat(uk.getCode3()).isEqualTo("UNKNOWN");
        assertThat(uk.getCname()).isEqualTo("未知国家地区");
    }

    @Test
    void valuesShouldContainAllMajorRegions() {
        RegionEnum[] values = RegionEnum.values();
        assertThat(values.length).isGreaterThan(200);
    }
}
