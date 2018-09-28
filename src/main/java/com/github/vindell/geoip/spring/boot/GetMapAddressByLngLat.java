package com.github.vindell.geoip.spring.boot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 根据百度API经纬度获取地理位置信息
 * 
 * @author achen
 *
 */
public class GetMapAddressByLngLat {

	/**调用百度API
	 * 
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	public static String GetLocationMsg(double latitude,double longitude){
		String message = "";
        String url = String.format(
            "http://maps.google.cn/maps/api/geocode/json?latlng=%s,%s&language=CN",
            latitude,longitude);
        URL myURL = null;
        URLConnection httpsConn = null;
        try {
            myURL = new URL(url);
        } catch (MalformedURLException e) {
          e.printStackTrace();
        }
        try {
            httpsConn = (URLConnection) myURL.openConnection();
            if (httpsConn != null) {
                InputStreamReader insr = new InputStreamReader(
                        httpsConn.getInputStream(), "UTF-8");
                BufferedReader br = new BufferedReader(insr);
                String data = null;
                while ((data = br.readLine()) != null) {
                message = message+data;
                }
                insr.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
	}
	
	// 在main函数调用步骤一中的方法：GetLocationMsg
	public final static void main(String[] args) {
		System.out.println(GetLocationMsg(34.756610064140257, 113.64964384986449));
		System.err.println(GetLocationMsg(32.7763644055,100.4338731743));
	}
}
