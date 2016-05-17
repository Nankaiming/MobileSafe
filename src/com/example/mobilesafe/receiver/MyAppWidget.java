package com.example.mobilesafe.receiver;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.example.mobilesafe.service.KillProcesWidgetService;

public class MyAppWidget extends AppWidgetProvider {

	//因为AppWidgetProvider继承了BroadcastReceiver所以也是广播
	
	
	//我们不能在此广播中做耗时的操作，因为onEnabled()和onDisabled()方法调用周期很短
	
	//通常的broadcast onReceive()方法运行在主线程上，所以不能做耗时操作



	

	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		super.onEnabled(context);
		Intent intent = new Intent(context,KillProcesWidgetService.class);
		context.startService(intent);
	}

	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		super.onDisabled(context);
		Intent intent = new Intent(context,KillProcesWidgetService.class);
		context.stopService(intent);
	}
	
}
