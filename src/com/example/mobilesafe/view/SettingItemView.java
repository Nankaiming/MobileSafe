package com.example.mobilesafe.view;

import com.example.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingItemView extends RelativeLayout {

	private static final String NAMESPACE = "http://schemas.android.com/apk/com.example.mobilesafe";
	private TextView tvTitle;
	private TextView tvDesc;
	private CheckBox cbStatus;
	private String mTitle;
	private String mDescOn;
	private String mDesOff;

	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
		// TODO Auto-generated constructor stub
	}

	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mTitle = attrs.getAttributeValue(NAMESPACE, "title");
		mDescOn = attrs.getAttributeValue(NAMESPACE, "desc_on");
		mDesOff = attrs.getAttributeValue(NAMESPACE, "desc_off");
		
		initView();
		// TODO Auto-generated constructor stub
	}

	public SettingItemView(Context context) {
		super(context);
		initView();
		// TODO Auto-generated constructor stub
	}

	private void initView() {
		// TODO Auto-generated method stub
		View.inflate(getContext(), R.layout.view_setting_item, this);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvDesc = (TextView) findViewById(R.id.tv_desc);
		cbStatus = (CheckBox) findViewById(R.id.cb_status);
		
		setTitle(mTitle);
	}

	public void setTitle(String text) {
		// TODO Auto-generated method stub
		tvTitle.setText(text);
	}

	public void setDesc(String text) {
		// TODO Auto-generated method stub
		tvDesc.setText(text);
	}

	public boolean isChecked() {
		return cbStatus.isChecked();
	}

	public void setChecked(boolean check) {
		// TODO Auto-generated method stub
		cbStatus.setChecked(check);
		if(check){
			setDesc(mDescOn);
		}else{
			setDesc(mDesOff);
		}
	}

}
