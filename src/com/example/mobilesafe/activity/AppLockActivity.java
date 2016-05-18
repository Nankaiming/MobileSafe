package com.example.mobilesafe.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.mobilesafe.fragment.LockFragment;
import com.example.mobilesafe.fragment.UnLockFragment;

public class AppLockActivity extends FragmentActivity implements OnClickListener{
	private FrameLayout fl_content;
	private TextView tv_unlock;
	private TextView tv_lock;
	private FragmentManager fragmentManager;
	private UnLockFragment unLockFragment;
	private LockFragment lockFragment;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);

		initUI();
	}

	private void initUI() {
		setContentView(R.layout.activity_app_lock);

		fl_content = (FrameLayout) findViewById(R.id.fl_content);

		tv_unlock = (TextView) findViewById(R.id.tv_unlock);

		tv_lock = (TextView) findViewById(R.id.tv_lock);

		tv_unlock.setOnClickListener(this);
		tv_lock.setOnClickListener(this);
		//��ȡ��fragment�Ĺ�����
		
		fragmentManager = getSupportFragmentManager();
		//��������
		FragmentTransaction mTransaction = fragmentManager.beginTransaction();

		unLockFragment = new UnLockFragment();
		
		lockFragment = new LockFragment();
		/**
		 * �滻����
		 * 1 ��Ҫ�滻�Ľ����id
		 * 2����ָĳһ��fragment�Ķ���
		 */
		mTransaction.replace(R.id.fl_content, unLockFragment).commit();
		
	
		
	}

	@Override
	public void onClick(View v) {
		
		FragmentTransaction ft = fragmentManager.beginTransaction();
		switch (v.getId()) {
		case R.id.tv_unlock:
            //û�м���
			tv_unlock.setBackgroundResource(R.drawable.tab_left_pressed);
			tv_lock.setBackgroundResource(R.drawable.tab_right_default);
			
			ft.replace(R.id.fl_content, unLockFragment);
			System.out.println("�л���lockFragment");
			break;

		case R.id.tv_lock:
			//û�м���
			tv_unlock.setBackgroundResource(R.drawable.tab_left_default);
			tv_lock.setBackgroundResource(R.drawable.tab_right_pressed);
			
			ft.replace(R.id.fl_content, lockFragment);
			System.out.println("�л���unlockFragment");
			break;
		}
		ft.commit();
	}
}