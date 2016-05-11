package com.example.mobilesafe.view;

import com.example.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingClickView extends RelativeLayout {

	private TextView tvTitle;
	private TextView tvDesc;

	public SettingClickView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
		// TODO Auto-generated constructor stub
	}

	public SettingClickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
		// TODO Auto-generated constructor stub
	}

	public SettingClickView(Context context) {
		super(context);
		initView();
		// TODO Auto-generated constructor stub
	}

	private void initView() {
		View.inflate(getContext(), R.layout.view_setting_click, this);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvDesc = (TextView) findViewById(R.id.tv_desc);
	}

	public void setTitle(String title) {
		tvTitle.setText(title);
	}

	public void setDesc(String desc) {
		tvDesc.setText(desc);
	}
}
