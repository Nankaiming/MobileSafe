package com.example.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.mobilesafe.db.dao.AddressDao;

public class AddressService extends Service {

	private TelephonyManager tm;
	private MyListener listener;
	private outCallReceiver outCallReceiver;
	private WindowManager mWM;
	private View view;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new MyListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		outCallReceiver = new outCallReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		registerReceiver(outCallReceiver, intentFilter);
	}

	class MyListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				String address = AddressDao.getAddress(incomingNumber);
				showToast(address);
				break;
			case TelephonyManager.CALL_STATE_IDLE:
				if(mWM != null && view != null){
					mWM.removeView(view);
					view = null;
				}
				break;
			default:
				break;
			}
		}

	}
	class outCallReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String number = getResultData();
			String address = AddressDao.getAddress(number);
			showToast(address);
		}
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		unregisterReceiver(outCallReceiver);
	}
	private void showToast(String text) {
		mWM = (WindowManager) getSystemService(WINDOW_SERVICE);
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_TOAST;
		params.setTitle("Toast");
		view = View.inflate(this, R.layout.toast_showaddress, null);
		TextView tv_toast_address = (TextView) view.findViewById(R.id.tv_toast_address);
		tv_toast_address.setText(text);
		mWM.addView(view, params);
		
	}
}
