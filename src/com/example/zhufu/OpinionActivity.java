package com.example.zhufu;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.tool.GMailSender;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
/**
 * 
 * @description 反馈UI
 * @version 1.0
 * @author Mrluo
 * @date 2013-8-17 上午11:18:40
 */
public class OpinionActivity extends Activity {
	private Spinner select = null;
	private String[] select_value = { "界面意见", "操作意见", "流量问题", "其他" };
	private EditText option_th, option_two = null;
	private ArrayAdapter<String> myAdapter = null;
	private Handler handler = null;
	private ProgressDialog dialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_opinion);

		select = (Spinner) findViewById(R.id.opinion_selet);
		option_two = (EditText) findViewById(R.id.opinion_centet);
		option_th = (EditText) findViewById(R.id.option_conten1);
		myAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, select_value);
		// 风格
		myAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 绑定适配器
		select.setAdapter(myAdapter);
		/**
		 * 接受网络提交异步处理
		 */
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				System.out.println("接受返回信息===>"+msg.what);
				switch (msg.what) {
				case 1:
					Toast.makeText(getBaseContext(), "发送成功！", Toast.LENGTH_LONG)
							.show();
					option_two.setText("");
					option_th.setText("");
					option_two.setFocusable(true);
					dialog.dismiss();
					break;

				default:
					Toast.makeText(getBaseContext(), "发送失败",
							Toast.LENGTH_LONG).show();
					dialog.dismiss();
					break;
				}

				super.handleMessage(msg);
			}
		};

		dialog = new ProgressDialog(OpinionActivity.this);

		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("正在提交反馈...");
		dialog.setCancelable(true);

	}

	/**
	 * 
	 * @description 单击事件
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-8-14 下午3:39:16
	 * @param view
	 * @return void
	 * @throws
	 */
	public void BttonClick(View view) {
		switch (view.getId()) {
		case R.id.opinion_submit:
			// 显示正在提交提示框
			dialog.show();
			String sele = select.getSelectedItem().toString();
			String two = option_two.getText().toString().trim();
			String senders = option_th.getText().toString().trim();
			String theme = "来自 " + senders + " 的意见";
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String Values = "来自  " + senders + " 的 " + sele + " :" + two + "。\n"
					+ dateFormat.format(new Date());
			
			new startMail(theme, Values, "139811679@qq.com", senders).start();
			
			break;

		default:
			break;
		}
	}

	/**
	 * 
	 * @description 邮箱发送方法
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-8-14 下午4:59:44
	 * @param theme
	 *            邮件主题
	 * @param text
	 *            邮件内容
	 * @param addressee
	 *            收件人邮箱
	 * @param senders
	 *            发件人
	 * @return int
	 * @throws
	 */
	private int startStmp(String theme, String text, String addressee,
			String senders) {
		// 发送Email
		try {
			// 发送方的邮箱名及密码。
			GMailSender sender = new GMailSender("mrluoa@gmail.com",
					"luoying.2583676");
			sender.sendMail(theme, // 主题
					text, // 正文
					"mrluoa@gmail.com", // 发送人
					addressee); // 收件人,不一定非得gmail其他邮箱比如163，qq邮箱都行。
			return 1;

		} catch (Exception e) {
			Log.e("SendMail", e.getMessage(), e);
			return 0;

		}
	}

	/**
	 * 
	 * @description 另开线程提交数据
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-8-14 下午5:09:14
	 */
	private class startMail extends Thread {
		String theme, text, addressee, senders;

		public startMail(String theme, String text, String addressee,
				String senders) {
			this.theme = theme;
			this.text = text;
			this.addressee = addressee;
			this.senders = senders;
		}

		@Override
		public void run() {
			Message ms = handler.obtainMessage(startStmp(theme, text, addressee, senders));
			handler.sendMessage(ms);
		}
	}

}
