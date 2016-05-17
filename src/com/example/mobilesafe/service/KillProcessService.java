package com.example.mobilesafe.service;

import java.util.List;
import java.util.Timer;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class KillProcessService extends Service {

	private Timer timer;
	private LockScreenReceiver lockScreenReceiver;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	class LockScreenReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
			List<RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
			for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
				am.killBackgroundProcesses(runningAppProcessInfo.processName);
			}
		}
		
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		lockScreenReceiver = new LockScreenReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(lockScreenReceiver, filter);
		//定时清理进程
//		timer = new Timer();
//		TimerTask timerTask = new TimerTask() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				ActivityManager am = (ActivityManager) getApplicationContext().getSystemService(Activity.ACTIVITY_SERVICE);
//				List<RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
//				for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
//					am.killBackgroundProcesses(runningAppProcessInfo.processName);
//				}
//			}
//		};
//		timer.schedule(timerTask, 1000, 1000);
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
//		if(timer != null){
//			timer.cancel();
//			timer = null;
//		}
		unregisterReceiver(lockScreenReceiver);
		lockScreenReceiver = null;
		super.onDestroy();
	}
}
