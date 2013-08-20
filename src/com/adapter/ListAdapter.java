package com.adapter;

import java.util.HashMap;
import java.util.List;

import com.example.zhufu.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {

	List<HashMap<String, Object>> list = null;
	private LayoutInflater layou = null;

	public ListAdapter(Context context) {
		layou = LayoutInflater.from(context);
	}

	public void setList(List<HashMap<String, Object>> list) {
		this.list = list;
	}
	
	public List<HashMap<String, Object>> getList(){
		return this.list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layou.inflate(R.layout.list_item, null);

			viewHolder.show_info = (TextView) convertView
					.findViewById(R.id.show_title);
			viewHolder.show_send = (TextView) convertView
					.findViewById(R.id.show_about);
			viewHolder.show_date = (TextView) convertView
					.findViewById(R.id.show_date);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.show_info.setText(list.get(arg0).get("info").toString());
		viewHolder.show_send.setText(list.get(arg0).get("send").toString());
		viewHolder.show_date.setText(list.get(arg0).get("postdate").toString());
		viewHolder.show_pick = list.get(arg0).get("pick").toString();
		viewHolder.show_URL = list.get(arg0).get("url").toString();
		viewHolder.show_Face = list.get(arg0).get("face").toString();
		return convertView;
	}

}
