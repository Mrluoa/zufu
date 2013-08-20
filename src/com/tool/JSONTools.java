package com.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONTools {
	/**
	 * 
	 * @description ����������JSON
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-7-22 ����4:02:29
	 * @param inputStream
	 * @return
	 * @return List<HashMap<String,Object>>
	 * @throws
	 */
	public static List<HashMap<String, Object>> getListData(String inputStream) {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		String values = inputStream;
		try {
			// �����������
			JSONObject jsonObject = new JSONObject(values);
			// ����������
			JSONArray jsonArray = jsonObject.getJSONArray("zufu");
			// ͳ������
			int count = jsonArray.length();
			for (int i = 0; i < count; i++) {
				// ��ȡһ������
				JSONObject jsonObject2 = jsonArray.getJSONObject(i);
				// ����map
				HashMap<String, Object> map = new HashMap<String, Object>();
				// ��ȡ����key
				@SuppressWarnings("unchecked")
				Iterator<String> iterator = jsonObject2.keys();
				while (iterator.hasNext()) {
					// ��ȡһ��key
					String key = iterator.next();
					// ���ݻ�ȡ��key��ȡֵ
					Object value = jsonObject2.get(key);

					if (value.equals(null)) {
						value = "";
					}
					// д��map��
					map.put(key, value);
					//System.out.println(map.toString());
				}
				list.add(map);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}
