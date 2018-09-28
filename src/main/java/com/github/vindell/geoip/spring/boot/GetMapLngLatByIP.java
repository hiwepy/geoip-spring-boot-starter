package com.github.vindell.geoip.spring.boot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import com.alibaba.fastjson.JSONObject;
import com.github.vindell.httpconn.HttpConnectionUtils;
import com.github.vindell.httpconn.handler.JSONResponseHandler;

/**
 * 根据百度API  IP获取经纬度
 * @author achen
 *
 */
public class GetMapLngLatByIP {

	public static void main(String[] args) {
		String[] mapLL = getIPXY("115.159.94.190"); // lng：116.86380647644208  lat：38.297615350325717
		String aa = mapLL[1];
		System.out.println(aa);
	}
	
	private static String geocoder = "http://api.map.baidu.com/location/ip?ak=%s&ip=%s&coor=bd09ll";
	
   /**
	* 获取指定IP对应的经纬度（为空返回当前机器经纬度）
	* 
	* @param ip
	* @return
	*/
	public static String[] getIPXY(String ip) {

		if (null == ip) {
			ip = "";
		}
		try {
			
			 String url = String.format(geocoder, "CGxeqGuAGgP7n475kMPTi58y2EqjAPTh", ip);
			/*
			 * 
			 * {  
				    address: "CN|北京|北京|None|CHINANET|1|None",    #详细地址信息  
				    content:    #结构信息  
				    {  
				        address: "北京市",    #简要地址信息  
				        address_detail:    #结构化地址信息  
				        {  
				            city: "北京市",    #城市  
				            city_code: 131,    #百度城市代码  
				            district: "",    #区县  
				            province: "北京市",    #省份  
				            street: "",    #街道  
				            street_number: ""    #门牌号  
				        },  
				        point:    #当前城市中心点  
				        {  
				            x: "116.39564504",    #当前城市中心点经度
				            y: "39.92998578"    #当前城市中心点纬度
				        }  
				    },  
				    status: 0    #结果状态返回码  
				}
			 * 
			 */
	        JSONObject json = HttpConnectionUtils.httpRequestWithGet(url, new JSONResponseHandler());
	        if(json.getInteger("status") != 0) {
	        	throw new IOException(json.getString("message"));
	        }
			System.out.println(json.getString("address"));
			 
			/*System.out.println(strAd);
			System.out.println(decodeUnicode(strAd));
		
			return new String[] { x, y };*/
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	 /** 
	  * unicode 字节码转换成 中文 
	  * 
	  * @author 陈升平 2017-2-15 
	  * @param ascii 
	  * @return 
	  */  
	public static String decodeUnicode(String ascii) {  
		char aChar;  
		int len = ascii.length();  
		StringBuffer outBuffer = new StringBuffer(len);  
		for (int x = 0; x < len;) {  
			aChar = ascii.charAt(x++);  
			if (aChar == '\\') {  
			    aChar = ascii.charAt(x++);  
			    if (aChar == 'u') {  
					int value = 0;  
					for (int i = 0; i < 4; i++) {  
						aChar = ascii.charAt(x++);  
						switch (aChar) {  
							case '0':  
							case '1':  
							case '2':  
							case '3':  
							case '4':  
							case '5':  
							case '6':  
							case '7':  
							case '8':  
							case '9':  
							value = (value << 4) + aChar - '0';  
							break;  
							case 'a':  
							case 'b':  
							case 'c':  
							case 'd':  
							case 'e':  
							case 'f':  
							value = (value << 4) + 10 + aChar - 'a';  
							break;  
							case 'A':  
							case 'B':  
							case 'C':  
							case 'D':  
							case 'E':  
							case 'F':  
							value = (value << 4) + 10 + aChar - 'A';  
							break;  
							default:  
							throw new IllegalArgumentException("Malformed encoding.");  
						}  
					}  
					outBuffer.append((char) value);  
			    } else {  
			    	if (aChar == 't') {  
			    		aChar = '\t';  
			    	} else if (aChar == 'r') {  
			    		aChar = '\r';  
			    	} else if (aChar == 'n') {  
			    		aChar = '\n';  
			    	} else if (aChar == 'f') {  
			    		aChar = '\f';  
			    	}  
			    	outBuffer.append(aChar);  
			    }  
			} else {  
				outBuffer.append(aChar);  
			}  
		}  
		return outBuffer.toString();  
	}  
}
