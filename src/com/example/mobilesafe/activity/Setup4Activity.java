package com.example.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.example.mobilesafe.R;

public class Setup4Activity extends BaseSetupActivity {
	private CheckBox cb_protect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		cb_protect = (CheckBox) findViewById(R.id.cb_protect);
		boolean protect = mPref.getBoolean("protect", false);
		if (protect) {
			cb_protect.setChecked(true);
			cb_protect.setText("防盗保护已经开启");
		} else {
			cb_protect.setChecked(false);
			cb_protect.setText("防盗保护没有开启");
		}
		cb_protect.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					cb_protect.setText("防盗保护已经开启");
					mPref.edit().putBoolean("protect", true).commit();
				} else {
					cb_protect.setText("防盗保护没有开启");
					mPref.edit().putBoolean("protect", false).commit();
				}
			}
		});
	}

	@Override
	public void showNextPage() {
		startActivity(new Intent(this, LostFindActivity.class));
		finish();
		mPref.edit().putBoolean("configed", true).commit();
		overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
	}

	@Override
	public void showPreviousPage() {
		// TODO Auto-generated method stub
		startActivity(new Intent(this, Setup3Activity.class));
		finish();
		overridePendingTransition(R.anim.trans_previous_in,
				R.anim.trans_previous_out);
	}
}
