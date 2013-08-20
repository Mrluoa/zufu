package com.example.zhufu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.adapter.ExpandeAdapter;
import com.domain.Blessing;
import com.tool.CommonURL;
import com.tool.HttpURL;
import com.tool.JSONTools;

public class CatActivity extends Activity {
	private ExpandableListView expandableListView = null;
	private int type = 0;
	private List<HashMap<String, Object>> SH_DATA, YQ_DATA, AQ_DATA,
			QY_DATA = null;
	private ExpandeAdapter expandeAdapter = null;
	private String[] grop = { "生日祝福", "友情深重", "爱情表白", "歉意浓浓" };
	private Button cat_reset;
	ProgressDialog progressDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cat);

		expandableListView = (ExpandableListView) findViewById(R.id.cat_one);
		cat_reset = (Button) findViewById(R.id.cat_reset);
		expandableListView.setGroupIndicator(null);
		System.out.println("分类查看");
		expandeAdapter = new ExpandeAdapter(this, grop);

		SH_DATA = new ArrayList<HashMap<String, Object>>();
		YQ_DATA = new ArrayList<HashMap<String, Object>>();
		AQ_DATA = new ArrayList<HashMap<String, Object>>();
		QY_DATA = new ArrayList<HashMap<String, Object>>();

		progressDialog = new ProgressDialog(this);

		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage("正在加载数据...");
		progressDialog.setCancelable(true);

		// 当网络可用时调用
		if (isNetworkConnected(this)) {
			// 加载网络数据
			new CatAsyTask().execute(CommonURL.MALL_SJ_URL);
		}
		// 刷新事件
		cat_reset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SH_DATA = new ArrayList<HashMap<String, Object>>();
				YQ_DATA = new ArrayList<HashMap<String, Object>>();
				AQ_DATA = new ArrayList<HashMap<String, Object>>();
				QY_DATA = new ArrayList<HashMap<String, Object>>();
				new CatAsyTask().execute(CommonURL.MALL_SJ_URL);

			}
		});
		// 单击item事件
		expandableListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1,
					int groupPosition, int childPosition, long arg4) {
				// TODO Auto-generated method stub
				HashMap<String, Object> map = null;
				switch (groupPosition) {
				case 0:
					map = SH_DATA.get(childPosition);
					break;
				case 1:
					map = YQ_DATA.get(childPosition);
					break;
				case 2:
					map = AQ_DATA.get(childPosition);
					break;
				case 3:
					map = QY_DATA.get(childPosition);
					break;
				}

				Blessing bl = new Blessing();
				bl.setInfo(map.get("info").toString());
				bl.setPick(map.get("pick").toString());
				bl.setSend(map.get("send").toString());
				bl.setUrl(map.get("url").toString());
				bl.setDate(map.get("postdate").toString());
				bl.setFace(map.get("face").toString());

				Intent intent = new Intent(CatActivity.this,
						MiddleActivity.class);
				Bundle bu = new Bundle();
				bu.putSerializable("bless", bl);
				intent.putExtras(bu);
				startActivity(intent);
				return false;
			}
		});
	}

	/**
	 * 
	 * @description 异步任务类
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-7-27 下午4:59:38
	 */
	private class CatAsyTask extends
			AsyncTask<String, Void, List<HashMap<String, Object>>> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			progressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected List<HashMap<String, Object>> doInBackground(String... params) {
			String inputStream = HttpURL.getItentInputStram(params[0]);
			return JSONTools.getListData(inputStream);
		}

		@Override
		protected void onPostExecute(List<HashMap<String, Object>> result) {

			int count = result.size();
			if (count > 0) {
				System.out.println(count);
				for (int i = 0; i < count; i++) {
					type = Integer.parseInt(result.get(i).get("face")
							.toString());
					System.out.println("当前第 " + i + " 条 ==> "
							+ result.get(i).toString());
					System.out.println("当前是 =======>" + type);
					switch (type) {
					case 0:
						SH_DATA.add(result.get(i));
						System.out.println("获取到生日的数据==========>"
								+ SH_DATA.get(0).get("info"));
						break;
					case 1:
						YQ_DATA.add(result.get(i));
						System.out.println("获取到友谊的数据==========>"
								+ YQ_DATA.get(0).get("info"));
						break;
					case 2:
						AQ_DATA.add(result.get(i));
						System.out.println("获取到爱情的数据==========>"
								+ AQ_DATA.get(0).get("info"));
						break;
					case 3:
						QY_DATA.add(result.get(i));
						System.out.println("获取到歉意的数据==========>"
								+ QY_DATA.get(0).get("info"));
						break;
					}
				}
				expandeAdapter.setDate(SH_DATA, YQ_DATA, AQ_DATA, QY_DATA);
				expandableListView.setAdapter(expandeAdapter);
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
			}
			super.onPostExecute(result);
		}

	}

	/**
	 * 
	 * @description 判断网络是否可用
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-7-24 下午3:56:47
	 * @param context
	 * @return
	 * @return boolean
	 * @throws
	 */
	public boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	/**
	 * 监听返回--是否退出程序
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean flag = true;
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 是否退出应用
			MainActivity.Exit(CatActivity.this);
		}
		return flag;
	}
}
