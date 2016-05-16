package com.example.mobilesafe.bean;

import android.graphics.drawable.Drawable;

public class TaskInfo {
	private String processName;
	private Drawable icon;
	private String appName;
	private long memorySize;
	
	
	private boolean userApp;
	
	private boolean checked;
	
	public boolean isChecked() {
		return checked;
	}


	public void setChecked(boolean checked) {
		this.checked = checked;
	}


	@Override
	public String toString() {
		return "TaskInfo [processName=" + processName + ", icon=" + icon
				+ ", appName=" + appName + ", memorySize=" + memorySize
				+ ", userApp=" + userApp + "]";
	}


	public String getProcessName() {
		return processName;
	}


	public void setProcessName(String processName) {
		this.processName = processName;
	}


	public Drawable getIcon() {
		return icon;
	}


	public void setIcon(Drawable icon) {
		this.icon = icon;
	}


	public String getAppName() {
		return appName;
	}


	public void setAppName(String appName) {
		this.appName = appName;
	}


	public long getMemorySize() {
		return memorySize;
	}


	public void setMemorySize(long memorySize) {
		this.memorySize = memorySize;
	}


	public boolean isUserApp() {
		return userApp;
	}


	public void setUserApp(boolean userApp) {
		this.userApp = userApp;
	}


	//判断是否是用户进程
}
