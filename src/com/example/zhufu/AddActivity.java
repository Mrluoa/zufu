package com.example.zhufu;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.domain.Blessing;
import com.tool.CommonURL;
import com.tool.HttpURL;

import android.R.bool;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddActivity extends Activity {
	private EditText pick, tel, send, info;
	private Spinner music;
	private Button submit;
	private ArrayAdapter<String> musicAdapter;
	private String[] value_music = { "生日祝福", "友情祝福", "爱情表白", "歉意浓浓" };

	private String mp3 = "";
	private ProgressDialog dialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);

		pick = (EditText) findViewById(R.id.add_pick);
		tel = (EditText) findViewById(R.id.add_tel);
		send = (EditText) findViewById(R.id.add_send);
		info = (EditText) findViewById(R.id.add_info);
		music = (Spinner) findViewById(R.id.add_music);
		submit = (Button) findViewById(R.id.add_submit);

		dialog = new ProgressDialog(AddActivity.this);
		
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("正在为你传递祝福...");
		dialog.setCancelable(true);

		musicAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, value_music);
		// 风格
		musicAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 绑定适配器
		music.setAdapter(musicAdapter);

		// 选择音乐监听事件
		music.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				mp3 = music.getSelectedItem().toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		// 提交事件
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				final Map<String, String> bl = new HashMap<String, String>();
				bl.put("pick", pick.getText().toString().trim());
				bl.put("tel", tel.getText().toString().trim());
				bl.put("send", send.getText().toString().trim());
				bl.put("info", info.getText().toString().trim());

				int type;
				// 设置MP3链接
				if (mp3.equals(value_music[0])) {
					bl.put("mp3", CommonURL.SHENGRI);
					type = 0;
				} else if (mp3.equals(value_music[1])) {
					bl.put("mp3", CommonURL.YOUQIN);
					type = 1;
				} else if (mp3.equals(value_music[2])) {
					bl.put("mp3", CommonURL.BIAOBAI);
					type = 2;
				} else {
					bl.put("mp3", CommonURL.QIANYI);
					type = 3;
				}
				bl.put("face", String.valueOf(type));
				// 判断是否为空
				if (bl.get("pick").equals("")) {
					Toast.makeText(getApplicationContext(), "接受人未填写！",
							Toast.LENGTH_SHORT).show();
					pick.requestFocus();
				} else if (bl.get("tel").trim().equals("")) {
					Toast.makeText(getApplicationContext(), "手机号未填写！",
							Toast.LENGTH_SHORT).show();
					tel.requestFocus();
				} else if (bl.get("send").equals("")) {
					Toast.makeText(getApplicationContext(), "发送人未填写！",
							Toast.LENGTH_SHORT).show();
					send.requestFocus();
				} else if (bl.get("info").equals("")) {
					Toast.makeText(getApplicationContext(), "字条内容未填写！",
							Toast.LENGTH_SHORT).show();
					info.requestFocus();
				}
				// 不为空执行
				else {
					SimpleDateFormat format = new SimpleDateFormat(
							"最近刷新:"+"yyyy-MM-dd HH:mm:ss");
					Date date = new Date();
					bl.put("date", format.format(date));
					if (isNetworkConnected(getApplicationContext())) {
						
						new PostAdd().execute(bl);
					}
				}

			}
		});

	}

	/**
	 * 
	 * @description 提交数据线程任务类
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-7-26 上午10:06:51
	 */
	private class PostAdd extends AsyncTask<Map<String, String>, Void, String> {
		@Override
		protected void onPreExecute() {
			// 启动加载框

			super.onPreExecute();
			dialog.show();
		}

		@Override
		protected String doInBackground(Map<String, String>... arg0) {
			return HttpURL.PostBlessing(CommonURL.MALL_ADD_URL, arg0[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			System.out.println("================= 提交结果 ===============>"
					+ result);
			// dialog.dismiss();
			String istemp = new String(result);
			
			if (istemp.trim().equals("true")) {
				Toast.makeText(getApplicationContext(), "提交祝福成功！",
						Toast.LENGTH_SHORT).show();
				pick.setText("");
				tel.setText("");
				send.setText("");
				info.setText("");
			} else {
				Toast.makeText(getApplicationContext(), "提交祝福失败！",
						Toast.LENGTH_SHORT).show();
			}
			dialog.dismiss();
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
			MainActivity.Exit(AddActivity.this);
		}
		return flag;
	}
}
