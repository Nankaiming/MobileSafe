package com.example.mobilesafe.receiver;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.example.mobilesafe.service.KillProcesWidgetService;

public class MyAppWidget extends AppWidgetProvider {

	//��ΪAppWidgetProvider�̳���BroadcastReceiver����Ҳ�ǹ㲥
	
	
	//���ǲ����ڴ˹㲥������ʱ�Ĳ�������ΪonEnabled()��onDisabled()�����������ںܶ�
	
	//ͨ����broadcast onReceive()�������������߳��ϣ����Բ�������ʱ����



	

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
