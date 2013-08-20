package com.adapter;

import java.util.HashMap;
import java.util.List;

import com.example.zhufu.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandeAdapter extends BaseExpandableListAdapter {
	// 上下文
	private Context context;
	// 加载额外样式
	private LayoutInflater Inflater = null;
	// 分组名
	private String[] GroupStrings = null;
	// 数据源
	private List<HashMap<String, Object>> SH_DATA, YQ_DATA, AQ_DATA,
			QY_DATA = null;

	/**
	 * 
	 * 类的构造方法
	 * 
	 * @param context
	 *            上下文
	 * @param groupStrings
	 *            分组标题
	 */
	public ExpandeAdapter(Context context, String[] groupStrings) {
		this.context = context;
		this.GroupStrings = groupStrings;

		Inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * 
	 * @description 绑定数据
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-7-27 下午1:47:23
	 * @param SH_DATA
	 *            生日祝福
	 * @param YQ_DATA
	 *            友情深重
	 * @param AQ_DATA
	 *            爱情表白
	 * @param QY_DATA
	 *            歉意浓浓
	 * @return void
	 * @throws
	 */
	public void setDate(List<HashMap<String, Object>> SH_DATA,
			List<HashMap<String, Object>> YQ_DATA,
			List<HashMap<String, Object>> AQ_DATA,
			List<HashMap<String, Object>> QY_DATA) {
		this.SH_DATA = SH_DATA;
		this.YQ_DATA = YQ_DATA;
		this.AQ_DATA = AQ_DATA;
		this.QY_DATA = QY_DATA;
	}

	// 返回一行数据
	@Override
	public Object getChild(int arg0, int arg1) {

		switch (arg0) {
		case 0:
			return SH_DATA.get(arg1);
		case 1:
			return YQ_DATA.get(arg1);
		case 2:
			return AQ_DATA.get(arg1);
		case 3:
			return QY_DATA.get(arg1);
		}
		return null;
	}

	// 返回分组下的子项id
	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return arg1;
	}

	// 返回某个分组的内容的多少
	@Override
	public int getChildrenCount(int arg0) {
		switch (arg0) {
		case 0:
			return SH_DATA.size();
		case 1:
			return YQ_DATA.size();
		case 2:
			return AQ_DATA.size();
		case 3:
			return QY_DATA.size();
		}
		return 0;
	}

	// 获取分钟名称
	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return GroupStrings[arg0];
	}

	// 获取分组的多少
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return GroupStrings.length;
	}

	// 获取分组的id
	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	// 设置可以单击打开分组
	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		/* 很重要：实现ChildView点击事件，必须返回true */
		return true;
	}

	// 设置分组组名的属性
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GropTitle gropTitle = null;
		if (convertView == null) {
			//初始化
			gropTitle = new GropTitle();
			//获取资源试图
			convertView = Inflater.inflate(R.layout.grop_title, null);
			//获取id
			gropTitle.image = (ImageView) convertView
					.findViewById(R.id.grop_left);
			gropTitle.conter = (TextView) convertView
					.findViewById(R.id.grop_center);
			gropTitle.count = (TextView) convertView
					.findViewById(R.id.grop_cright);

			convertView.setTag(gropTitle);
		} else {
			gropTitle = (GropTitle) convertView.getTag();
		}
		int counts = 0;
		switch (groupPosition) {
		case 0:
			counts = getChildrenCount(groupPosition);
			break;
		case 1:
			counts = getChildrenCount(groupPosition);
			break;
		case 2:
			counts = getChildrenCount(groupPosition);
			break;
		case 3:
			counts = getChildrenCount(groupPosition);
			break;
		}
		gropTitle.count.setText("[ " + counts + " ]");
		gropTitle.conter.setText(getGroup(groupPosition).toString());
		// 是否选中
		if (isExpanded) {
			gropTitle.image.setImageResource(R.drawable.list_close_arrow);
		} else {
			gropTitle.image.setImageResource(R.drawable.lbs_goto_arrow);
		}
		System.out.println(gropTitle.conter.getText());
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		GropItem gropItem = null;
		if (convertView == null) {
			// 初始化
			gropItem = new GropItem();
			// 加载布局
			convertView = Inflater.inflate(R.layout.list_item, null);
			// 获取id绑定
			gropItem.title = (TextView) convertView
					.findViewById(R.id.show_title);
			gropItem.about = (TextView) convertView
					.findViewById(R.id.show_about);
			gropItem.date = (TextView) convertView.findViewById(R.id.show_date);

			convertView.setTag(gropItem);
		} else {
			gropItem = (GropItem) convertView.getTag();
		}

		HashMap<String, Object> map = null;

		// System.out.println(groupPosition+":"+childPosition);
		switch (groupPosition) {
		case 0:
			map = (HashMap<String, Object>) getChild(groupPosition,
					childPosition);
			setText(gropItem, map);
			map = null;
			break;
		case 1:
			map = (HashMap<String, Object>) getChild(groupPosition,
					childPosition);
			setText(gropItem, map);
			map = null;
			break;
		case 2:
			map = (HashMap<String, Object>) getChild(groupPosition,
					childPosition);
			setText(gropItem, map);
			map = null;
			break;
		case 3:
			map = (HashMap<String, Object>) getChild(groupPosition,
					childPosition);
			setText(gropItem, map);
			map = null;
			break;
		}
		return convertView;
	}

	/**
	 * 
	 * @description 设置TextView数据
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-7-27 下午4:14:31
	 * @param gropItem
	 *            封装的数据封装类
	 * @param map
	 *            数据
	 * @return void
	 * @throws
	 */
	private void setText(GropItem gropItem, HashMap<String, Object> map) {
		gropItem.title.setText(map.get("info").toString());
		gropItem.about.setText(map.get("send").toString());
		gropItem.date.setText(map.get("postdate").toString());
	}

	/**
	 * 
	 * @description GropItem内部类-JavaBean
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-7-27 下午4:08:29
	 */
	private class GropItem {
		public TextView title;
		public TextView about;
		public TextView date;
	}

	/**
	 * 
	 * @description GropTitle内部类-JavaBean
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-7-27 下午4:36:27
	 */
	private class GropTitle {
		public ImageView image;
		public TextView conter;
		public TextView count;
	}
}
