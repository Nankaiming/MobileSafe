package com.example.mobilesafe.service;

import java.lang.reflect.Method;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.example.mobilesafe.db.dao.BlackNumberDao;

public class CallSafeService extends Service {
	private BlackNumberDao dao;
	private InnerReceiver innerReceiver;
	private MyListener listener;
	private TelephonyManager tm;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		dao = new BlackNumberDao(this);
        innerReceiver = new InnerReceiver();
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(Integer.MAX_VALUE);
        registerReceiver(innerReceiver, intentFilter);
        
        
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        listener = new MyListener();
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
	}
	class MyListener extends PhoneStateListener{
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				String mode = dao.findNumber(incomingNumber);
				if(mode.equals("1") || mode.equals("2")){
					Uri uri = Uri.parse("content://call_log/calls");
					getContentResolver().registerContentObserver(uri, true, new MyContentObserver(new Handler(), incomingNumber));
					endCall();
				}
				
				break;

			default:
				break;
			}
		}
	}
	class MyContentObserver extends ContentObserver{

		private String incomingNumber;
		public MyContentObserver(Handler handler,String incomingNumber) {
			super(handler);
			this.incomingNumber = incomingNumber;
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onChange(boolean selfChange) {
			// TODO Auto-generated method stub
			getContentResolver().unregisterContentObserver(this);
			deleteCallLog(incomingNumber);
			super.onChange(selfChange);
		}

		
		
	}
	private void deleteCallLog(String incomingNumber) {
		// TODO Auto-generated method stub
		Uri uri = Uri.parse("content://call_log/calls");
		getContentResolver().delete(uri, "number = ?", new String[]{incomingNumber});
	}
	public void endCall() {
		// TODO Auto-generated method stub
		try {
			Class<?> clazz = getClassLoader().loadClass("android.os.ServiceManager");
			Method method = clazz.getDeclaredMethod("getService", String.class);
			IBinder iBinder = (IBinder) method.invoke(null, TELEPHONY_SERVICE);
			
			ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);
			

            iTelephony.endCall();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	//¶ÌÐÅÀ¹½ØµÄ¹ã²¥
	private class InnerReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Object[] objects = (Object[]) intent.getExtras().get("pdus");
			for (Object object : objects) {
				SmsMessage message = SmsMessage.createFromPdu((byte[]) object);
				String originatingAddress = message.getOriginatingAddress();
				String mode = dao.findNumber(originatingAddress);
				if(mode.equals("1")){
	                  abortBroadcast();
	                }else if(mode.equals("3")){
	                    abortBroadcast();
	                }
			}
		}
		
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(listener != null){
			tm.listen(listener, PhoneStateListener.LISTEN_NONE);
			listener = null;
		}
		unregisterReceiver(innerReceiver);
	}

}
