package com.example.zhufu;


import com.tool.ExitApplication;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TabHost;
/**
 * 
 * @description ������UI
 * @version 1.0
 * @author Mrluo
 * @date 2013-8-17 ����11:19:35
 */
public class MainActivity extends TabActivity {
	private RadioButton main_home, main_cat, main_inter, main_more;
	private boolean isNetwork = false;
	TabHost tabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// �����ޱ���
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		isNetwork = getIntent().getBooleanExtra("isNetWork", false);
		//��ӵ�ǰactivity
				ExitApplication.getInstance().addActivity(this);

		// AlertDialog.Builder builder = new Builder(MainActivity.this);
		// builder.setTitle("��������");
		// builder.setMessage("�����������������");
		// builder.setPositiveButton("��������", new OnClickListener() {
		// public void onClick(DialogInterface dialog, int which) {
		// Intent intent = new Intent();
		// intent.setClassName("com.android.settings",
		// "com.android.settings.WirelessSettings");
		// startActivity(intent);
		// }
		// });
		// builder.setNegativeButton("ȡ��", new OnClickListener() {
		//
		// public void onClick(DialogInterface dialog, int which) {
		//
		// }
		// });
		// builder.create().show();

		iniTab();
		iniData();

	}

	// TabHost
	private void iniTab() {
		tabHost = getTabHost();
		Intent home_Intent = new Intent(this, HomeActivity.class);
		home_Intent.putExtra("isNetWork", isNetwork);
		tabHost.addTab(tabHost.newTabSpec("home").setIndicator("home")
				.setContent(home_Intent));
		tabHost.addTab(tabHost.newTabSpec("cat").setIndicator("cat")
				.setContent(new Intent(this, CatActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("add").setIndicator("add")
				.setContent(new Intent(this, AddActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("more").setIndicator("more")
				.setContent(new Intent(this, MoreActivity.class)));
		

	}

	// ��ʼ��
	private void iniData() {
		main_home = (RadioButton) findViewById(R.id.main_tab_home);
		main_cat = (RadioButton) findViewById(R.id.main_tab_catagory);
		main_inter = (RadioButton) findViewById(R.id.main_tab_buy);
		main_more = (RadioButton) findViewById(R.id.main_tab_more);
		main_more = (RadioButton) findViewById(R.id.main_tab_more);
		
		main_home.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					tabHost.setCurrentTabByTag("home");
				}
			}
		});
		main_cat.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					tabHost.setCurrentTabByTag("cat");
				}
			}
		});
		main_inter.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
					tabHost.setCurrentTabByTag("add");
				}
			}
		});
		main_more.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(arg1){
					tabHost.setCurrentTabByTag("more");
				}
			}
		});
	}
	
	/**
	 * ��������--�Ƿ��˳�����
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean flag = true;
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// �Ƿ��˳�Ӧ��
			Exit(MainActivity.this);
		}
		return flag;
	}
	
	/**
	 * �˳�����
	 * 
	 * @param cont
	 */
	public static void Exit(final Context cont) {
		AlertDialog.Builder builder = new AlertDialog.Builder(cont);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle("�Ƿ��˳�У԰ף��ǽ?");
		builder.setPositiveButton("�˳�",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// �˳�
						ExitApplication.getInstance().exit();
					}
				});
		builder.setNegativeButton("ȡ��",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.show();
	}
}
