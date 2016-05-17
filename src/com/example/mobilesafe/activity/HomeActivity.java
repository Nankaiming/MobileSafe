package com.example.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilesafe.R;
import com.example.mobilesafe.utils.MD5Utils;

public class HomeActivity extends Activity {
	private GridView gvHome;

	private String[] mItem = { "手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计", "手机杀毒",
			"缓存清理", "高级工具", "设置中心" };

	private int[] mPic = { R.drawable.home_safe, R.drawable.home_callmsgsafe,
			R.drawable.home_apps, R.drawable.home_taskmanager,
			R.drawable.home_netmanager, R.drawable.home_trojan,
			R.drawable.home_sysoptimize, R.drawable.home_tools,
			R.drawable.home_settings };

	private Button btnOk;

	private Button btnCancel;

	private EditText et_password;

	private EditText et_passwordConfirm;

	private SharedPreferences mPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		gvHome = (GridView) findViewById(R.id.gv_home);
		gvHome.setAdapter(new HomeAdapter());
		gvHome.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				// 手机防盗
				case 0:
					showPasswordDialog();
					break;
					// 通信卫士
				case 1:
					startActivity(new Intent(HomeActivity.this,CallSafeActivity2.class));
					break;
					// 软件管理
				case 2:
					startActivity(new Intent(HomeActivity.this,AppManagerActivity.class));
					break;
					// 进程管理
				case 3:
					startActivity(new Intent(HomeActivity.this,TaskManagerActivity.class));
					break;
					
				case 5:
					startActivity(new Intent(HomeActivity.this,AntivirusActivity.class));
					break;
					
				// 高级工具
				case 7:
					startActivity(new Intent(HomeActivity.this,
							AtoolsActivity.class));
					break;
					// 设置中心
				case 8:
					startActivity(new Intent(HomeActivity.this,
							SettingActivity.class));
					break;

				default:
					break;
				}
			}
		});
	}

	private void showPasswordDialog() {
		// TODO Auto-generated method stub
		mPrefs = getSharedPreferences("config", MODE_PRIVATE);
		String savedpassword = mPrefs.getString("password", null);
		if (!TextUtils.isEmpty(savedpassword)) {
			showInputDialog();
		} else {
			showPasswordSetDialog();
		}

	}

	private void showInputDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(this, R.layout.dialog_input_password, null);
		et_password = (EditText) view.findViewById(R.id.password);
		et_passwordConfirm = (EditText) view
				.findViewById(R.id.password_confirm);
		mPrefs = getSharedPreferences("config", MODE_PRIVATE);

		btnOk = (Button) view.findViewById(R.id.btn_ok);
		btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String password = et_password.getText().toString();
				String savedPassword = mPrefs.getString("password", null);
				if (!TextUtils.isEmpty(password)) {
					if (MD5Utils.encode(password).equals(savedPassword)) {
						// Toast.makeText(HomeActivity.this, "登录成功", 0).show();
						dialog.dismiss();
						startActivity(new Intent(HomeActivity.this,
								LostFindActivity.class));
					} else {
						Toast.makeText(HomeActivity.this, "密码错误",
								Toast.LENGTH_SHORT).show();

					}
				} else {
					Toast.makeText(HomeActivity.this, "输入框不能为空",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
	}

	private void showPasswordSetDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(this, R.layout.dialog_set_password, null);
		et_password = (EditText) view.findViewById(R.id.password);
		et_passwordConfirm = (EditText) view
				.findViewById(R.id.password_confirm);
		mPrefs = getSharedPreferences("config", MODE_PRIVATE);

		btnOk = (Button) view.findViewById(R.id.btn_ok);
		btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String password = et_password.getText().toString();
				String passwordConfirm = et_passwordConfirm.getText()
						.toString();
				if ((!TextUtils.isEmpty(password))
						&& (!TextUtils.isEmpty(passwordConfirm))) {
					if (password.equals(passwordConfirm)) {
						// Toast.makeText(HomeActivity.this, "登录成功", 0).show();
						mPrefs.edit()
								.putString("password",
										MD5Utils.encode(password)).commit();
						dialog.dismiss();
						startActivity(new Intent(HomeActivity.this,
								LostFindActivity.class));
					} else {
						Toast.makeText(HomeActivity.this, "密码不一致",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(HomeActivity.this, "输入框不能为空",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();

	}

	class HomeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mItem.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mItem[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			View view = View.inflate(HomeActivity.this,
					R.layout.home_list_item, null);
			ImageView ivItem = (ImageView) view.findViewById(R.id.iv_item);
			TextView tvItem = (TextView) view.findViewById(R.id.tv_item);

			ivItem.setImageResource(mPic[position]);
			tvItem.setText(mItem[position]);

			return view;
		}

	}
}
