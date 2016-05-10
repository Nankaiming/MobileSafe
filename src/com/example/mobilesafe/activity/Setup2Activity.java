package com.example.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.mobilesafe.R;
import com.example.mobilesafe.view.SettingItemView;

public class Setup2Activity extends BaseSetupActivity {
	private SettingItemView siv_sim;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		siv_sim = (SettingItemView) findViewById(R.id.siv_sim);
		String sim = mPref.getString("sim", null);
		if (!TextUtils.isEmpty(sim)) {
			siv_sim.setChecked(true);
		} else {
			siv_sim.setChecked(false);
		}
		siv_sim.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (siv_sim.isChecked()) {
					siv_sim.setChecked(false);
					mPref.edit().remove("sim").commit();
				} else {
					siv_sim.setChecked(true);
					TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
					//获取sim卡序列号
					String simSerialNumber = tm.getSimSerialNumber();
					simSerialNumber = "22222222";
					System.out.println("sim卡序列号:" + simSerialNumber);
					mPref.edit().putString("sim", simSerialNumber).commit();
				}
			}
		});
	}

	@Override
	public void showNextPage() {
		// TODO Auto-generated method stub
		String sim = mPref.getString("sim", null);
		if(TextUtils.isEmpty(sim)){
			return;
		}
		startActivity(new Intent(this, Setup3Activity.class));
		finish();
		overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
	}

	@Override
	public void showPreviousPage() {
		// TODO Auto-generated method stub
		startActivity(new Intent(this, Setup1Activity.class));
		finish();
		overridePendingTransition(R.anim.trans_previous_in,
				R.anim.trans_previous_out);
	}
}
