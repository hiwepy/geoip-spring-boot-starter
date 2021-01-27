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
package com.maxmind.db.spring.boot;

import java.io.File;
import java.net.InetAddress;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.maxmind.geoip2.DatabaseReader;

/**
 * 
 * @description: 获取客户端IP地址
 * @author: paulandcode
 * @email: paulandcode@gmail.com
 * @since: 2018年9月17日 下午3:44:51
 */
public class IPUtils {
    private static Logger logger = LoggerFactory.getLogger(IPUtils.class);

    /**
     * 获取客户端IP地址
     * 
     * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
     * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
     */
    public static String getIP(HttpServletRequest request) {
        String ip = null;
        try {
            ip = request.getHeader("x-forwarded-for");
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception e) {
            logger.error("IPUtils ERROR ", e);
        }
        return ip;
    }

    /**
     * 
     * @description: 获得国家 
     * @param reader
     * @param ip
     * @return
     * @throws Exception
     */
    public static String getCountry(DatabaseReader reader, String ip) throws Exception {
        return reader.city(InetAddress.getByName(ip)).getCountry().getNames().get("zh-CN");
    }

    /**
     * 
     * @description: 获得省份 
     * @param reader
     * @param ip
     * @return
     * @throws Exception
     */
    public static String getProvince(DatabaseReader reader, String ip) throws Exception {
        return reader.city(InetAddress.getByName(ip)).getMostSpecificSubdivision().getNames().get("zh-CN");
    }

    /**
     * 
     * @description: 获得城市 
     * @param reader
     * @param ip
     * @return
     * @throws Exception
     */
    public static String getCity(DatabaseReader reader, String ip) throws Exception {
        return reader.city(InetAddress.getByName(ip)).getCity().getNames().get("zh-CN");
    }
    
    /**
     * 
     * @description: 获得经度 
     * @param reader
     * @param ip
     * @return
     * @throws Exception
     */
    public static Double getLongitude(DatabaseReader reader, String ip) throws Exception {
        return reader.city(InetAddress.getByName(ip)).getLocation().getLongitude();
    }
    
    /**
     * 
     * @description: 获得纬度
     * @param reader
     * @param ip
     * @return
     * @throws Exception
     */
    public static Double getLatitude(DatabaseReader reader, String ip) throws Exception {
        return reader.city(InetAddress.getByName(ip)).getLocation().getLatitude();
    }
    
    public static void main(String[] args) throws Exception {
    	// String path = req.getSession().getServletContext().getRealPath("/WEB-INF/classes/GeoLite2-City.mmdb");
    	String path = "D:/GeoLite2-City.mmdb";
    	// 创建 GeoLite2 数据库
    	File database = new File(path);
    	// 读取数据库内容
    	DatabaseReader reader = new DatabaseReader.Builder(database).build();
    	// 访问IP
    	String ip = "115.204.225.154";
    	String site = IPUtils.getCountry(reader, ip) + "-" + IPUtils.getProvince(reader, ip) + "-" + IPUtils.getCity(reader, ip)+ "-" + IPUtils.getLongitude(reader, ip)+ "-" + IPUtils.getLatitude(reader, ip);
    	
    	System.out.println(site);
	}
}