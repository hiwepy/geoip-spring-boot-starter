package com.github.vindell.geoip.spring.boot;

import java.io.BufferedReader;  
import java.io.DataOutputStream;  
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.io.UnsupportedEncodingException;  
import java.net.HttpURLConnection;  
import java.net.URL;  

/** 
 *  根据IP地址获取详细的地域信息 
 *  @project: personGocheck 
 *  @class: AddressUtils.java 
 *  @author: CHENSP
 *  @date：Nov 14, 2017 6:38:25 PM 
 *  
 *  
 互联网有很多接口可以实现通过ip查询到具体的位置，如下：
通过淘宝IP地址库获取IP位置
1. 请求接口（GET）：http://ip.taobao.com/service/getIpInfo.php?ip=[ip地址字串]
2. 响应信息：（json格式的）国家 、省（自治区或直辖市）、市（县）、运营商
3. 返回数据格式：
{"code":0,"data":{"ip":"210.75.225.254","country":"\u4e2d\u56fd","area":"\u534e\u5317",
"region":"\u5317\u4eac\u5e02","city":"\u5317\u4eac\u5e02","county":"","isp":"\u7535\u4fe1",
"country_id":"86","area_id":"100000","region_id":"110000","city_id":"110000",
"county_id":"-1","isp_id":"100017"}}
其中code的值的含义为，0：成功，1：失败。 


新浪的接口 ：http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=218.192.3.42
返回值
var remote_ip_info = {"ret":1,"start":"218.192.0.0","end":"218.192.7.255","country":"\u4e2d\u56fd",
"province":"\u5e7f\u4e1c","city":"\u5e7f\u5dde","district":"","isp":"\u6559\u80b2\u7f51","type":"\u5b66\u6821",
"desc":"\u5e7f\u5dde\u5927\u5b66\u7eba\u7ec7\u670d\u88c5\u5b66\u9662"};
通过jqry 获取相应的数据
$.getScript('数据接口',function(){
//新浪：remote_ip_info.country
}) 


腾讯IP分享计划的地址获取IP所在地：
http://ip.qq.com/cgi-bin/searchip?searchip1=ip

用Java调用淘宝ip查询接口查询地域的一个java实例：
 */ 
public class GetTaobaoAddressByIP {
	/** 
	  * @param content 
	  *            请求的参数 格式为：name=xxx&pwd=xxx 
	  * @param encoding 
	  *            服务器端请求编码。如GBK,UTF-8等 
	  * @return 
	  * @throws UnsupportedEncodingException 
	  */  
	public String getAddresses(String content, String encodingString) throws UnsupportedEncodingException {  
		// 这里调用pconline的接口  
		String urlStr = "http://ip.taobao.com/service/getIpInfo.php";  
		// 从http://whois.pconline.com.cn取得IP所在的省市区信息  
		String returnStr = this.getResult(urlStr, content, encodingString);  
		if (returnStr != null) {
			// 处理返回的省市区信息  
			System.out.println(returnStr);  
			String[] temp = returnStr.split(",");  
			if(temp.length<3){  
				return "0";//无效IP，局域网测试  
			}  
			String region = (temp[5].split(":"))[1].replaceAll("\"", "");  
			region = decodeUnicode(region);// 省份  
		    
			String country = "";  
			String area = "";  
			// String region = "";  
			String city = "";  
			String county = "";  
			String isp = "";  
			for (int i = 0; i < temp.length; i++) { 
			    switch (i) {  
			    	case 1:  
				        country = (temp[i].split(":"))[2].replaceAll("\"", "");  
				        country = decodeUnicode(country);// 国家  
			        break;  
			        case 3:  
			            area = (temp[i].split(":"))[1].replaceAll("\"", "");  
			            area = decodeUnicode(area);// 地区   
			        break;  
			        case 5:  
			            region = (temp[i].split(":"))[1].replaceAll("\"", "");  
			            region = decodeUnicode(region);// 省份   
			        break;   
			        case 7:  
			            city = (temp[i].split(":"))[1].replaceAll("\"", "");  
			            city = decodeUnicode(city);// 市区  
			        break;   
			        case 9:  
			                county = (temp[i].split(":"))[1].replaceAll("\"", "");  
			                county = decodeUnicode(county);// 地区   
			        break;  
			        case 11:  
			            isp = (temp[i].split(":"))[1].replaceAll("\"", "");  
			            isp = decodeUnicode(isp); // ISP公司  
			        break;  
			    }  
			}  
			System.out.println(country+"="+area+"="+region+"="+city+"="+county+"="+isp);  
			return region;  
		}  
		return null;  
	}  
	 
	 /** 
	  * @param urlStr 
	  *            请求的地址 
	  * @param content 
	  *            请求的参数 格式为：name=xxx&pwd=xxx 
	  * @param encoding 
	  *            服务器端请求编码。如GBK,UTF-8等 
	  * @return 
	  */  
	private String getResult(String urlStr, String content, String encoding) {  
		URL url = null;  
		HttpURLConnection connection = null;  
		try {  
			url = new URL(urlStr);  
			connection = (HttpURLConnection) url.openConnection();// 新建连接实例  
			connection.setConnectTimeout(2000);// 设置连接超时时间，单位毫秒  
			connection.setReadTimeout(2000);// 设置读取数据超时时间，单位毫秒  
			connection.setDoOutput(true);// 是否打开输出流 true|false  
			connection.setDoInput(true);// 是否打开输入流true|false  
			connection.setRequestMethod("POST");// 提交方法POST|GET  
			connection.setUseCaches(false);// 是否缓存true|false  
			connection.connect();// 打开连接端口  
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());// 打开输出流往对端服务器写数据  
			out.writeBytes(content);// 写数据,也就是提交你的表单 name=xxx&pwd=xxx  
			out.flush();// 刷新  
			out.close();// 关闭输出流  
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));// 往对端写完数据对端服务器返回数据  
			// ,以BufferedReader流来读取  
			StringBuffer buffer = new StringBuffer();  
			String line = "";  
			while ((line = reader.readLine()) != null) {  
				buffer.append(line);  
			}  
			reader.close();  
			return buffer.toString();  
		} catch (IOException e) {  
			e.printStackTrace();  
		} finally {  
			if (connection != null) {  
				connection.disconnect();// 关闭连接  
			}  
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
	 
	/**
	 *  测试  
	 * @param args
	 */
	public static void main(String[] args) {  
		GetTaobaoAddressByIP addressUtils = new GetTaobaoAddressByIP();  
		// 测试ip 219.136.134.157 中国=华南=广东省=广州市=越秀区=电信  
		String ip = "115.159.94.190";	// "125.70.11.136";  
		String address = "";  
		try {  
			address = addressUtils.getAddresses("ip="+ip, "utf-8");  
		} catch (UnsupportedEncodingException e) {  
			// TODO Auto-generated catch block  
			e.printStackTrace();  
		}  
		System.out.println(address);  
		// 输出结果为：广东省,广州市,越秀区  
	}  
}
