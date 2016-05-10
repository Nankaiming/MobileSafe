package com.example.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mobilesafe.R;

public class Setup3Activity extends BaseSetupActivity {
	private EditText et_phone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		et_phone = (EditText) findViewById(R.id.et_phone);
		String phone = mPref.getString("safe_phone", "");
		et_phone.setText(phone);
	}

	@Override
	public void showNextPage() {
		// TODO Auto-generated method stub
		String phone = et_phone.getText().toString().trim();
		if(TextUtils.isEmpty(phone)){
			Toast.makeText(this, "safe phone can't be empty", Toast.LENGTH_SHORT).show();
			return;
		}
		mPref.edit().putString("safe_phone", phone).commit();
		startActivity(new Intent(this, Setup4Activity.class));
		finish();
		overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
	}

	@Override
	public void showPreviousPage() {
		// TODO Auto-generated method stub
		startActivity(new Intent(this, Setup2Activity.class));
		finish();
		overridePendingTransition(R.anim.trans_previous_in,
				R.anim.trans_previous_out);
	}
	public void selectContact(View view){
		Intent intent = new Intent(this,ContactActivity.class);
		startActivityForResult(intent, 1);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == Activity.RESULT_OK) {
			String phone = data.getStringExtra("phone");
			phone = phone.replaceAll("-", "").replaceAll(" ", "");
			et_phone.setText(phone);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
