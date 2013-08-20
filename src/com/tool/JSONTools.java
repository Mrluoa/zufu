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
	 * @description 根据流返回JSON
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-7-22 下午4:02:29
	 * @param inputStream
	 * @return
	 * @return List<HashMap<String,Object>>
	 * @throws
	 */
	public static List<HashMap<String, Object>> getListData(String inputStream) {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		String values = inputStream;
		try {
			// 放入解析数据
			JSONObject jsonObject = new JSONObject(values);
			// 解析成数组
			JSONArray jsonArray = jsonObject.getJSONArray("zufu");
			// 统计总数
			int count = jsonArray.length();
			for (int i = 0; i < count; i++) {
				// 获取一组数据
				JSONObject jsonObject2 = jsonArray.getJSONObject(i);
				// 创建map
				HashMap<String, Object> map = new HashMap<String, Object>();
				// 获取所有key
				@SuppressWarnings("unchecked")
				Iterator<String> iterator = jsonObject2.keys();
				while (iterator.hasNext()) {
					// 获取一个key
					String key = iterator.next();
					// 根据获取的key获取值
					Object value = jsonObject2.get(key);

					if (value.equals(null)) {
						value = "";
					}
					// 写入map中
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
