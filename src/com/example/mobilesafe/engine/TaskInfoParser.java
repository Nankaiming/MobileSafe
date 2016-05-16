package com.example.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug.MemoryInfo;

import com.example.mobilesafe.R;
import com.example.mobilesafe.bean.TaskInfo;

public class TaskInfoParser {
	public static List<TaskInfo> getTaskInfos(Context context){
		List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();
		PackageManager packageManager = context.getPackageManager();
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
		for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
			TaskInfo taskInfo = new TaskInfo();
			//获取进程名称
			String processName = runningAppProcessInfo.processName;
			taskInfo.setProcessName(processName);
			//获取进程占用内存
			MemoryInfo[] memoryInfo = am.getProcessMemoryInfo(new int[]{runningAppProcessInfo.pid});
			int totalPrivateDirty = memoryInfo[0].getTotalPrivateDirty() * 1024;
			taskInfo.setMemorySize(totalPrivateDirty);
			try {
				//获取进程包的信息
				PackageInfo packageInfo = packageManager.getPackageInfo(processName, 0);
				//获取进程图标
				Drawable icon = packageInfo.applicationInfo.loadIcon(packageManager);
				taskInfo.setIcon(icon);
				//获取应用名称
				String appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
				taskInfo.setAppName(appName);
				//判断是否是用户进程还是系统进程
				int flags = packageInfo.applicationInfo.flags;
				if((flags & ApplicationInfo.FLAG_SYSTEM) != 0){
					taskInfo.setUserApp(false);
				}else{
					taskInfo.setUserApp(true);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// 系统核心库里面有些系统没有图标。必须给一个默认的图标
				taskInfo.setAppName(processName);
				taskInfo.setIcon(context.getResources().getDrawable(R.drawable.ic_launcher));
				taskInfo.setUserApp(true);
			}
			taskInfos.add(taskInfo);
		}
		return taskInfos;
	}
}
