package com.example.mobilesafe.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.example.mobilesafe.bean.AppInfo;

public class AppInfos {
	public static List<AppInfo> getAppInfos(Context context){
		
		List<AppInfo> list = new ArrayList<AppInfo>();
		PackageManager packageManager = context.getPackageManager();
		List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
		for (PackageInfo packageInfo : installedPackages) {
			AppInfo appInfo = new AppInfo();
			Drawable drawable = packageInfo.applicationInfo.loadIcon(packageManager);
			
			String apkName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
			
			String packageName = packageInfo.packageName;
			
			String sourceDir = packageInfo.applicationInfo.sourceDir;
			
			appInfo.setApkPath(sourceDir);
			
			File file = new File(sourceDir);
			
			
			long apkSize = file.length();
			
			appInfo.setApkName(apkName);
			appInfo.setApkPackageName(packageName);
			appInfo.setApkSize(apkSize);
			appInfo.setIcon(drawable);
			
			int flags = packageInfo.applicationInfo.flags;
			
			if((flags & ApplicationInfo.FLAG_SYSTEM) != 0){
				appInfo.setUserApp(false);
			}else{
				appInfo.setUserApp(true);
			}
			
			if ((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
				appInfo.setRom(false);
			}else{
				appInfo.setRom(true);
			}
			list.add(appInfo);
		}
		return list;
	}
}
