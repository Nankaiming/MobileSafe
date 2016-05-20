package com.example.mobilesafe.activity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mobilesafe.R;

public class CleanCacheActivity extends Activity {
	private PackageManager packageManager;
	private ListView list_view;
	private List<CatchInfo> catchInfos;
	private CatchAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clean_cache);

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		initUI();
		initData();

	}

	private void initUI() {

		list_view = (ListView) findViewById(R.id.list_view);

	}

	class CatchAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return catchInfos.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return catchInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view;
			ViewHolder holder = null;
			final CatchInfo catchInfo = catchInfos.get(position);
			if (convertView == null) {
				view = View.inflate(CleanCacheActivity.this,
						R.layout.item_clean_cache, null);
				holder = new ViewHolder();
				holder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
				holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
				holder.tv_cache_size = (TextView) view
						.findViewById(R.id.tv_cache_size);
				holder.iv_clean = (ImageView) view.findViewById(R.id.iv_clean);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}

			holder.iv_icon.setImageDrawable(catchInfo.icon);
			holder.tv_name.setText(catchInfo.appName);
			holder.tv_cache_size.setText(Formatter.formatFileSize(
					CleanCacheActivity.this, catchInfo.catchSize));
			holder.iv_clean.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
					intent.setData(Uri.parse("package:"
							+ catchInfo.packageName));
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					startActivity(intent);
				}
			});
			return view;
		}

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			adapter = new CatchAdapter();
			list_view.setAdapter(adapter);
		};
	};

	static class ViewHolder {
		ImageView iv_icon;
		TextView tv_name;
		TextView tv_cache_size;
		ImageView iv_clean;
	}

	private void initData() {
		packageManager = getPackageManager();
		catchInfos = new ArrayList<CatchInfo>();
		new Thread() {
			public void run() {

				// 安装到手机上面所有的应用程序
				List<PackageInfo> installedPackages = packageManager
						.getInstalledPackages(0);
				for (PackageInfo packageInfo : installedPackages) {
					getCacheSize(packageInfo);
				}
				SystemClock.sleep(5000);
				handler.sendEmptyMessage(0);
			};
		}.start();
	}

	private void getCacheSize(PackageInfo packageInfo) {
		try {
			// 通过反射获取到当前的方法
			Method method = PackageManager.class.getDeclaredMethod(
					"getPackageSizeInfo", String.class,
					IPackageStatsObserver.class);
			method.invoke(packageManager,
					packageInfo.applicationInfo.packageName,
					new MyIpackageStatsObserver(packageInfo));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	static class CatchInfo {
		Drawable icon;
		String appName;
		long catchSize;
		String packageName;
	}

	public void cleanAll(View view) {
		try {
			Method method = PackageManager.class.getDeclaredMethod(
					"freeStorageAndNotify", long.class,
					IPackageDataObserver.class);
			method.invoke(packageManager, Integer.MAX_VALUE,
					new MyIPackageDataObserver());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class MyIPackageDataObserver extends IPackageDataObserver.Stub {

		@Override
		public void onRemoveCompleted(String packageName, boolean succeeded)
				throws RemoteException {
			// TODO Auto-generated method stub
		}

	}

	class MyIpackageStatsObserver extends IPackageStatsObserver.Stub {
		private PackageInfo packageInfo;

		public MyIpackageStatsObserver(PackageInfo packageInfo) {
			this.packageInfo = packageInfo;
		}

		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {
			// TODO Auto-generated method stub
			long catchSize = pStats.cacheSize;
			if (catchSize > 0) {
				CatchInfo catchInfo = new CatchInfo();
				Drawable icon = packageInfo.applicationInfo
						.loadIcon(packageManager);
				catchInfo.icon = icon;
				String appName = packageInfo.applicationInfo.loadLabel(
						packageManager).toString();
				catchInfo.appName = appName;
				catchInfo.catchSize = catchSize;
				String packageName = packageInfo.packageName;
				catchInfo.packageName = packageName;
				catchInfos.add(catchInfo);
			}
		}

	}

	

}
