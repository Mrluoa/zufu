package com.example.zhufu;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AboutActivity extends Activity {
	private Button btnstart;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_about);
		
		btnstart = (Button) findViewById(R.id.about_btn);
		btnstart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Uri uri = Uri.parse("http://zufu.mrluo.net");  
				Intent it  = new Intent(Intent.ACTION_VIEW,uri);  
				startActivity(it);  
			}
		});
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.about, menu);
		return true;
	}

}
