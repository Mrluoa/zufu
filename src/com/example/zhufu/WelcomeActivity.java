package com.example.zhufu;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class WelcomeActivity extends Activity {
	// 延迟时间
	private final int SPLASH_DISPLAY_LENGHT = 5000;
//	private ImageView welcome = null;
//	private Bitmap one,two,three;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.welcome);
//		one = CreatImage(WelcomeActivity.this, R.drawable.we1);
//		two = CreatImage(WelcomeActivity.this, R.drawable.we2);
//		three = CreatImage(WelcomeActivity.this, R.drawable.we3);
//		welcome = (ImageView) findViewById(R.id.welcome_show);

//		Random random = new Random();
//		//int type = random.nextInt(3);
//		int type = 2;
//		System.out.println("第"+type+"个页面");
//		switch (type) {
//		case 0:
//			welcome.setImageBitmap(one);
//			break;
//		case 1:
//			welcome.setImageBitmap(two);
//			break;
//		case 2:
//			welcome.setImageBitmap(three);
//			break;
//		}

		/**
		 * 使用handler来处理
		 */
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent mainIntent = new Intent(WelcomeActivity.this,
						MainActivity.class);
				System.out.println("===>>>>  welcome"
						+ isNetworkConnected(WelcomeActivity.this));
				mainIntent.putExtra("isNetWork",
						isNetworkConnected(WelcomeActivity.this));
				WelcomeActivity.this.startActivity(mainIntent);
				WelcomeActivity.this.finish();
			}

		}, SPLASH_DISPLAY_LENGHT);

	}

	/**
	 * 
	 * @description 判断网络
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-7-24 下午4:04:24
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

	/***
	 * 
	 * 加载本地图片
	 * 
	 * @param context
	 *            ：主运行函数实例
	 * 
	 * @param bitAdress
	 *            ：图片地址，一般指向R下的drawable目录
	 * 
	 * @return
	 */
	public final Bitmap CreatImage(Context context, int bitAdress) {

		Bitmap bitmaptemp = null;

		bitmaptemp = BitmapFactory.decodeResource(context.getResources(),
				bitAdress);

		return bitmaptemp;

	}
}
