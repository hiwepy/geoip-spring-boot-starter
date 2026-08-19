package com.maxmind.db.spring.boot.ext;

/**
 * Basic RegionAddress Info
 * @author 凡梦星尘(elkan1788@gmail.com)
 */
public class RegionAddress {

    private String country;
    private String province;
    private String city;
    private String area;
    private String ISP;

    public RegionAddress() {
    }

    /**
     * Translate this string "中国|华东|江苏省|南京市|电信" to location fields.
     * @param region location region address info array
     */
    public RegionAddress(String[] region) {
        this(region[0], region[2], region[3], region[1], region[4]);
    }

    /**
     * Basic constructor method
     * @param country   Country name
     * @param province  province name
     * @param city      city name
     * @param area      area name
     * @param ISP       ISP name
     */
    public RegionAddress(String country, String province, String city, String area, String ISP) {
        this.country = country;
        this.province = province;
        this.city = city;
        this.area = area;
        this.ISP = ISP;
    }

    /** @return return the country. */
    public String getCountry() {
        return country;
    }

    /** @param country set the country. */
    public void setCountry(String country) {
        this.country = country;
    }

    /** @return return the province. */
    public String getProvince() {
        return province;
    }

    /** @param province set the province. */
    public void setProvince(String province) {
        this.province = province;
    }

    /** @return return the city. */
    public String getCity() {
        return city;
    }

    /** @param city set the city. */
    public void setCity(String city) {
        this.city = city;
    }

    /** @return return the area. */
    public String getArea() {
        return area;
    }

    /** @param area set the area. */
    public void setArea(String area) {
        this.area = area;
    }

    /** @return return the i s p. */
    public String getISP() {
        return ISP;
    }

    /** @param ISP set the i s p. */
    public void setISP(String ISP) {
        this.ISP = ISP;
    }

    @Override
    /**
     * <p>To string.</p>
     * @return the result
     */
    public String toString() {
        return "RegionAddress{" +
                "country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", area='" + area + '\'' +
                ", ISP='" + ISP + '\'' +
                '}';
    }
}
