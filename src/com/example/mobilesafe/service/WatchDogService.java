package com.example.mobilesafe.service;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;

import com.example.mobilesafe.activity.EnterPwdActivity;
import com.example.mobilesafe.db.dao.AppLockDao;

public class WatchDogService extends Service {

	private ActivityManager activityManager;
	private AppLockDao dao;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	// ��ʱֹͣ�����İ���
	private String tempStopProtectPackageName;

	private class WatchDogReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals("com.example.mobilesafe.stopprotect")) {
				// ��ȡ��ֹͣ�����Ķ���

				tempStopProtectPackageName = intent
						.getStringExtra("packageName");
			} else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
				tempStopProtectPackageName = null;
				// �ù���Ϣ
				falg = false;
			} else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
				// �ù������ɻ�
				if (falg == false) {
					startWatDog();
				}
			}

		}

	}

	class AppLockObserver extends ContentObserver {

		public AppLockObserver(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onChange(boolean selfChange) {
			// TODO Auto-generated method stub
			appLockInfos = dao.findAll();
			super.onChange(selfChange);
		}

	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		lockObserver = new AppLockObserver(new Handler());
		dao = new AppLockDao(this);
		getContentResolver().registerContentObserver(
				Uri.parse("content://com.example.mobilesafe.change"), true,
				lockObserver);

		appLockInfos = dao.findAll();

		// ע��㲥������

		receiver = new WatchDogReceiver();

		IntentFilter filter = new IntentFilter();
		// ֹͣ����
		filter.addAction("com.example.mobilesafe.stopprotect");

		// ע��һ�������Ĺ㲥
		/**
		 * ����Ļ��ס��ʱ�򡣹�����Ϣ ��Ļ������ʱ���ù������
		 */
		filter.addAction(Intent.ACTION_SCREEN_OFF);

		filter.addAction(Intent.ACTION_SCREEN_ON);

		registerReceiver(receiver, filter);

		// ��ȡ�����̹�����

		activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

		// 1 ������Ҫ��ȡ����ǰ������ջ

		// 2ȡ����ջ�����������

		startWatDog();
	}

	// ��ǵ�ǰ�Ŀ��ȹ��Ƿ�ͣ����
	private boolean falg = false;
	private List<String> appLockInfos;
	private WatchDogReceiver receiver;
	private AppLockObserver lockObserver;

	private void startWatDog() {

		new Thread() {
			public void run() {
				falg = true;
				while (falg) {
					// ���������һֱ�ں�̨���С�Ϊ�˱������������
					// ��ȡ����ǰ�������е�����ջ
					List<RunningTaskInfo> tasks = activityManager
							.getRunningTasks(1);
					// ��ȡ��������Ľ���
					RunningTaskInfo taskInfo = tasks.get(0);
					// ��ȡ�����Ӧ�ó���İ���
					String packageName = taskInfo.topActivity.getPackageName();

//					System.out.println(packageName);
					// �ù���Ϣһ��
//					SystemClock.sleep(1000);
					// ֱ�Ӵ����ݿ�������ҵ�ǰ������
					// ��������Ż����ĳɴ��ڴ浱��Ѱ��
					if (appLockInfos.contains(packageName)) {
						// if(dao.find(packageName)){
						// System.out.println("�ڳ��������ݿ�����");

						// ˵����Ҫ��ʱȡ������
						// ����Ϊ�û���������ȷ������
						if (packageName.equals(tempStopProtectPackageName)) {

						} else {
							Intent intent = new Intent(WatchDogService.this,
									EnterPwdActivity.class);
							/**
							 * ��Ҫע�⣺������ڷ���������activity�������Ļ�����Ҫ����flag
							 */
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							// ֹͣ�����Ķ���
							intent.putExtra("packageName", packageName);

							startActivity(intent);
						}

					}
				}

			};
		}.start();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		falg = false;
		unregisterReceiver(receiver);
		getContentResolver().unregisterContentObserver(lockObserver);
		receiver = null;
	}

}
