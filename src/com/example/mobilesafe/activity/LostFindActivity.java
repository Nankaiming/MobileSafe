package com.example.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobilesafe.R;

public class LostFindActivity extends Activity {
	private SharedPreferences mPrefs;
	private TextView tv_safe_phone;
	private ImageView iv_lock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mPrefs = getSharedPreferences("config", MODE_PRIVATE);
		boolean configed = mPrefs.getBoolean("configed", false);
		if (configed) {
			setContentView(R.layout.activity_lost_find);
			tv_safe_phone = (TextView) findViewById(R.id.tv_safe_phone);
			iv_lock = (ImageView) findViewById(R.id.iv_lock);
			String phone = mPrefs.getString("safe_phone", "");
			tv_safe_phone.setText(phone);
			boolean protect = mPrefs.getBoolean("protect", false);
			if(protect){
				iv_lock.setImageResource(R.drawable.lock);
			}else{
				iv_lock.setImageResource(R.drawable.unlock);
			}
		} else {
			startActivity(new Intent(this,Setup1Activity.class));
			finish();
		}

	}
	public void reEnter(View view){
		startActivity(new Intent(this,Setup1Activity.class));
		finish();
	}
}
