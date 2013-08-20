package com.example.zhufu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.ListAdapter;
import com.adapter.ViewHolder;
import com.domain.Blessing;
import com.tool.CommonURL;
import com.tool.DropDownListView;
import com.tool.DropDownListView.OnDropDownListener;
import com.tool.ExitApplication;
import com.tool.HttpURL;
import com.tool.JSONTools;

/**
 * 
 * @description 主页UI
 * @version 1.0
 * @author Mrluo
 * @date 2013-8-11 上午9:06:28
 */
public class HomeActivity extends Activity {
	private com.tool.DropDownListView listview;
	private RelativeLayout read_lodding = null;
	private int count = 1;

	private Button home_reset;

	private ListAdapter myadapter;

	private ImageView imageanmi = null;

	private int number = 12;// 每次获取多少条数据
	private int maxpage;// 总共有多少页
	private int row;// 总共数据多少
	// 全局的资源list
	private List<HashMap<String, Object>> asylist = null;

	// 正在加载字体
	private TextView textView1 = null;

	// 顶部刷新
	private boolean isDrow = false;
	// 第一次运行
	private boolean isOneRun = true;
	private boolean loadfinish = false;
	// 底部加载中
	private boolean isScrol = false;

	private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		// 添加当前activity
		ExitApplication.getInstance().addActivity(this);
		// 初始化
		myadapter = new ListAdapter(this);
		listview = (DropDownListView) findViewById(R.id.home_listview);

		home_reset = (Button) findViewById(R.id.home_reset);
		read_lodding = (RelativeLayout) findViewById(R.id.read_lodding);
		imageanmi = (ImageView) findViewById(R.id.imageview);

		textView1 = (TextView) findViewById(R.id.textView1);

		// 在Activity中播放动画
		handler.postDelayed(new Runnable() {
			public void run() {
				AnimationDrawable frameAnimation = (AnimationDrawable) imageanmi
						.getDrawable();
				frameAnimation.start();
			}
		}, 50);

		// 判断网络
		isNetStart();

		// 刷新事件
		home_reset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 加载显示中动画
				read_lodding.setVisibility(View.VISIBLE);
				// 显示listview
				listview.setVisibility(View.GONE);

				imageanmi.setVisibility(View.VISIBLE);
				textView1.setTextColor(Color.GRAY);
				textView1.setText("正在加载中....");

				// 在Activity中播放动画
				handler.postDelayed(new Runnable() {
					public void run() {
						AnimationDrawable frameAnimation = (AnimationDrawable) imageanmi
								.getDrawable();
						frameAnimation.start();
					}
				}, 50);

