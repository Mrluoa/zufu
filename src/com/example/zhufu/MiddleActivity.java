package com.example.zhufu;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import com.domain.Blessing;
import com.tool.HttpDownloader;
import com.tool.Mp3Service;
/**
 * 
 * @description �鿴����UI
 * @version 1.0
 * @author Mrluo
 * @date 2013-8-17 ����11:19:10
 */
public class MiddleActivity extends Activity {
	// ��ʾ��������Ϣ
	private TextView pick, info, send, date, title, text;
	// ���ֵ�ַ
	private String mp3 = null;
	// mp3���ļ�����
	private String pathName = "";
	// mp3������·��
	private String mp3_path = "";
	// �Ƿ��ǵ�1�ε���
	private boolean isOnclick = true;
	private boolean isPhone = false;
	// ף������
	private int type = 0;
	// ����·����
	private String mppath = "zufu";
	// ִ�����ض���
	private HttpDownloader httpDownloadUtil = new HttpDownloader();

	public Handler musicHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_middle);

		Blessing bl = (Blessing) getIntent().getSerializableExtra("bless");

		pick = (TextView) findViewById(R.id.midd_pick);
		info = (TextView) findViewById(R.id.midd_content);
		send = (TextView) findViewById(R.id.midd_send);
		date = (TextView) findViewById(R.id.midd_date);
		title = (TextView) findViewById(R.id.midd_title);
		text = (TextView) findViewById(R.id.midd_text);

		title.setText(bl.getSend() + " ��ף��");
		pick.setText("To:" + bl.getPick());
		info.setText("��" + bl.getInfo());
		send.setText(bl.getSend());
		date.setText(bl.getDate());
		mp3 = bl.getUrl();
		// ��ȡף������
		type = Integer.parseInt(bl.getFace());

		// �绰״̬����
		TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		telManager.listen(new MobliePhoneStateListener(),
				PhoneStateListener.LISTEN_CALL_STATE);
	}

    Runnable updateThread = new Runnable(){  
        public void run() {  
            //��ø������ڲ���λ�ò����óɲ��Ž�������ֵ  
        //seekBar1.setProgress(player.getCurrentPosition());  
            //ÿ���ӳ�100�����������߳�  
            handler.postDelayed(updateThread, 100); 
            System.out.println("�ܹ���"+Mp3Service.getDuration()+",��ǰ��"+Mp3Service.getCurrentPosition());
            if(Mp3Service.getDuration() == Mp3Service.getCurrentPosition()){
            	text.setText("�������...");
            }
        }  
    }; 

	// handler�����������ֵ�����
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			// ����������
			if (msg.arg1 == 0) {
				text.setText("�������...");
				System.out.println("===============>�������سɹ�");
				// �½�Intent׼����ת
				Intent service = new Intent(MiddleActivity.this,
						Mp3Service.class);
				// ��Ӵ��봫��һ���ַ� 0 ����������
				service.putExtra("state", 0);
				service.putExtra("type", type);
				startService(service);
				text.setText("���ڲ���...");
				handler.post(updateThread);
			}
			// ������������ʧ��
			else {
				System.out.println("���ּ���ʧ�ܣ�");
			}
		}
	};

	boolean pause;

	/**
	 * 
	 * @description �������ֵ����¼�
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-8-1 ����9:14:48
	 * @param view
	 * @return void
	 * @throws
	 */
	public void mediaplay(View view) {
		switch (view.getId()) {
		case R.id.midd_play:

			// ��ȡsd��·��
			String SDPath = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/";
			// �����ļ�����
			pathName = type + ".mp3";
			// �������·��
			mp3_path = SDPath + mppath + "/" + pathName;
			// �½�һ���ļ�
			File file = new File(mp3_path);
			// ����ļ�����
			if (file.isFile()) {
				if (isOnclick) {
					// ��ֱ����ת

					Intent service = new Intent(MiddleActivity.this,
							Mp3Service.class);
					service.putExtra("state", 0);
					service.putExtra("type", type);
					startService(service);
					text.setText("���ڲ���...");
					isOnclick = false;
				} else {
					// ��ͣ��
					Mp3Service.setPase();
					if (Mp3Service.getIsPlaying()) {
						text.setText("���ڲ���...");
					}else{
						text.setText("��ͣ��...");
					}
				}
			}
			// ���������������������
			else {
				text.setText("���ڼ�����...");
				System.out.println("�����ļ�(������)");
				new Thread(new Runnable() {

					@Override
					public void run() {
						Message ms = handler.obtainMessage();
						// ִ������
						System.out.println("===============>����ִ������");
						// ������ɷ������ش���
						ms.arg1 = httpDownloadUtil.downFile(mp3, "zufu/",
								pathName);
						// ������Ϣ��handler
						handler.sendMessage(ms);
					}
				}).start();
			}

			break;
		}
	}

	/**
	 * ��������
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent service = new Intent(MiddleActivity.this, Mp3Service.class);
			service.putExtra("state", 1);
			stopService(service);
			MiddleActivity.this.finish();
		}
		return false;
	}

	/**
	 * 
	 * @description �绰���ˣ���ͣ�¼�
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-8-1 ����11:11:54
	 */
	private class MobliePhoneStateListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE: /* ���κ�״̬ʱ */
				// ����ǵ绰��ͣ
				if (isPhone) {
					Mp3Service.setPase();
					isPhone = false;
				}
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK: /* ����绰ʱ */

			case TelephonyManager.CALL_STATE_RINGING: /* �绰����ʱ */

				Mp3Service.setPase();
				// ���õ绰��ͣ
				isPhone = true;
				break;
			default:
				break;

			}

		}

	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Intent service = new Intent(MiddleActivity.this,
				Mp3Service.class);
		service.putExtra("state", 1);
		service.putExtra("type", type);
		stopService(service);
	}
}
