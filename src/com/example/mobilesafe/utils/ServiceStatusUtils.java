package com.example.mobilesafe.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceStatusUtils {
	public static boolean isServiceRunning(Context context,String serviceName){
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> runningServices = activityManager.getRunningServices(100);
		for (RunningServiceInfo runningServiceInfo : runningServices) {
			String className = runningServiceInfo.service.getClassName();
			if(className.equals(serviceName)){
				return true;
			}
		}
		return false;
	
	}
	public static int getProcessCount(Context context){
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
		return runningAppProcesses.size();
	}
	public static long getAvailMem(Context context){
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
		am.getMemoryInfo(memoryInfo);
		return memoryInfo.availMem;
	}
	public static long getTotalMem(Context context){
		//在低版本的手机上不适用
//		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//		MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
//		am.getMemoryInfo(memoryInfo);
//		return memoryInfo.totalMem;
		try {
			//
			FileInputStream fis = new FileInputStream(new File("/proc/meminfo"));
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
			String readLine = reader.readLine();
			StringBuilder sb = new StringBuilder();
			for (char c : readLine.toCharArray()) {
				if(c >= '0' && c <= '9'){
					sb.append(c);
				}
			}
			return Long.parseLong(sb.toString()) * 1024;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
}
