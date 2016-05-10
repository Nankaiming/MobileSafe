package com.example.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.mobilesafe.R;

public class Setup1Activity extends BaseSetupActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup1);
	}

	@Override
	public void showNextPage() {
		// TODO Auto-generated method stub
		startActivity(new Intent(this, Setup2Activity.class));
		finish();
		overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
	}

	@Override
	public void showPreviousPage() {
		// TODO Auto-generated method stub

	}
}
