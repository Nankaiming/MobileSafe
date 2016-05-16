package com.example.mobilesafe.bean;

import android.graphics.drawable.Drawable;

public class AppInfo {
	private String apkPath;
	
	public String getApkPath() {
		return apkPath;
	}


	public void setApkPath(String apkPath) {
		this.apkPath = apkPath;
	}


	private Drawable icon;
	
	private String apkName;
	
	
	private long apkSize;
	
	
	private String apkPackageName;
	
	private boolean userApp;
	


	private boolean isRom;


	
	public boolean isUserApp() {
		return userApp;
	}
	
	
	public void setUserApp(boolean userApp) {
		this.userApp = userApp;
	}
	@Override
	public String toString() {
		return "AppInfo [icon=" + icon + ", apkName=" + apkName + ", apkSize="
				+ apkSize + ", apkPackageName=" + apkPackageName + ", userApp="
				+ userApp + ", isRom=" + isRom + "]";
	}


	public boolean isRom() {
		return isRom;
	}


	public void setRom(boolean isRom) {
		this.isRom = isRom;
	}


	public Drawable getIcon() {
		return icon;
	}


	public void setIcon(Drawable icon) {
		this.icon = icon;
	}




	public String getApkName() {
		return apkName;
	}


	public void setApkName(String apkName) {
		this.apkName = apkName;
	}


	public long getApkSize() {
		return apkSize;
	}


	public void setApkSize(long apkSize) {
		this.apkSize = apkSize;
	}


	public String getApkPackageName() {
		return apkPackageName;
	}


	public void setApkPackageName(String apkPackageName) {
		this.apkPackageName = apkPackageName;
	}


}
