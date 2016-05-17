package com.example.mobilesafe.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.mobilesafe.utils.ToastUtils;
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
	private int processCount;
	private long availMem;
	private long totalMem;

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
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}

	class TaskManagerAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			/**
			 * �жϵ�ǰ�û��Ƿ���Ҫչʾϵͳ���� �����Ҫ��ȫ��չʾ �������Ҫ��չʾ�û�����
			 */
			SharedPreferences sp = getSharedPreferences("config",
					Activity.MODE_PRIVATE);
			boolean result = sp.getBoolean("is_show_system", false);
			if (result) {

				return userTaskInfos.size() + 1 + systemAppInfos.size() + 1;
			} else {
				return userTaskInfos.size() + 1;
			}

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
				// �û�����
				taskInfo = userTaskInfos.get(position - 1);// ����һ��textview�ı�ǩ ��
															// λ����Ҫ-1
			} else {
				// ϵͳ����
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
				// ��0��λ����ʾ��Ӧ���� �û�����ĸ����ı�ǩ��
				TextView tv = new TextView(getApplicationContext());
				tv.setBackgroundColor(Color.GRAY);
				tv.setTextColor(Color.WHITE);
				tv.setText("�û�����" + userTaskInfos.size() + "��");
				return tv;
			} else if (position == (userTaskInfos.size() + 1)) {
				TextView tv = new TextView(getApplicationContext());
				tv.setBackgroundColor(Color.GRAY);
				tv.setTextColor(Color.WHITE);
				tv.setText("ϵͳ����" + systemAppInfos.size() + "��");
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
				// �û�����
				taskInfo = userTaskInfos.get(position - 1);// ����һ��textview�ı�ǩ ��
															// λ����Ҫ-1
			} else {
				// ϵͳ����
				int location = position - 1 - userTaskInfos.size() - 1;
				taskInfo = systemAppInfos.get(location);
			}
			// ���������ͼƬ�ؼ��Ĵ�С
			// holder.iv_app_icon.setBackgroundDrawable(d)
			// ����ͼƬ����Ĵ�С
			holder.iv_app_icon.setImageDrawable(taskInfo.getIcon());

			holder.tv_app_name.setText(taskInfo.getAppName());

			holder.tv_app_memory_size.setText("�ڴ�ռ��:"
					+ Formatter.formatFileSize(TaskManagerActivity.this,
							taskInfo.getMemorySize()));

			if (taskInfo.isChecked()) {
				holder.tv_app_status.setChecked(true);
			} else {
				holder.tv_app_status.setChecked(false);
			}
			// �жϵ�ǰչʾ��item�Ƿ����Լ��ĳ�������ǡ��Ͱѳ��������
			if (taskInfo.getProcessName().equals(getPackageName())) {
				// ����
				holder.tv_app_status.setVisibility(View.INVISIBLE);
			} else {
				// ��ʾ
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
					if (taskInfo.isUserApp()) {
						userTaskInfos.add(taskInfo);
					} else {
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
		processCount = ServiceStatusUtils.getProcessCount(this);
		tv_task_process_count.setText("����:" + processCount + "��");
		availMem = ServiceStatusUtils.getAvailMem(this);
		totalMem = ServiceStatusUtils.getTotalMem(this);
		tv_task_memory.setText("ʣ��/���ڴ�:"
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

					if (taskInfo.getProcessName().equals(getPackageName())) {
						return;
					}

					// �жϵ�ǰ��item�Ƿ񱻹�ѡ��
					/**
					 * �������ѡ���ˡ���ô�͸ĳ�û�й�ѡ�� ���û�й�ѡ���͸ĳ��Ѿ���ѡ
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

	public void selectAll(View view) {
		for (TaskInfo taskInfo : userTaskInfos) {
			if (taskInfo.getProcessName().equals(getPackageName())) {
				continue;
			}
			taskInfo.setChecked(true);
		}
		for (TaskInfo taskInfo : systemAppInfos) {
			taskInfo.setChecked(true);
		}
		adapter.notifyDataSetChanged();
	}

	public void selectOppsite(View view) {
		for (TaskInfo taskInfo : userTaskInfos) {
			// �жϵ�ǰ���û������ǲ����Լ��ĳ���������Լ��ĳ�����ô�Ͱ��ı�������

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

	public void killProcess(View view) {
		ActivityManager am = (ActivityManager) getSystemService(Activity.ACTIVITY_SERVICE);
		List<TaskInfo> list = new ArrayList<TaskInfo>();
		// �ͷŵĽ��̸���
		int totalCount = 0;
		// �ͷŵĽ����ڴ�
		long killMem = 0;
		// �����ټ��ϵ����Ǹı伯�ϴ�С����ɾ������
		for (TaskInfo taskInfo : userTaskInfos) {
			if (taskInfo.isChecked()) {
				// ��������û�����
				totalCount++;
				killMem += taskInfo.getMemorySize();
				list.add(taskInfo);
			}
		}
		for (TaskInfo taskInfo : systemAppInfos) {
			if (taskInfo.isChecked()) {
				// �������ϵͳ����
				totalCount++;
				killMem += taskInfo.getMemorySize();
				list.add(taskInfo);
			}
		}
		for (TaskInfo taskInfo : list) {
			if (taskInfo.isUserApp()) {
				am.killBackgroundProcesses(taskInfo.getProcessName());
				userTaskInfos.remove(taskInfo);
			} else {
				am.killBackgroundProcesses(taskInfo.getProcessName());
				systemAppInfos.remove(taskInfo);
			}
		}
		ToastUtils.showToast(
				this,
				"������"
						+ totalCount
						+ "������,�ͷ�"
						+ Formatter.formatFileSize(TaskManagerActivity.this,
								killMem) + "�ڴ�");
		processCount -= totalCount;
		tv_task_process_count.setText("����:" + processCount + "��");
		tv_task_memory.setText("ʣ��/���ڴ�:"
				+ Formatter.formatFileSize(TaskManagerActivity.this, availMem
						+ killMem) + "/"
				+ Formatter.formatFileSize(TaskManagerActivity.this, totalMem));
		adapter.notifyDataSetChanged();

	}

	public void openSetting(View view) {
		Intent intent = new Intent(this, TaskManagerSettingActivity.class);
		startActivity(intent);
	}
}
