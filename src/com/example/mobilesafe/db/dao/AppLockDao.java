package com.example.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;


public class AppLockDao {

	private AppLockOpenHelper helper;
	private Context context;

	public AppLockDao(Context context) {
		helper = new AppLockOpenHelper(context);
		this.context = context;
		
	}

	/**
	 * ��ӵ�����������
	 * 
	 * @param packageName
	 *            ����
	 */
	public void add(String packageName) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("packagename", packageName);
		db.insert("info", null, values);
		context.getContentResolver().notifyChange(Uri.parse("content://com.example.mobilesafe.change"), null);
		db.close();
	}

	/**
	 * �ӳ���������ɾ����ǰ�İ�
	 * 
	 * @param packageName
	 */
	public void delete(String packageName) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("info", "packagename=?", new String[] { packageName });
		context.getContentResolver().notifyChange(Uri.parse("content://com.example.mobilesafe.change"), null);
		db.close();
	}
    /**
     * ��ѯ��ǰ�İ��Ƿ��ڳ���������
     * @param packageName
     * @return
     */
	public boolean find(String packageName) {
		boolean result = false;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("info", null, "packagename=?",
				new String[] { packageName }, null, null, null);
		if (cursor.moveToNext()) {
			result = true;
		}
		cursor.close();
		db.close();
		return result;

	}
	/**
	 * ��ѯȫ���������İ���
	 * @return
	 */
	public List<String> findAll(){
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("info", new String[]{"packagename"}, null, null, null, null, null);
		List<String> packnames = new ArrayList<String>();
		while(cursor.moveToNext()){
			packnames.add(cursor.getString(0));
		}
		cursor.close();
		db.close();
		return packnames;
	}
	
}
