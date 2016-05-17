package com.example.mobilesafe.service;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.example.mobilesafe.R;
import com.example.mobilesafe.bean.TaskInfo;
import com.example.mobilesafe.engine.TaskInfoParser;
import com.example.mobilesafe.receiver.MyAppWidget;
import com.example.mobilesafe.utils.ServiceStatusUtils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.Formatter;
import android.widget.RemoteViews;

public class KillProcesWidgetService extends Service {

	private AppWidgetManager widgetManager;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		widgetManager = AppWidgetManager.getInstance(this);
		
		Timer timer = new Timer();
		
		TimerTask timerTask = new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				RemoteViews views = new RemoteViews(getPackageName(), R.layout.process_widget);
				
				int processCount = ServiceStatusUtils.getProcessCount(getApplicationContext());
				
				long availMem = ServiceStatusUtils.getAvailMem(getApplicationContext());
				
				views.setTextViewText(R.id.process_count, "正在运行的软件:" + String.valueOf(processCount));
				
				views.setTextViewText(R.id.process_memory, "可用内存:" + Formatter.formatFileSize(getApplicationContext(), availMem));
				Intent intent = new Intent();
				intent.setAction("com.example.mobilesafe");
				PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
				
				views.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);
				
				ComponentName componentName = new ComponentName(getApplicationContext(), MyAppWidget.class);
				widgetManager.updateAppWidget(componentName , views);
			}
		};
		//没五秒更新一次
		timer.schedule(timerTask, 0, 5000);
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