				isOneRun = true;
				isNetStart();
			}
		});
		// 下拉事件
		listview.setOnDropDownListener(new OnDropDownListener() {

			@Override
			public void onDropDown() {
				System.out.println("事件-->下拉刷新成功！");
				isDrow = true;
				new MallAsyncTask().execute(CommonURL.MALL_TJ_URL);
			}
		});

		// 底部事件
		listview.setOnBottomListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				int loadCount = myadapter.getCount();
				// if (isScrol && loadCount < row) {
				int page = row % myadapter.getCount();
				String sql = null;
				if (loadCount < row && count <= maxpage) {
					System.out.println("当前:" + count + "总数:" + maxpage);
					// 最后页数据需判断于数

					if (count == maxpage) {
						System.out.println("最后一页咯哦================= 执行！");
						sql = CommonURL.MALL_PAGE_URL + "&offone="
								+ (myadapter.getCount()) + "&offtwo=" + page;
					} else {
						sql = CommonURL.MALL_PAGE_URL + "&offone="
								+ (myadapter.getCount() + 1) + "&offtwo="
								+ number;
					}
					loadfinish = true;
					System.out.println("==================== " + sql);
					isScrol = true;
					new MallAsyncTask().execute(sql);
				} else {
					loadfinish = false;
					isScrol = false;
					listview.setHasMore(false);
					// 要setHashMaor生效必须执行onBottomComplete
					listview.onBottomComplete();

				}
			}
		});
		// 单机事件
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				ViewHolder viewHolder = (ViewHolder) view.getTag();
				Blessing bl = new Blessing();
				bl.setInfo(viewHolder.show_info.getText().toString());
				bl.setPick(viewHolder.show_pick.toString());
				bl.setSend(viewHolder.show_send.getText().toString());
				bl.setUrl(viewHolder.show_URL.toString());
				bl.setDate(viewHolder.show_date.getText().toString());
				bl.setFace(viewHolder.show_Face.toString());
				Intent intent = new Intent(HomeActivity.this,
						MiddleActivity.class);
				Bundle bu = new Bundle();
				System.out.println(bl.toString());
				bu.putSerializable("bless", bl);
				intent.putExtras(bu);
				startActivity(intent);
			}
		});
	}

	/**
	 * 
	 * @description 异步下载数据类
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-7-22 下午11:08:17
	 */
	private class MallAsyncTask extends
			AsyncTask<String, Void, List<HashMap<String, Object>>> {
		@Override
		protected List<HashMap<String, Object>> doInBackground(String... params) {

			String inputStream = HttpURL.getItentInputStram(params[0]);
			if (CommonURL.MALL_SJ_URL.equals(params[0])) {
				row = JSONTools.getListData(inputStream).size();
				System.out
						.println("=========================  count -> " + row);
				maxpage = row / number;
				System.out.println("===================  page " + maxpage);
			}
			return JSONTools.getListData(inputStream);
		}

		@Override
		protected void onPostExecute(List<HashMap<String, Object>> result) {
			// 当结果集大于0执行
			if (result.size() > 0) {
				System.out.println("完成恩！");
				// 第一次加载
				if (isOneRun) {
					asylist = new ArrayList<HashMap<String, Object>>();
					asylist.addAll(result);
					// 绑定适配器中的数据
					myadapter.setList(asylist);
					// 绑定适配器
					listview.setAdapter(myadapter);
					// 隐藏加载中动画
					read_lodding.setVisibility(View.GONE);
					// 显示listview
					listview.setVisibility(View.VISIBLE);
					// 关闭动画
					AnimationDrawable frameAnimation = (AnimationDrawable) imageanmi
							.getDrawable();
					frameAnimation.stop();
					System.out.println("第一次执行");
					// 设置第一次运行结果为false
					isOneRun = false;
					loadfinish = true;
				}
				// 当下拉刷新为真是执行
				if (isDrow) {
					// 初始化list未最新的以免,划到底部有重复数据
					asylist = new ArrayList<HashMap<String, Object>>();

					asylist.addAll(result);

					// 绑定适配器中的数据
					myadapter.setList(asylist);
					// 刷新适配器
					myadapter.notifyDataSetChanged();

					System.out.println("顶部刷新");
					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"MM-dd HH:mm:ss");

					isDrow = false;
					loadfinish = false;
					count = 1;
					// 设置没有加载为没有就爱在
					listview.setHasMore(true);
					// 要setHashMaor生效必须执行onBottomComplete
					listview.onBottomComplete();

					// 关闭顶部刷新
					listview.onDropDownComplete(dateFormat.format(new Date()));
					System.out.println("=======>关闭刷新成功！");
				}
				// 获取适配器中数据的条数
				int loadCount = myadapter.getCount();
				// 如果是拉入底部刷新，并且总数小于当前条数，并且只运行一次
				if (isScrol && loadCount < row && loadfinish) {
					loadfinish = false;
					System.out.println("底部刷新");
					// 添加数据
					asylist.addAll(result);
					// myadapter.setList(asylist);
					// 刷新适配器
					myadapter.notifyDataSetChanged();
					System.out.println(myadapter.getCount());
					// 关闭底部加载
					listview.onBottomComplete();
					// 当前页数
					count++;
				} else {
					listview.onBottomComplete();
				}

			}
			super.onPostExecute(result);
		}
	};

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
		} else {
			listview.setEnabled(false);
			System.out.println("无数据");
		}
		return false;
	}

	/**
	 * 将Listview数据放入xml中
	 * 
	 * @description
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-8-11 上午9:09:57
	 * @return
	 * @return boolean
	 * @throws
	 */
	private void TextSaveXml() {
		List<HashMap<String, Object>> savelist = myadapter.getList();
		//实例化SharedPreferences对象（第一步）
		SharedPreferences mySharedPreferences = getSharedPreferences("zufu",
				Activity.MODE_PRIVATE);
		// 实例化SharedPreferences.Editor对象（第二步）
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		
		int count = savelist.size();
		for(int i = 0;i < count;i++){
			// 用putString的方法保存数据
			editor.putString("info", savelist.get(i).get("info").toString());
			editor.putString("send", savelist.get(i).get("send").toString());
			editor.putString("postdate", savelist.get(i).get("postdate").toString());
			editor.putString("pick", savelist.get(i).get("pick").toString());
			editor.putString("url", savelist.get(i).get("url").toString());
			editor.putString("face", savelist.get(i).get("face").toString());
		}
		// 提交当前数据
		editor.commit();
		// 使用LogCat信息提示框提示成功写入数据
		Log.i("保存xml==>", "将数据保存在xml中");
	}

	/**
	 * 监听返回--是否退出程序
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean flag = true;
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			//TextSaveXml();
			// 是否退出应用
			MainActivity.Exit(HomeActivity.this);
			
		}
		return flag;
	}

	private void isNetStart() {
		// 重欢迎界面返回的网络判断网络是否连接
		if (isNetworkConnected(HomeActivity.this)) {

			new MallAsyncTask().execute(CommonURL.MALL_TJ_URL);
			new MallAsyncTask().execute(CommonURL.MALL_SJ_URL);

		} else {
			System.out.println("无网络状态！");
			// 隐藏加载中动画
			// read_lodding.setVisibility(View.GONE);
			imageanmi.setVisibility(View.GONE);
			textView1.setText(Html.fromHtml("<u>无网络,单击设置网络！</u>"));
			textView1.setTextColor(Color.BLUE);
			textView1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					AlertDialog.Builder builder = new Builder(HomeActivity.this);
					builder.setTitle("设置网络");
					builder.setMessage("网络错误请设置网络");
					builder.setPositiveButton("设置网络",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									Intent intent = new Intent();
									intent.setClassName("com.android.settings",
											"com.android.settings.WirelessSettings");
									startActivity(intent);
								}
							});
					builder.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub

								}
							});
					builder.create().show();
				}
			});
		}
	}
}
