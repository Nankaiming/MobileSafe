package com.example.mobilesafe.utils;

import android.app.Activity;
import android.widget.Toast;
/**
 * 弹吐司的工具类 
 * @author God vision
 *
 */
public class ToastUtils {
	public static void showToast(final Activity context,final String text){
		if("main".equals(Thread.currentThread().getName())){
			Toast.makeText(context, text, 0).show();
		}else{
			context.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(context, text, 0).show();
				}
			});
		}
	}
}
