package com.example.mobilesafe.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilesafe.R;
import com.example.mobilesafe.utils.StreamUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class SplashActivity extends Activity {

	private static final int CODE_UPDATE_DIALOG = 0;

	private static final int CODE_URL_ERROR = 1;

	private static final int CODE_NET_ERROR = 2;

	private static final int CODE_JSON_ERROR = 3;

	protected static final int CODE_ENTER_HOME = 4;

	private TextView tvVersion;

	// 服务器获取的信息
	private String mVersionName;
	private int mVersionCode;
	private String mDescription;
	private String mDownUrl;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CODE_UPDATE_DIALOG:
				showUpdateDialog();
				break;
			case CODE_URL_ERROR:
				Toast.makeText(SplashActivity.this, "url错误", 0).show();
				entryHome();
				break;
			case CODE_NET_ERROR:
				Toast.makeText(SplashActivity.this, "网络错误", 0).show();
				entryHome();
				break;
			case CODE_JSON_ERROR:
				Toast.makeText(SplashActivity.this, "数据解析错误", 0).show();
				entryHome();
				break;
			case CODE_ENTER_HOME:
				entryHome();
				break;
			default:
				break;
			}
		};
	};

	private TextView tvProgress;

	private RelativeLayout rtRoot;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		tvVersion = (TextView) findViewById(R.id.tv_version);
		tvVersion.setText("版本号:" + getVersionName());
		tvProgress = (TextView) findViewById(R.id.tv_progress);
		rtRoot = (RelativeLayout) findViewById(R.id.rt_root);

		SharedPreferences mPrefs = getSharedPreferences("config", MODE_PRIVATE);

		copyDB("address.db");

		boolean autoUpdate = mPrefs.getBoolean("auto_update", true);
		if (autoUpdate) {
			checkVersion();
		} else {
			handler.sendEmptyMessageDelayed(CODE_ENTER_HOME, 2000);
		}
		AlphaAnimation alpha = new AlphaAnimation(0.3f, 1f);
		alpha.setDuration(2000);
		rtRoot.startAnimation(alpha);

	}

	/**
	 * 获取当前版本名
	 * 
	 * @return
	 */
	private String getVersionName() {
		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);

			int versionCode = packageInfo.versionCode;
			String versionName = packageInfo.versionName;

			return versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取当前版本号
	 * 
	 * @return
	 */
	private int getVersionCode() {
		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);

			int versionCode = packageInfo.versionCode;
			String versionName = packageInfo.versionName;

			return versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 检查是否有更新版本
	 * 
	 */
	private void checkVersion() {
		// TODO Auto-generated method stub
		final long startTime = System.currentTimeMillis();
		new Thread(new Runnable() {
			public void run() {
				Message msg = handler.obtainMessage();
				HttpURLConnection conn = null;
				try {
					URL url = new URL("http://10.0.2.2:8080/update.json");
					conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(5000);
					conn.setReadTimeout(5000);
					conn.connect();

					int responseCode = conn.getResponseCode();

					if (responseCode == 200) {
						InputStream inputStream = conn.getInputStream();
						String result = StreamUtil.readFromStream(inputStream);
						JSONObject json = new JSONObject(result);
						mVersionName = json.getString("versionName");
						mVersionCode = json.getInt("versionCode");
						mDescription = json.getString("description");
						mDownUrl = json.getString("downloadUrl");

						if (mVersionCode > getVersionCode()) {
							msg.what = CODE_UPDATE_DIALOG;
						} else {
							msg.what = CODE_ENTER_HOME;
						}
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					msg.what = CODE_URL_ERROR;
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					msg.what = CODE_NET_ERROR;
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					msg.what = CODE_JSON_ERROR;
					e.printStackTrace();
				} finally {
					long endTime = System.currentTimeMillis();
					long timeUsed = endTime - startTime;
					if (timeUsed < 2000) {
						try {
							Thread.sleep(2000 - timeUsed);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					handler.sendMessage(msg);
					if (conn != null) {
						conn.disconnect();
					}
				}
			}
		}).start();

	}

	private void showUpdateDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("最新版本:" + mVersionName);
		builder.setMessage(mDescription);
		builder.setPositiveButton("立即更新", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				download();
			}
		});
		builder.setNegativeButton("以后再说", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				entryHome();
			}
		});
		builder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				entryHome();
			}
		});
		builder.show();
	}

	private void entryHome() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		finish();
	}

	private void download() {
		// TODO Auto-generated method stub
		tvProgress.setVisibility(View.VISIBLE);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			String target = Environment.getExternalStorageDirectory()
					+ "/update.apk";
			HttpUtils utils = new HttpUtils();
			utils.download(mDownUrl, target, new RequestCallBack<File>() {
				@Override
				public void onLoading(long total, long current,
						boolean isUploading) {
					// TODO Auto-generated method stub
					super.onLoading(total, current, isUploading);
					tvProgress.setText("下载进度" + (current * 100 / total) + "%");
				}

				@Override
				public void onSuccess(ResponseInfo<File> arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					intent.setDataAndType(Uri.fromFile(arg0.result),
							"application/vnd.android.package-archive");
					startActivityForResult(intent, 0);
				}

				@Override
				public void onFailure(HttpException arg0, String arg1) {
					// TODO Auto-generated method stub
					Toast.makeText(SplashActivity.this, "下载失败", 0).show();
				}
			});
		} else {
			Toast.makeText(SplashActivity.this, "没有sdcard", 0).show();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		entryHome();
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void copyDB(String dbName) {
		File destFile = new File(getFilesDir(), dbName);
		if(destFile.exists()){
			return;
		}
		InputStream in = null;
		FileOutputStream out = null;
		try {
			in = getAssets().open(dbName);
			out = new FileOutputStream(destFile);
			int len = 0;
			byte[] buffer = new byte[1024];
			while((len = in.read(buffer)) != -1){
				out.write(buffer, 0, len);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try{
				in.close();
				out.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
