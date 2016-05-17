package com.example.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.example.mobilesafe.R;
import com.example.mobilesafe.service.KillProcessService;
import com.example.mobilesafe.utils.ServiceStatusUtils;

public class TaskManagerSettingActivity extends Activity {
	private CheckBox cb_status;
	private CheckBox cb_status_kill_process;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initUI();
	}

	private void initUI() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_task_manager_setting);
		cb_status = (CheckBox) findViewById(R.id.cb_status);
		final SharedPreferences sp = getSharedPreferences("config",
				Activity.MODE_PRIVATE);
		boolean isShowSystem = sp.getBoolean("is_show_system", false);
		cb_status.setChecked(isShowSystem);
		// 是否显示系统进程的checkbox监听器
		cb_status.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				sp.edit().putBoolean("is_show_system", isChecked).commit();
			}
		});
		cb_status_kill_process = (CheckBox) findViewById(R.id.cb_status_kill_process);

		final Intent intent = new Intent(this, KillProcessService.class);
		cb_status_kill_process
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							startService(intent);
						} else {
							stopService(intent);
						}

					}
				});
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		boolean serviceRunning = ServiceStatusUtils.isServiceRunning(this,
				"com.example.mobilesafe.service.KillProcessService");
		cb_status_kill_process.setChecked(serviceRunning);
		super.onStart();
	}
}
