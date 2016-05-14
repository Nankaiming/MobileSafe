package com.example.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.mobilesafe.R;
import com.example.mobilesafe.service.AddressService;
import com.example.mobilesafe.service.CallSafeService;
import com.example.mobilesafe.utils.ServiceStatusUtils;
import com.example.mobilesafe.view.SettingClickView;
import com.example.mobilesafe.view.SettingItemView;

public class SettingActivity extends Activity {
	private SettingItemView sivUpdate;
	private SharedPreferences mPrefs;
	private SettingItemView siv_address;
	private SettingItemView siv_black;
	private SettingClickView scvAddressStyle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mPrefs = getSharedPreferences("config", MODE_PRIVATE);
		setContentView(R.layout.activity_setting);
		initUpadateView();
		initAddressView();
		initAddressStyle();
		initAddressLocation();
		initBlackView();
	}
	//设置是否自动更新
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
//设置是否开启归属地查询
	private void initAddressView() {
		siv_address = (SettingItemView) findViewById(R.id.siv_address);
		boolean isRunning = ServiceStatusUtils.isServiceRunning(this,
				"com.example.mobilesafe.service.AddressService");
		if (isRunning) {
			siv_address.setChecked(true);
		} else {
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
//设置是否开启黑名单拦截
	private void initBlackView() {
		siv_black = (SettingItemView) findViewById(R.id.siv_black);
		boolean isRunning = ServiceStatusUtils.isServiceRunning(this,
				"com.example.mobilesafe.service.CallSafeService");
		if (isRunning) {
			siv_black.setChecked(true);
		} else {
			siv_black.setChecked(false);
		}
		siv_black.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				if (siv_black.isChecked()) {
					siv_black.setChecked(false);
					stopService(new Intent(SettingActivity.this,
							CallSafeService.class));
				} else {
					siv_black.setChecked(true);
					startService(new Intent(SettingActivity.this,
							CallSafeService.class));
				}
			}
		});
	}

	final String[] items = new String[] { "半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿" };
	private SettingClickView scvAddressLocation;
//初始化归属地提示框风格
	private void initAddressStyle() {
		scvAddressStyle = (SettingClickView) findViewById(R.id.scv_address_style);
		scvAddressStyle.setTitle("归属地提示框风格");
		int style = mPrefs.getInt("address_style", 0);// 读取保存的style
		scvAddressStyle.setDesc(items[style]);
		scvAddressStyle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showSingleChooseDialog();
			}
		});
	}

	protected void showSingleChooseDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("归属地提示框风格");
		int style = mPrefs.getInt("address_style", 0);
		builder.setSingleChoiceItems(items, style,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						mPrefs.edit().putInt("address_style", which).commit();
						dialog.dismiss();
						scvAddressStyle.setDesc(items[which]);
					}

				});
		builder.setNegativeButton("cancel", null);
		builder.show();
	}
//初始化归属地提示框显示位置
	private void initAddressLocation() {
		scvAddressLocation = (SettingClickView) findViewById(R.id.scv_address_location);
		scvAddressLocation.setTitle("归属地提示框显示位置");
		scvAddressLocation.setDesc("设置归属地提示框的显示位置");
		scvAddressLocation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(SettingActivity.this,DragViewActivity.class));
			}
		});
	}
}
