package com.example.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
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
	private SharedPreferences mPref;
	private int startX;
	private int startY;
	private WindowManager.LayoutParams params;
	private int winWidth;
	private int winHeight;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		//监听来电
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new MyListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		outCallReceiver = new outCallReceiver();
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		//注册去电广播
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
			//响铃
			case TelephonyManager.CALL_STATE_RINGING:
				String address = AddressDao.getAddress(incomingNumber);
				showToast(address);
				break;
				//闲置
			case TelephonyManager.CALL_STATE_IDLE:
				if (mWM != null && view != null) {
					mWM.removeView(view);
					view = null;
				}
				break;
			default:
				break;
			}
		}

	}
//去电的广播接受者
	class outCallReceiver extends BroadcastReceiver {

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
		//取消监听来电服务
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		unregisterReceiver(outCallReceiver);
	}
//归属地提示框显示
	private void showToast(String text) {
		mWM = (WindowManager) getSystemService(WINDOW_SERVICE);
		params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_PHONE;
		params.gravity = Gravity.LEFT + Gravity.TOP;
		params.setTitle("Toast");
		
		int lastX = mPref.getInt("lastX", 0);
		int lastY = mPref.getInt("lastY", 0);
		
		
		winWidth = mWM.getDefaultDisplay().getWidth();
		winHeight = mWM.getDefaultDisplay().getHeight();
		
		params.x = lastX;
		params.y = lastY;
		
		view = View.inflate(this, R.layout.toast_showaddress, null);
		TextView tv_toast_address = (TextView) view.findViewById(R.id.tv_toast_address);
		tv_toast_address.setText(text);
		int bgs[] = new int[]{R.drawable.call_locate_white,
				R.drawable.call_locate_orange, R.drawable.call_locate_blue,
				R.drawable.call_locate_gray, R.drawable.call_locate_green};
		int style = mPref.getInt("address_style", 0);
		view.setBackgroundResource(bgs[style]);
		mWM.addView(view, params);
		
		
		view.setOnTouchListener(new OnTouchListener() {
			
			

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int endX = (int) event.getRawX();
					int endY = (int) event.getRawY();
					
					int dx = endX - startX;
					int dy = endY - startY;
					
					
					params.x += dx;
					params.y += dy;
					
					// 防止坐标偏离屏幕
					if (params.x < 0) {
						params.x = 0;
					}

					if (params.y < 0) {
						params.y = 0;
					}

					// 防止坐标偏离屏幕
					if (params.x > winWidth - view.getWidth()) {
						params.x = winWidth - view.getWidth();
					}

					if (params.y > winHeight - view.getHeight()) {
						params.y = winHeight - view.getHeight();
					}
					
					mWM.updateViewLayout(view, params);
					
					
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_UP:
					Editor editor = mPref.edit();
					editor.putInt("lastX", params.x);
					editor.putInt("lastY", params.y);
					editor.commit();
					break;

				default:
					break;
				}
				return false;
			}
		});
		
	}
}
