package com.example.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;

import com.example.mobilesafe.bean.BlackNumberInfo;

public class BlackNumberDao {
	private BlackNumberOpenHelper helper;

	public BlackNumberDao(Context context) {
		helper = new BlackNumberOpenHelper(context);
	}

	/**
	 * 添加黑名单号码
	 * 
	 * @param number
	 *            电话号码
	 * @param mode
	 *            模式
	 * @return
	 */
	public boolean add(String number, String mode) {
		SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("number", number);
		contentValues.put("mode", mode);
		long rowId = sqLiteDatabase.insert("blacknumber", null, contentValues);
		if (rowId == -1) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * 从黑名单中删除指定电话号码
	 * 
	 * @param number
	 *            电话号码
	 * @return
	 */
	public boolean delete(String number) {
		SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
		int rowNumber = sqLiteDatabase.delete("blacknumber", "number = ?",
				new String[] { number });
		if (rowNumber == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 通过电话号码修改拦截模式
	 * 
	 * @param number
	 * @param mode
	 * @return
	 */
	public boolean changeNumberMode(String number, String mode) {
		SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("mode", mode);
		int rowNumber = sqLiteDatabase.update("blacknumber", contentValues,
				"number = ?", new String[] { number });
		if (rowNumber == 0) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * 返回指定号码的拦截模式
	 * 
	 * @param number
	 * @return
	 */
	public String findNumber(String number) {
		String mode = "";
		SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.query("blacknumber",
				new String[] { "mode" }, "number = ?", new String[] { number },
				null, null, null);
		if (cursor.moveToNext()) {
			mode = cursor.getString(0);
		}
		cursor.close();
		sqLiteDatabase.close();
		return mode;
	}

	/**
	 * 查询所有的黑名单
	 * 
	 * @return
	 */
	public List<BlackNumberInfo> findAll() {
		SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();
		List<BlackNumberInfo> blackNumberInfos = new ArrayList<BlackNumberInfo>();
		Cursor cursor = sqLiteDatabase.query("blacknumber", new String[] {
				"number", "mode" }, null, null, null, null, null);
		while (cursor.moveToNext()) {
			BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
			String number = cursor.getString(0);
			String mode = cursor.getString(1);
			blackNumberInfo.setNumber(number);
			blackNumberInfo.setMode(mode);
			blackNumberInfos.add(blackNumberInfo);
		}
		cursor.close();
		sqLiteDatabase.close();
		SystemClock.sleep(3000);
		return blackNumberInfos;
	}

	/**
	 * 分页加载数据
	 * 
	 * @param pageNumber
	 *            第几页
	 * @param pageSize
	 *            每一页数据的大小
	 * @return
	 */
	public List<BlackNumberInfo> findPar(int pageNumber, int pageSize) {
		SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();
		List<BlackNumberInfo> blackNumberInfos = new ArrayList<BlackNumberInfo>();
		Cursor cursor = sqLiteDatabase.rawQuery(
				"select number,mode from blacknumber limit ? offset ?",
				new String[] { String.valueOf(pageSize),
						String.valueOf(pageNumber * pageSize) });
		while (cursor.moveToNext()) {
			BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
			blackNumberInfo.setNumber(cursor.getString(0));
			blackNumberInfo.setMode(cursor.getString(1));
			blackNumberInfos.add(blackNumberInfo);
			
		}
		cursor.close();
		sqLiteDatabase.close();
		return blackNumberInfos;
	}
	/**
	 * 分批加载数据
	 * @param startIndex 开始的位置
	 * @param maxCount 每一批加载的数据量
	 * @return
	 */
	public List<BlackNumberInfo> findPar2(int startIndex, int maxCount) {
		SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();
		List<BlackNumberInfo> blackNumberInfos = new ArrayList<BlackNumberInfo>();
		Cursor cursor = sqLiteDatabase.rawQuery(
				"select number,mode from blacknumber limit ? offset ?",
				new String[] { String.valueOf(maxCount),
						String.valueOf(startIndex) });
		while (cursor.moveToNext()) {
			BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
			blackNumberInfo.setNumber(cursor.getString(0));
			blackNumberInfo.setMode(cursor.getString(1));
			blackNumberInfos.add(blackNumberInfo);
			
		}
		cursor.close();
		sqLiteDatabase.close();
		return blackNumberInfos;
	}
	public int getTotalNumber(){
		SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery("select count(*) from blacknumber", null);
		cursor.moveToNext();
		int totalNumber = cursor.getInt(0);
		cursor.close();
		sqLiteDatabase.close();
		return totalNumber;
	}
}
