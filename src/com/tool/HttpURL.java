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
	 * @description ����URL��ȡ������
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-7-22 ����3:24:45
	 * @param URL
	 * @return
	 * @return InputStream
	 * @throws
	 */
	public static String getItentInputStram(String URL) {

		// ��ȡHTTP����
		HttpClient http = new DefaultHttpClient();
		HttpPost post = new HttpPost(URL);
		HttpResponse response;
		String inputStream = null;
		//�������ӳ�ʱʱ��(��λ����) 
	 	HttpConnectionParams.setConnectionTimeout(post.getParams(), 15000);
	 	//���ö����ݳ�ʱʱ��(��λ����)    	
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
	 * @description �����ļ���
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-8-14 ����8:25:56
	 * @param URL
	 * @return
	 * @return String
	 * @throws
	 */
	public static InputStream getItentInputS(String URL) {

		// ��ȡHTTP����
		HttpClient http = new DefaultHttpClient();
		HttpPost post = new HttpPost(URL);
		HttpResponse response;
		InputStream inputStream = null;
		//�������ӳ�ʱʱ��(��λ����) 
	 	HttpConnectionParams.setConnectionTimeout(post.getParams(), 15000);
	 	//���ö����ݳ�ʱʱ��(��λ����)    	
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
	 * @description �ύף����Ϣ
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-7-26 ����10:22:14
	 * @param URL
	 * @param values
	 * @return
	 * @return boolean
	 * @throws
	 */
	public static String PostBlessing(String URL, Map<String, String> values) {
		// ��ȡHTTP����
		HttpClient http = new DefaultHttpClient();
		HttpPost post = new HttpPost(URL);
		// ������ݲ��������Ƚ϶�Ļ����ԶԴ��ݵĲ������з�װ
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (String key : values.keySet()) {
			// ��װ�������
			params.add(new BasicNameValuePair(key, values.get(key)));
		}
		try {
			// �����������
			post.setEntity((new UrlEncodedFormEntity(params, HTTP.UTF_8)));
			// ����POST����
			HttpResponse httpResponse = http.execute(post);
			System.out.println("=== �ύ�Ĵ���"+httpResponse.getStatusLine().getStatusCode());
			// ����������ɹ��ط�����Ӧ
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// ��ȡ��������Ӧ�ַ���
				 String result = EntityUtils.toString(httpResponse.getEntity(),HTTP.UTF_8);
				//System.out.println("==============���ؽ��"+result);
				return result;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;

	}
	
}