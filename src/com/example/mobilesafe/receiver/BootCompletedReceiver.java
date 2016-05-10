package com.example.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
/**
 * 监听开机启动的广播
 * @author God vision
 *
 */
public class BootCompletedReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		SharedPreferences sp = context.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		boolean protect = sp.getBoolean("protect", false);
		if (protect) {
			String sim = sp.getString("sim", null);
			if (!TextUtils.isEmpty(sim)) {
				TelephonyManager tm = (TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE);
				String simSerialNumber = tm.getSimSerialNumber();
				simSerialNumber = "22222222";
				if (simSerialNumber.equals(sim)) {
					System.out.println("sim卡安全");
				} else {
					System.out.println("sim卡变更");
					String phone = sp.getString("safe_phone", "");
					SmsManager smsManager = SmsManager.getDefault();
					smsManager.sendTextMessage(phone, null, "sim card changed",
							null, null);
				}
			}
		}
	}

}
