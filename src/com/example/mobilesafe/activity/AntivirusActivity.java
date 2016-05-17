package com.example.mobilesafe.activity;

import java.util.List;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.mobilesafe.db.dao.AntivirusDao;
import com.example.mobilesafe.utils.MD5Utils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class AntivirusActivity extends Activity {
	protected static final int SCANNING = 0;
	protected static final int FINISHED = 1;
	@ViewInject(R.id.iv_scanning)
	private ImageView iv_scanning;
	@ViewInject(R.id.ll_content)
	private LinearLayout ll_content;
	@ViewInject(R.id.progressBar1)
	private ProgressBar progressBar1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initUI();
		initData();
	}

	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SCANNING:
				ScanInfo scanInfo = (ScanInfo) msg.obj;
				TextView textView = new TextView(AntivirusActivity.this);
				if(scanInfo.desc){
					textView.setText(scanInfo.appName + "ÓÐ²¡¶¾");
					textView.setTextColor(Color.RED);
				}else{
					textView.setText(scanInfo.appName + "É¨Ãè°²È«");
					textView.setTextColor(Color.BLACK);
				}
				ll_content.addView(textView);
				break;
			case FINISHED:
				iv_scanning.clearAnimation();
				break;
			default:
				break;
			}
			
		};
	};
	private void initData() {
		// TODO Auto-generated method stub
		new Thread(){
			private Message message;

			public void run() {
				PackageManager packageManager = getPackageManager();
				List<PackageInfo> packages = packageManager.getInstalledPackages(0);
				
				
				progressBar1.setMax(packages.size());
				int progress = 0;
				
				
				
				for (PackageInfo packageInfo : packages) {
					ScanInfo scanInfo = new ScanInfo();
					String appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
					String packageName = packageInfo.packageName;
					scanInfo.appName = appName;
					scanInfo.packageName = packageName;
					String sourceDir = packageInfo.applicationInfo.sourceDir;
					String md5 = MD5Utils.getFileMd5(sourceDir);
					String desc = AntivirusDao.checkFileVirus(md5);

					System.out.println("-------------------------");
					
					System.out.println(appName);
					
					System.out.println(md5);
					
					
					if(desc == null){
						scanInfo.desc = false;
					}else{
						scanInfo.desc = true;
					}
					progress++;
					progressBar1.setProgress(progress);
					message = Message.obtain();
					message.what = SCANNING;
					message.obj = scanInfo;
					handler.sendMessage(message);
				}
				message = Message.obtain();
				message.what = FINISHED;
				handler.sendMessage(message);
			};
		}.start();
	}
	static class ScanInfo {
		boolean desc;
		String appName;
		String packageName;
	}

	private void initUI() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_antivirus);
		ViewUtils.inject(this);
		
		
		
		RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotateAnimation.setDuration(5000);
		rotateAnimation.setRepeatCount(Animation.INFINITE);
		iv_scanning.startAnimation(rotateAnimation);
	}
}
