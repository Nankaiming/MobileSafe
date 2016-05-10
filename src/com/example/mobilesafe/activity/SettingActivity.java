package com.example.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.mobilesafe.R;
import com.example.mobilesafe.service.AddressService;
import com.example.mobilesafe.utils.ServiceStatusUtils;
import com.example.mobilesafe.view.SettingItemView;

public class SettingActivity extends Activity {
	private SettingItemView sivUpdate;
	private SharedPreferences mPrefs;
	private SettingItemView siv_address;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mPrefs = getSharedPreferences("config", MODE_PRIVATE);
		setContentView(R.layout.activity_setting);
		initUpadateView();
		initAddressView();
	}

	private void initUpadateView() {
		sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
		boolean autoUpdate = mPrefs.getBoolean("auto_update", true);
		sivUpdate.setChecked(autoUpdate);
		sivUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (sivUpdate.isChecked()) {
					sivUpdate.setChecked(false);
					mPrefs.edit().putBoolean("auto_update", false).commit();
				} else {
					sivUpdate.setChecked(true);
					mPrefs.edit().putBoolean("auto_update", true).commit();
				}

			}
		});
	}

	private void initAddressView() {
		siv_address = (SettingItemView) findViewById(R.id.siv_address);
		boolean isRunning = ServiceStatusUtils.isServiceRunning(this,
				"com.example.mobilesafe.service.AddressService");
		if(isRunning){
			siv_address.setChecked(true);
		}else{
			siv_address.setChecked(false);
		}
		siv_address.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				if (siv_address.isChecked()) {
					siv_address.setChecked(false);
					stopService(new Intent(SettingActivity.this,
							AddressService.class));
				} else {
					siv_address.setChecked(true);
					startService(new Intent(SettingActivity.this,
							AddressService.class));
				}
			}
		});
	}
}
