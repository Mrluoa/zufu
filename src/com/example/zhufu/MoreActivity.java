package com.example.zhufu;

import com.update.UpdateManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

/**
 * 
 * @description 更多界面UI
 * @version 1.0
 * @author Mrluo
 * @date 2013-8-14 下午2:10:57
 */
public class MoreActivity extends Activity {
	// LinearLayout视图
	private LinearLayout moreitems_version, more_sousuo;
	private String query;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);

	}

	/**
	 * 
	 * @description 单机事件
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-8-14 下午2:12:42
	 * @param view
	 * @return void
	 * @throws
	 */
	public void BottonEver(View view) {
		switch (view.getId()) {
		// 关于
		case R.id.more_page_row7:
			Intent intent = new Intent(MoreActivity.this, AboutActivity.class);
			startActivity(intent);
			break;
		// 搜索
		case R.id.more_sousuo:
			Intent intent1 = new Intent(MoreActivity.this, SearchActivity.class);
			startActivity(intent1);
			break;
		// 检测更新
		case R.id.more_page_row6:
			UpdateManager updateManager = new UpdateManager(MoreActivity.this);
			updateManager.checkUpdate();
			break;
		// 反馈
		case R.id.more_page_row5:
			Intent intent2 = new Intent(MoreActivity.this,OpinionActivity.class);
			startActivity(intent2);
			break;
		default:
			break;
		}
	}

	/**
	 * 监听返回--是否退出程序
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean flag = true;
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 是否退出应用
			MainActivity.Exit(MoreActivity.this);
		}
		return flag;
	}

}
