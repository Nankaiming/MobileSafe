package com.example.mobilesafe.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.mobilesafe.bean.TaskInfo;
import com.example.mobilesafe.engine.TaskInfoParser;
import com.example.mobilesafe.utils.ServiceStatusUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class TaskManagerActivity extends Activity {
	@ViewInject(R.id.tv_task_process_count)
	private TextView tv_task_process_count;
	@ViewInject(R.id.tv_task_memory)
	private TextView tv_task_memory;
	@ViewInject(R.id.list_view)
	private ListView list_view;
	private List<TaskInfo> taskInfos;
	private List<TaskInfo> userTaskInfos;
	private List<TaskInfo> systemAppInfos;
	
	private TaskManagerAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initUI();
		initData();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(adapter != null){
			adapter.notifyDataSetChanged();
		}
	}

	class TaskManagerAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			/**
			 * 判断当前用户是否需要展示系统进程 如果需要就全部展示 如果不需要就展示用户进程
			 */
				return userTaskInfos.size() + 1 + systemAppInfos.size() + 1;

		}

		@Override
		public Object getItem(int position) {

			if (position == 0) {
				return null;
			} else if (position == userTaskInfos.size() + 1) {
				return null;
			}

			TaskInfo taskInfo;

			if (position < (userTaskInfos.size() + 1)) {
				// 用户程序
				taskInfo = userTaskInfos.get(position - 1);// 多了一个textview的标签 ，
															// 位置需要-1
			} else {
				// 系统程序
				int location = position - 1 - userTaskInfos.size() - 1;
				taskInfo = systemAppInfos.get(location);
			}
			return taskInfo;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (position == 0) {
				// 第0个位置显示的应该是 用户程序的个数的标签。
				TextView tv = new TextView(getApplicationContext());
				tv.setBackgroundColor(Color.GRAY);
				tv.setTextColor(Color.WHITE);
				tv.setText("用户程序：" + userTaskInfos.size() + "个");
				return tv;
			} else if (position == (userTaskInfos.size() + 1)) {
				TextView tv = new TextView(getApplicationContext());
				tv.setBackgroundColor(Color.GRAY);
				tv.setTextColor(Color.WHITE);
				tv.setText("系统程序：" + systemAppInfos.size() + "个");
				return tv;
			}
			ViewHolder holder;
			View view;
			if (convertView != null && convertView instanceof LinearLayout) {
				view = convertView;

				holder = (ViewHolder) view.getTag();

			} else {
				view = View.inflate(TaskManagerActivity.this,
						R.layout.item_task_manager, null);

				holder = new ViewHolder();

				holder.iv_app_icon = (ImageView) view
						.findViewById(R.id.iv_app_icon);

				holder.tv_app_name = (TextView) view
						.findViewById(R.id.tv_app_name);

				holder.tv_app_memory_size = (TextView) view
						.findViewById(R.id.tv_app_memory_size);

				holder.tv_app_status = (CheckBox) view
						.findViewById(R.id.tv_app_status);

				view.setTag(holder);
			}

			TaskInfo taskInfo;

			if (position < (userTaskInfos.size() + 1)) {
				// 用户程序
				taskInfo = userTaskInfos.get(position - 1);// 多了一个textview的标签 ，
															// 位置需要-1
			} else {
				// 系统程序
				int location = position - 1 - userTaskInfos.size() - 1;
				taskInfo = systemAppInfos.get(location);
			}
			// 这个是设置图片控件的大小
			// holder.iv_app_icon.setBackgroundDrawable(d)
			// 设置图片本身的大小
			holder.iv_app_icon.setImageDrawable(taskInfo.getIcon());

			holder.tv_app_name.setText(taskInfo.getAppName());

			holder.tv_app_memory_size.setText("内存占用:"
					+ Formatter.formatFileSize(TaskManagerActivity.this,
							taskInfo.getMemorySize()));

			if (taskInfo.isChecked()) {
				holder.tv_app_status.setChecked(true);
			} else {
				holder.tv_app_status.setChecked(false);
			}
			// 判断当前展示的item是否是自己的程序。如果是。就把程序给隐藏
			if (taskInfo.getProcessName().equals(getPackageName())) {
				// 隐藏
				holder.tv_app_status.setVisibility(View.INVISIBLE);
			} else {
				// 显示
				holder.tv_app_status.setVisibility(View.VISIBLE);
			}

			return view;
		}

	}
	static class ViewHolder {
		ImageView iv_app_icon;
		TextView tv_app_name;
		TextView tv_app_memory_size;
		CheckBox tv_app_status;
	}

	private void initData() {
		// TODO Auto-generated method stub
		new Thread() {

			

			public void run() {

				taskInfos = TaskInfoParser
						.getTaskInfos(TaskManagerActivity.this);
				userTaskInfos = new ArrayList<TaskInfo>();
				systemAppInfos = new ArrayList<TaskInfo>();
				for (TaskInfo taskInfo : taskInfos) {
					if(taskInfo.isUserApp()){
						userTaskInfos.add(taskInfo);
					}else{
						systemAppInfos.add(taskInfo);
					}
				}
				runOnUiThread(new Runnable() {
					

					public void run() {

						adapter = new TaskManagerAdapter();
						list_view.setAdapter(adapter);
					}
				});
			};
		}.start();
	}

	private void initUI() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_task_manager);
		ViewUtils.inject(this);
		// 获取进程个数
		int processCount = ServiceStatusUtils.getProcessCount(this);
		tv_task_process_count.setText("进程:" + processCount + "个");
		// 获取进程可用内存
		long availMem = ServiceStatusUtils.getAvailMem(this);
		// 获取总内存
		long totalMem = ServiceStatusUtils.getTotalMem(this);
		tv_task_memory.setText("剩余/总内存:"
				+ Formatter.formatFileSize(TaskManagerActivity.this, availMem)
				+ "/"
				+ Formatter.formatFileSize(TaskManagerActivity.this, totalMem));
		
		list_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Object object = list_view.getItemAtPosition(position);

				if (object != null && object instanceof TaskInfo) {

					TaskInfo taskInfo = (TaskInfo) object;

					ViewHolder holder = (ViewHolder) view.getTag();
					
					if(taskInfo.getProcessName().equals(getPackageName())){
						return;
					}
					
					// 判断当前的item是否被勾选上
					/**
					 * 如果被勾选上了。那么就改成没有勾选。 如果没有勾选。就改成已经勾选
					 */
					if (taskInfo.isChecked()) {
						taskInfo.setChecked(false);
						holder.tv_app_status.setChecked(false);
					} else {
						taskInfo.setChecked(true);
						holder.tv_app_status.setChecked(true);
					}

				}
			}
		});
	}
	public void selectAll(View view){
		for (TaskInfo taskInfo : userTaskInfos) {
			if(taskInfo.getProcessName().equals(getPackageName())){
				continue;
			}
			taskInfo.setChecked(true);
		}
		for (TaskInfo taskInfo : systemAppInfos) {
			taskInfo.setChecked(true);
		}
		adapter.notifyDataSetChanged();
	}
	public void selectOppsite(View view){
		for (TaskInfo taskInfo : userTaskInfos) {
			// 判断当前的用户程序是不是自己的程序。如果是自己的程序。那么就把文本框隐藏

			if (taskInfo.getProcessName().equals(getPackageName())) {
				continue;
			}
			taskInfo.setChecked(!taskInfo.isChecked());
		}
		for (TaskInfo taskInfo : systemAppInfos) {
			taskInfo.setChecked(!taskInfo.isChecked());
		}
		adapter.notifyDataSetChanged();
	}
	public void killProcess(View view){
		
	}
	public void openSetting(View view){
		
	}
}
