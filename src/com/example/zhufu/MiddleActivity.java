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
 * @description 查看界面UI
 * @version 1.0
 * @author Mrluo
 * @date 2013-8-17 上午11:19:10
 */
public class MiddleActivity extends Activity {
	// 显示的内容信息
	private TextView pick, info, send, date, title, text;
	// 音乐地址
	private String mp3 = null;
	// mp3的文件名称
	private String pathName = "";
	// mp3的完整路径
	private String mp3_path = "";
	// 是否是第1次单击
	private boolean isOnclick = true;
	private boolean isPhone = false;
	// 祝福类型
	private int type = 0;
	// 音乐路径名
	private String mppath = "zufu";
	// 执行下载对象
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

		title.setText(bl.getSend() + " 的祝福");
		pick.setText("To:" + bl.getPick());
		info.setText("　" + bl.getInfo());
		send.setText(bl.getSend());
		date.setText(bl.getDate());
		mp3 = bl.getUrl();
		// 获取祝福类型
		type = Integer.parseInt(bl.getFace());

		// 电话状态监听
		TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		telManager.listen(new MobliePhoneStateListener(),
				PhoneStateListener.LISTEN_CALL_STATE);
	}

    Runnable updateThread = new Runnable(){  
        public void run() {  
            //获得歌曲现在播放位置并设置成播放进度条的值  
        //seekBar1.setProgress(player.getCurrentPosition());  
            //每次延迟100毫秒再启动线程  
            handler.postDelayed(updateThread, 100); 
            System.out.println("总共："+Mp3Service.getDuration()+",当前："+Mp3Service.getCurrentPosition());
            if(Mp3Service.getDuration() == Mp3Service.getCurrentPosition()){
            	text.setText("播放完成...");
            }
        }  
    }; 

	// handler接收下载音乐的数据
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			// 如果下载完成
			if (msg.arg1 == 0) {
				text.setText("加载完成...");
				System.out.println("===============>音乐下载成功");
				// 新建Intent准备跳转
				Intent service = new Intent(MiddleActivity.this,
						Mp3Service.class);
				// 添加传入传入一个字符 0 代表播放音乐
				service.putExtra("state", 0);
				service.putExtra("type", type);
				startService(service);
				text.setText("正在播放...");
				handler.post(updateThread);
			}
			// 否则下载音乐失败
			else {
				System.out.println("音乐加载失败！");
			}
		}
	};

	boolean pause;

	/**
	 * 
	 * @description 播放音乐单机事件
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-8-1 下午9:14:48
	 * @param view
	 * @return void
	 * @throws
	 */
	public void mediaplay(View view) {
		switch (view.getId()) {
		case R.id.midd_play:

			// 获取sd卡路径
			String SDPath = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/";
			// 音乐文件名称
			pathName = type + ".mp3";
			// 音乐完成路径
			mp3_path = SDPath + mppath + "/" + pathName;
			// 新建一个文件
			File file = new File(mp3_path);
			// 如果文件存在
			if (file.isFile()) {
				if (isOnclick) {
					// 就直接跳转

					Intent service = new Intent(MiddleActivity.this,
							Mp3Service.class);
					service.putExtra("state", 0);
					service.putExtra("type", type);
					startService(service);
					text.setText("正在播放...");
					isOnclick = false;
				} else {
					// 暂停下
					Mp3Service.setPase();
					if (Mp3Service.getIsPlaying()) {
						text.setText("正在播放...");
					}else{
						text.setText("暂停中...");
					}
				}
			}
			// 否则就重网络上下载数据
			else {
				text.setText("正在加载中...");
				System.out.println("音乐文件(不存在)");
				new Thread(new Runnable() {

					@Override
					public void run() {
						Message ms = handler.obtainMessage();
						// 执行下载
						System.out.println("===============>音乐执行下载");
						// 下载完成返回下载代码
						ms.arg1 = httpDownloadUtil.downFile(mp3, "zufu/",
								pathName);
						// 发送消息到handler
						handler.sendMessage(ms);
					}
				}).start();
			}

			break;
		}
	}

	/**
	 * 监听按键
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
	 * @description 电话来了，暂停事件
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-8-1 下午11:11:54
	 */
	private class MobliePhoneStateListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE: /* 无任何状态时 */
				// 如果是电话暂停
				if (isPhone) {
					Mp3Service.setPase();
					isPhone = false;
				}
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK: /* 接起电话时 */

			case TelephonyManager.CALL_STATE_RINGING: /* 电话进来时 */

				Mp3Service.setPase();
				// 设置电话暂停
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
