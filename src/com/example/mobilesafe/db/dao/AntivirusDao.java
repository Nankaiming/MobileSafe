package com.example.mobilesafe.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AntivirusDao {
	private static final String PATH = "data/data/com.example.mobilesafe/files/antivirus.db";

	public static String checkFileVirus(String md5) {

		String desc = null;

		SQLiteDatabase db = SQLiteDatabase.openDatabase(PATH, null,
				SQLiteDatabase.OPEN_READONLY);

		// ��ѯ��ǰ��������md5�Ƿ��ڲ������ݿ�����
		Cursor cursor = db.rawQuery("select desc from datable where md5 = ?",
				new String[] { md5 });
		// �жϵ�ǰ���α��Ƿ�����ƶ�
		if (cursor.moveToNext()) {
			desc = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return desc;
	}
	public static void addVirus(String md5,String desc){
		SQLiteDatabase db = SQLiteDatabase.openDatabase(PATH, null,
				SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.rawQuery("select desc from datable where md5 = ?",
				new String[] { md5 });
		if (cursor.moveToNext()) {
			return;
		}else{
			ContentValues values = new ContentValues();
			
			values.put("md5", md5);
			
			values.put("type", 6);
			
			values.put("name", "Android.Troj.AirAD.a");
			
			values.put("desc", desc);
			
			db.insert("datable", null, values);
		}
		cursor.close();
		db.close();
	}
}
