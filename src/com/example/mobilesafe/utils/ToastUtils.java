package com.example.mobilesafe.utils;

import android.content.Context;
import android.widget.Toast;
/**
 * 弹吐司的工具类 
 * @author God vision
 *
 */
public class ToastUtils {
	public static void showToast(Context context,String text){
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
}
