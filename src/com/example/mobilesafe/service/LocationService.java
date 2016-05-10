package com.example.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
/**
 * 归属地查询服务
 * @author God vision
 *
 */
public class LocationService extends Service {

	private LocationManager lm;
	private MyLocationListener listener;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setCostAllowed(true);
		String bestProvider = lm.getBestProvider(criteria, true);
		listener = new MyLocationListener();
		lm.requestLocationUpdates(bestProvider, 0, 0, listener);
		super.onCreate();
	}

	private class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			StringBuilder sb = new StringBuilder();
			System.out.println("精确度：" + location.getAccuracy());
			System.out.println("移动的速度：" + location.getSpeed());
			System.out.println("纬度：" + location.getLatitude());
			System.out.println("经度：" + location.getLongitude());
			System.out.println("海拔：" + location.getAltitude());
			sb.append("accuracy:" + location.getAccuracy() + "\n");
			sb.append("speed:" + location.getSpeed() + "\n");
			sb.append("weidu:" + location.getLatitude() + "\n");
			sb.append("jingdu:" + location.getLongitude() + "\n");
			String result = sb.toString();
			SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
			SmsManager.getDefault().sendTextMessage(
					sp.getString("safe_phone", ""), null, result, null, null);
			stopSelf();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		lm.removeUpdates(listener);
		super.onDestroy();
	}

}
