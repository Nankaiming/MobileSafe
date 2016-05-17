package com.example.mobilesafe.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilesafe.R;
import com.example.mobilesafe.bean.AppInfo;
import com.example.mobilesafe.engine.AppInfos;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.stericson.RootShell.RootShell;
import com.stericson.RootTools.RootTools;

@SuppressLint("NewApi")
public class AppManagerActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.list_view)
	private ListView listView;
	@ViewInject(R.id.tv_sd)
	private TextView tv_sd;
	@ViewInject(R.id.tv_rom)
	private TextView tv_rom;
	@ViewInject(R.id.tv_app)
	private TextView tv_app;

	private List<AppInfo> appInfos;
	private List<AppInfo> userAppInfos;
	private List<AppInfo> systemAppInfos;

	private PopupWindow popupWindow;

	private AppInfo clickAppInfo;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initUI();
		initData();
		receiver = new UninstallReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		intentFilter.addDataScheme("package");
		registerReceiver(receiver, intentFilter);
	}
	class UninstallReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			initData();
		}
		
	}
	class AppManagerAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return userAppInfos.size() + 1 + systemAppInfos.size() + 1;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (position == 0) {
				return null;
			} else if (position == userAppInfos.size() + 1) {
				return null;
			}
			AppInfo appInfo;

			if (position < userAppInfos.size() + 1) {
				// 把多出来的特殊的条目减掉
				appInfo = userAppInfos.get(position - 1);

			} else {

				int location = userAppInfos.size() + 2;

				appInfo = systemAppInfos.get(position - location);
			}

			return appInfo;
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
			ViewHolder viewHolder;
			if (position == 0) {
				TextView textView = new TextView(AppManagerActivity.this);
				textView.setTextColor(Color.WHITE);
				textView.setBackgroundColor(Color.GRAY);
				textView.setText("用户程序(" + userAppInfos.size() + ")");
				return textView;
			} else if (position == userAppInfos.size() + 1) {
				TextView textView = new TextView(AppManagerActivity.this);
				textView.setTextColor(Color.WHITE);
				textView.setBackgroundColor(Color.GRAY);
				textView.setText("系统程序(" + systemAppInfos.size() + ")");
				return textView;
			}

			AppInfo appInfo;

			if (position < userAppInfos.size() + 1) {
				// 把多出来的特殊的条目减掉
				appInfo = userAppInfos.get(position - 1);

			} else {

				int location = userAppInfos.size() + 2;

				appInfo = systemAppInfos.get(position - location);
			}

			if (convertView != null && convertView instanceof LinearLayout) {
				view = convertView;
				viewHolder = (ViewHolder) view.getTag();
			} else {
				viewHolder = new ViewHolder();
				view = View.inflate(AppManagerActivity.this,
						R.layout.item_app_manager, null);
				viewHolder.iv_icon = (ImageView) view
						.findViewById(R.id.iv_icon);
				viewHolder.tv_name = (TextView) view
						.findViewById(R.id.tv_app_name);
				viewHolder.tv_location = (TextView) view
						.findViewById(R.id.tv_app_location);
				viewHolder.tv_apk_size = (TextView) view
						.findViewById(R.id.tv_app_size);
				view.setTag(viewHolder);
			}

			viewHolder.iv_icon.setImageDrawable(appInfo.getIcon());
			viewHolder.tv_name.setText(appInfo.getApkName());
			viewHolder.tv_apk_size.setText(Formatter.formatFileSize(
					AppManagerActivity.this, appInfo.getApkSize()));
			if (appInfo.isRom()) {
				viewHolder.tv_location.setText("手机内存");
			} else {
				viewHolder.tv_location.setText("外部存储");
			}
			return view;
		}

	}

	static class ViewHolder {
		ImageView iv_icon;
		TextView tv_apk_size;
		TextView tv_location;
		TextView tv_name;
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			AppManagerAdapter adapter = new AppManagerAdapter();
			listView.setAdapter(adapter);
		};
	};
	private UninstallReceiver receiver;

	private void initData() {
		// TODO Auto-generated method stub
		new Thread() {

			@Override
			public void run() {
				appInfos = AppInfos.getAppInfos(AppManagerActivity.this);
				userAppInfos = new ArrayList<AppInfo>();
				systemAppInfos = new ArrayList<AppInfo>();
				for (AppInfo appInfo : appInfos) {
					if (appInfo.isUserApp()) {
						userAppInfos.add(appInfo);
					} else {
						systemAppInfos.add(appInfo);
					}
				}
				handler.sendEmptyMessage(0);
			}
		}.start();
	}

	@SuppressLint("NewApi")
	private void initUI() {
		setContentView(R.layout.activity_app_manager);
		ViewUtils.inject(this);
		// 获取到rom内存的的剩余空间
		long rom_freeSpace = Environment.getDataDirectory().getFreeSpace();
		// 获取到SD卡的剩余空间
		long sd_freeSpace = Environment.getExternalStorageDirectory()
				.getFreeSpace();

		tv_rom.setText("内存可用" + Formatter.formatFileSize(this, rom_freeSpace));
		tv_sd.setText("sd卡可用" + Formatter.formatFileSize(this, sd_freeSpace));

		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				popupWindowDismiss();
				if (userAppInfos != null && systemAppInfos != null) {

					if (firstVisibleItem > (userAppInfos.size() + 1)) {
						// 系统应用程序
						tv_app.setText("系统程序(" + systemAppInfos.size() + ")个");
					} else {
						// 用户应用程序
						tv_app.setText("用户程序(" + userAppInfos.size() + ")个");
					}
				}
			}

		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Object obj = listView.getItemAtPosition(position);
				if (obj != null && obj instanceof AppInfo) {
					clickAppInfo = (AppInfo) obj;
					popupWindowDismiss();
					View convertView = View.inflate(AppManagerActivity.this,
							R.layout.item_popup, null);
					LinearLayout ll_uninstall = (LinearLayout) convertView
							.findViewById(R.id.ll_uninstall);
					LinearLayout ll_start = (LinearLayout) convertView
							.findViewById(R.id.ll_start);
					LinearLayout ll_share = (LinearLayout) convertView
							.findViewById(R.id.ll_share);
					LinearLayout ll_detail = (LinearLayout) convertView
							.findViewById(R.id.ll_detail);

					ll_uninstall.setOnClickListener(AppManagerActivity.this);
					ll_start.setOnClickListener(AppManagerActivity.this);
					ll_share.setOnClickListener(AppManagerActivity.this);
					ll_detail.setOnClickListener(AppManagerActivity.this);

					popupWindow = new PopupWindow(convertView, -2, -2);
					// popupwindow 必须设置背景才能显示动画
					popupWindow.setBackgroundDrawable(new ColorDrawable(
							Color.TRANSPARENT));
					int[] location = new int[2];
					view.getLocationInWindow(location);
					popupWindow.showAtLocation(parent, Gravity.LEFT
							+ Gravity.TOP, 80, location[1]);

					ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f,
							1f, 0.5f, 1f, Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 0.5f);
					scaleAnimation.setDuration(3000);
					convertView.startAnimation(scaleAnimation);
				}
			}
		});
	}

	private void popupWindowDismiss() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
			popupWindow = null;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		popupWindowDismiss();
		unregisterReceiver(receiver);
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_uninstall:
			// 用户应用可以直接删除
