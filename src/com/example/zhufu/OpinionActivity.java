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
 * @description ����UI
 * @version 1.0
 * @author Mrluo
 * @date 2013-8-17 ����11:18:40
 */
public class OpinionActivity extends Activity {
	private Spinner select = null;
	private String[] select_value = { "�������", "�������", "��������", "����" };
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
		// ���
		myAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��������
		select.setAdapter(myAdapter);
		/**
		 * ���������ύ�첽����
		 */
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				System.out.println("���ܷ�����Ϣ===>"+msg.what);
				switch (msg.what) {
				case 1:
					Toast.makeText(getBaseContext(), "���ͳɹ���", Toast.LENGTH_LONG)
							.show();
					option_two.setText("");
					option_th.setText("");
					option_two.setFocusable(true);
					dialog.dismiss();
					break;

				default:
					Toast.makeText(getBaseContext(), "����ʧ��",
							Toast.LENGTH_LONG).show();
					dialog.dismiss();
					break;
				}

				super.handleMessage(msg);
			}
		};

		dialog = new ProgressDialog(OpinionActivity.this);

		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("�����ύ����...");
		dialog.setCancelable(true);

	}

	/**
	 * 
	 * @description �����¼�
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-8-14 ����3:39:16
	 * @param view
	 * @return void
	 * @throws
	 */
	public void BttonClick(View view) {
		switch (view.getId()) {
		case R.id.opinion_submit:
			// ��ʾ�����ύ��ʾ��
			dialog.show();
			String sele = select.getSelectedItem().toString();
			String two = option_two.getText().toString().trim();
			String senders = option_th.getText().toString().trim();
			String theme = "���� " + senders + " �����";
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String Values = "����  " + senders + " �� " + sele + " :" + two + "��\n"
					+ dateFormat.format(new Date());
			
			new startMail(theme, Values, "139811679@qq.com", senders).start();
			
			break;

		default:
			break;
		}
	}

	/**
	 * 
	 * @description ���䷢�ͷ���
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-8-14 ����4:59:44
	 * @param theme
	 *            �ʼ�����
	 * @param text
	 *            �ʼ�����
	 * @param addressee
	 *            �ռ�������
	 * @param senders
	 *            ������
	 * @return int
	 * @throws
	 */
	private int startStmp(String theme, String text, String addressee,
			String senders) {
		// ����Email
		try {
			// ���ͷ��������������롣
			GMailSender sender = new GMailSender("mrluoa@gmail.com",
					"luoying.2583676");
			sender.sendMail(theme, // ����
					text, // ����
					"mrluoa@gmail.com", // ������
					addressee); // �ռ���,��һ���ǵ�gmail�����������163��qq���䶼�С�
			return 1;

		} catch (Exception e) {
			Log.e("SendMail", e.getMessage(), e);
			return 0;

		}
	}

	/**
	 * 
	 * @description ���߳��ύ����
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-8-14 ����5:09:14
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
