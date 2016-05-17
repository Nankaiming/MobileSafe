package com.example.mobilesafe.receiver;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class KillProcessAllReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        //得到手机上面正在运行的进程
		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		
		for (RunningAppProcessInfo runningAppProcessInfo : appProcesses) {
			//杀死所有的进程
			activityManager.killBackgroundProcesses(runningAppProcessInfo.processName);
		}
	}

}
