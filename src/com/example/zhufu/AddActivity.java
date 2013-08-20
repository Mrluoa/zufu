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
	private String[] value_music = { "����ף��", "����ף��", "������", "Ǹ��ŨŨ" };

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
		dialog.setMessage("����Ϊ�㴫��ף��...");
		dialog.setCancelable(true);

		musicAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, value_music);
		// ���
		musicAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��������
		music.setAdapter(musicAdapter);

		// ѡ�����ּ����¼�
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
		// �ύ�¼�
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				final Map<String, String> bl = new HashMap<String, String>();
				bl.put("pick", pick.getText().toString().trim());
				bl.put("tel", tel.getText().toString().trim());
				bl.put("send", send.getText().toString().trim());
				bl.put("info", info.getText().toString().trim());

				int type;
				// ����MP3����
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
				// �ж��Ƿ�Ϊ��
				if (bl.get("pick").equals("")) {
					Toast.makeText(getApplicationContext(), "������δ��д��",
							Toast.LENGTH_SHORT).show();
					pick.requestFocus();
				} else if (bl.get("tel").trim().equals("")) {
					Toast.makeText(getApplicationContext(), "�ֻ���δ��д��",
							Toast.LENGTH_SHORT).show();
					tel.requestFocus();
				} else if (bl.get("send").equals("")) {
					Toast.makeText(getApplicationContext(), "������δ��д��",
							Toast.LENGTH_SHORT).show();
					send.requestFocus();
				} else if (bl.get("info").equals("")) {
					Toast.makeText(getApplicationContext(), "��������δ��д��",
							Toast.LENGTH_SHORT).show();
					info.requestFocus();
				}
				// ��Ϊ��ִ��
				else {
					SimpleDateFormat format = new SimpleDateFormat(
							"���ˢ��:"+"yyyy-MM-dd HH:mm:ss");
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
	 * @description �ύ�����߳�������
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-7-26 ����10:06:51
	 */
	private class PostAdd extends AsyncTask<Map<String, String>, Void, String> {
		@Override
		protected void onPreExecute() {
			// �������ؿ�

			super.onPreExecute();
			dialog.show();
		}

		@Override
		protected String doInBackground(Map<String, String>... arg0) {
			return HttpURL.PostBlessing(CommonURL.MALL_ADD_URL, arg0[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			System.out.println("================= �ύ��� ===============>"
					+ result);
			// dialog.dismiss();
			String istemp = new String(result);
			
			if (istemp.trim().equals("true")) {
				Toast.makeText(getApplicationContext(), "�ύף���ɹ���",
						Toast.LENGTH_SHORT).show();
				pick.setText("");
				tel.setText("");
				send.setText("");
				info.setText("");
			} else {
				Toast.makeText(getApplicationContext(), "�ύף��ʧ�ܣ�",
						Toast.LENGTH_SHORT).show();
			}
			dialog.dismiss();
			super.onPostExecute(result);
		}
	}

	/**
	 * 
	 * @description �ж������Ƿ����
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-7-24 ����3:56:47
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
	 * ��������--�Ƿ��˳�����
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean flag = true;
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// �Ƿ��˳�Ӧ��
			MainActivity.Exit(AddActivity.this);
		}
		return flag;
	}
}
