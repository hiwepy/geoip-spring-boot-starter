package com.github.vindell.geoip.spring.boot;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.github.vindell.httpconn.HttpConnectionUtils;
import com.github.vindell.httpconn.handler.JSONResponseHandler;

/**
 * 根据百度API地址获取经纬度
 * @author achen
 * http://lbsyun.baidu.com/index.php?title=webapi/guide/webservice-geocoding
 */
public class GetMapLngLatByAddress {
	
	private static String geocoder = "http://api.map.baidu.com/geocoder/v2/?address=%s&output=json&ak=%s";
	
	public static void main(String[] args) throws IOException {
		Map<String, BigDecimal> mapLL = getLatAndLngByAddress("河南省郑州市"); // lng：116.86380647644208  lat：38.297615350325717
		mapLL.get("lat");
		mapLL.get("lng");
		System.out.println("lng："+mapLL.get("lng") + "  lat："+mapLL.get("lat"));
	}

	/**
	 * 调用百度API 
	 * @param addr
	 * @return
	 * @throws IOException 
	 */
	public static Map<String, BigDecimal> getLatAndLngByAddress(String addr) throws IOException{
        String address = "";
        try {  
            address = java.net.URLEncoder.encode(addr,"UTF-8");  
        } catch (UnsupportedEncodingException e1) {  
            e1.printStackTrace();  
        } 
        
        String url = String.format(geocoder, address, "CGxeqGuAGgP7n475kMPTi58y2EqjAPTh");
        // {"message":"APP Referer校验失败","status":220}
        JSONObject json = HttpConnectionUtils.httpRequestWithGet(url, new JSONResponseHandler());
        if(json.getInteger("status") != 0) {
        	throw new IOException(json.getString("message"));
        }
        
        JSONObject result = json.getJSONObject("result");
        JSONObject location = result.getJSONObject("location");
        
        Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
        map.put("lat", location.getBigDecimal("lat"));
        map.put("lng", location.getBigDecimal("lng"));
        return map;
	}
}
