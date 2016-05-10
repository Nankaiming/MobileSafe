package com.example.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
/**
 * 
 * @author God vision
 *�������û���ṩһ�����õ� SharedPreferences��ʵ�����ƻ�����
 */
public abstract class BaseSetupActivity extends Activity {
	public SharedPreferences mPref;
	private GestureDetector mDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		
		
		mDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				//�ٶ�̫������������ 
				if(Math.abs(velocityX) < 100){
					return true;
				}
				//y�����̫��������(������)
				if(Math.abs(e2.getRawY() - e1.getRawY()) > 100){
					return true;
				}
				//���һ�
				if(e2.getRawX() - e1.getRawX() > 200){
					showPreviousPage();
					return true;
				}
				//����
				if(e1.getRawX() - e2.getRawX() > 200){
					showNextPage();
					return true;
				}
				
				return super.onFling(e1, e2, velocityX, velocityY);
			}

			
		});
	}
	public abstract void showNextPage();
	public abstract void showPreviousPage();
	
	public void next(View view){
		showNextPage();
	}
	public void previous(View view){
		showPreviousPage();
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		mDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
}
