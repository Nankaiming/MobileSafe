package com.example.mobilesafe.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mobilesafe.R;
import com.example.mobilesafe.utils.SmsUtils;
import com.example.mobilesafe.utils.SmsUtils.BackUpCallbackSms;
import com.example.mobilesafe.utils.ToastUtils;

public class AtoolsActivity extends Activity {
	private ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atools);
	}
	//归属地号码查询工具
	public void numberAddressQuery(View view){
		startActivity(new Intent(this, AddressActivity.class));
	}
	public void backUpsms(View view){
		pd = new ProgressDialog(AtoolsActivity.this);
		pd.setTitle("提示");
		pd.setMessage("稍安勿躁。正在备份。你等着吧。。");
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.show();
		new Thread(){
			public void run() {
				boolean result = SmsUtils.backUp(AtoolsActivity.this, pd,new BackUpCallbackSms() {
					
					@Override
					public void onBackUpSms(int progress) {
						// TODO Auto-generated method stub
						pd.setProgress(progress);
					}
					
					@Override
					public void befor(int count) {
						// TODO Auto-generated method stub
						pd.setMax(count);
					}
				});
				if(result){
					ToastUtils.showToast(AtoolsActivity.this, "success");
//					Log.e("error", "success");
				}else{
					ToastUtils.showToast(AtoolsActivity.this, "failure");
//					Log.e("error", "failure");
				}
				pd.dismiss();
			};
		}.start();
	}
}
