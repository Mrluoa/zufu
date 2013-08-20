package com.tool;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpURL {
	/**
	 * 
	 * @description 根据URL获取数据流
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-7-22 下午3:24:45
	 * @param URL
	 * @return
	 * @return InputStream
	 * @throws
	 */
	public static String getItentInputStram(String URL) {

		// 获取HTTP连接
		HttpClient http = new DefaultHttpClient();
		HttpPost post = new HttpPost(URL);
		HttpResponse response;
		String inputStream = null;
		//设置连接超时时间(单位毫秒) 
	 	HttpConnectionParams.setConnectionTimeout(post.getParams(), 15000);
	 	//设置读数据超时时间(单位毫秒)    	
        HttpConnectionParams.setSoTimeout(post.getParams(), 10000);
		try {
			response = http.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				inputStream = EntityUtils.toString(response.getEntity(),
						"utf-8");
			} else {
				System.out.println("----list");
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return inputStream;
	}
	/**
	 * 
	 * @description 返回文件流
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-8-14 上午8:25:56
	 * @param URL
	 * @return
	 * @return String
	 * @throws
	 */
	public static InputStream getItentInputS(String URL) {

		// 获取HTTP连接
		HttpClient http = new DefaultHttpClient();
		HttpPost post = new HttpPost(URL);
		HttpResponse response;
		InputStream inputStream = null;
		//设置连接超时时间(单位毫秒) 
	 	HttpConnectionParams.setConnectionTimeout(post.getParams(), 15000);
	 	//设置读数据超时时间(单位毫秒)    	
        HttpConnectionParams.setSoTimeout(post.getParams(), 10000);
		try {
			response = http.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				inputStream = response.getEntity().getContent();
			} else {
				System.out.println("----list");
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return inputStream;
	}
	/**
	 * 
	 * @description 提交祝福信息
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-7-26 上午10:22:14
	 * @param URL
	 * @param values
	 * @return
	 * @return boolean
	 * @throws
	 */
	public static String PostBlessing(String URL, Map<String, String> values) {
		// 获取HTTP连接
		HttpClient http = new DefaultHttpClient();
		HttpPost post = new HttpPost(URL);
		// 如果传递参数个数比较多的话可以对传递的参数进行封装
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (String key : values.keySet()) {
			// 封装请求参数
			params.add(new BasicNameValuePair(key, values.get(key)));
		}
		try {
			// 设置请求参数
			post.setEntity((new UrlEncodedFormEntity(params, HTTP.UTF_8)));
			// 发送POST请求
			HttpResponse httpResponse = http.execute(post);
			System.out.println("=== 提交的代码"+httpResponse.getStatusLine().getStatusCode());
			// 如果服务器成功地返回响应
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// 获取服务器响应字符串
				 String result = EntityUtils.toString(httpResponse.getEntity(),HTTP.UTF_8);
				//System.out.println("==============返回结果"+result);
				return result;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;

	}
	
}