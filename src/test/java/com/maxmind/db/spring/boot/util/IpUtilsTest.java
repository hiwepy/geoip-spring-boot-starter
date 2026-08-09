package com.maxmind.db.spring.boot.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link IpUtils}.
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class IpUtilsTest {

    @ParameterizedTest
    @ValueSource(strings = {
        "192.168.1.1",
        "10.0.0.1",
        "255.255.255.255",
        "0.0.0.0",
        "127.0.0.1",
        "172.16.0.1"
    })
    void shouldValidateIpv4Addresses(String ip) {
        assertThat(IpUtils.isIpv4(ip)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "256.1.1.1",
        "192.168.1",
        "192.168.1.1.1",
        "abc.def.ghi.jkl",
        "",
        "::1",
        "fe80:1295:8030:1fc6:57fa:0000:0000:1fc6"
    })
    void shouldRejectInvalidIpv4Addresses(String ip) {
        assertThat(IpUtils.isIpv4(ip)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "fe80:1295:8030:1fc6:57fa:0000:0000:1fc6",
        "fe80:1295:8030:1fc6:57fa:0000:0000:8030",
        "2001:0db8:85a3:0000:0000:8a2e:0370:7334",
        "0000:0000:0000:0000:0000:0000:0000:0001"
    })
    void shouldValidateIpv6Addresses(String ip) {
        assertThat(IpUtils.isIpv6(ip)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "192.168.1.1",
        "1610329044 ",
        "fe80:1295:8030:1fc6:57fa:0000:0000",
        "not-an-ip",
        ""
    })
    void shouldRejectInvalidIpv6Addresses(String ip) {
        assertThat(IpUtils.isIpv6(ip)).isFalse();
    }
}