//			if (clickAppInfo.isUserApp()) {
				Intent uninstall_localIntent = new Intent(
						"android.intent.action.DELETE", Uri.parse("package:"
								+ clickAppInfo.getApkPackageName()));
				startActivity(uninstall_localIntent);
//			} else {
//				if (!RootShell.isRootAvailable()) {
//					Toast.makeText(this, "卸载系统应用，必须要root权限", 0).show();
//					return;
//				}
//				try {
//					if (!RootShell.isAccessGiven()) {
//						Toast.makeText(this, "请授权手机卫士root权限", 0).show();
//						return;
//					}
//					RootTools.getCustomShell("mount -o remount ,rw /system", 3000);
//					RootTools.getCustomShell("rm -r " + clickAppInfo.getApkPath(),
//							30000);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
			popupWindowDismiss();
			break;
		case R.id.ll_start:
			Intent intent = getPackageManager().getLaunchIntentForPackage(
					clickAppInfo.getApkPackageName());
			startActivity(intent);
			popupWindowDismiss();
			break;
		case R.id.ll_share:

			Intent share_localIntent = new Intent("android.intent.action.SEND");
			share_localIntent.setType("text/plain");
			share_localIntent.putExtra("android.intent.extra.SUBJECT", "f分享");
			share_localIntent.putExtra("android.intent.extra.TEXT",
					"Hi！推荐您使用软件：" + clickAppInfo.getApkName() + "下载地址:"
							+ "https://play.google.com/store/apps/details?id="
							+ clickAppInfo.getApkPackageName());
			this.startActivity(Intent.createChooser(share_localIntent, "分享"));
			popupWindowDismiss();
			break;

		case R.id.ll_detail:
			Intent detail_intent = new Intent();
			detail_intent
					.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
			detail_intent.addCategory(Intent.CATEGORY_DEFAULT);
			detail_intent.setData(Uri.parse("package:"
					+ clickAppInfo.getApkPackageName()));
			startActivity(detail_intent);
			popupWindowDismiss();
			break;
		default:
			break;
		}
	}
}
