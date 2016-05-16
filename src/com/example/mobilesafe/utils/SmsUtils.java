package com.example.mobilesafe.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.xmlpull.v1.XmlSerializer;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Xml;

public class SmsUtils {
	public interface BackUpCallbackSms{
		public void befor(int count);
		public void onBackUpSms(int progress);
	}
	public static boolean backUp(Context context,ProgressDialog pd,BackUpCallbackSms callBack) {

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			ContentResolver contentResolver = context.getContentResolver();
			Uri uri = Uri.parse("content://sms/");
			Cursor cursor = contentResolver.query(uri, new String[] {
					"address", "date", "type", "body" }, null, null, null);
			//设置进度框
			int count = cursor.getCount();
//			pd.setMax(count);
			callBack.befor(count);
			
			int progress = 0;
			
			
			File file = new File(Environment.getExternalStorageDirectory(), "backup.xml");
			try {
				FileOutputStream os = new FileOutputStream(file);
				
				XmlSerializer xmlSerializer = Xml.newSerializer();
				
				xmlSerializer.setOutput(os, "utf-8");
				
				// standalone表示当前的xml是否是独立文件 ture表示文件独立。
				xmlSerializer.startDocument("utf-8", true);
				
				xmlSerializer.startTag(null, "smss");
				
				while(cursor.moveToNext()){
					xmlSerializer.startTag(null, "sms");
					
					xmlSerializer.startTag(null, "address");
					
					xmlSerializer.text(cursor.getString(0));
					
					xmlSerializer.endTag(null, "address");
					xmlSerializer.startTag(null, "date");
					
					xmlSerializer.text(cursor.getString(1));
					
					xmlSerializer.endTag(null, "date");
					xmlSerializer.startTag(null, "type");
					
					xmlSerializer.text(cursor.getString(2));
					
					xmlSerializer.endTag(null, "type");
					xmlSerializer.startTag(null, "body");
					
					xmlSerializer.text(Crypto.encrypt("123", cursor.getString(3)));
					
					xmlSerializer.endTag(null, "body");
					
					xmlSerializer.endTag(null, "sms");
					progress++;
//					pd.setProgress(progress);
					callBack.onBackUpSms(progress);
					SystemClock.sleep(2000);
				}
				
				xmlSerializer.endTag(null, "smss");
				
				xmlSerializer.endDocument();
				cursor.close();
				os.flush();
				os.close();
				return true;
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;

	}
}
