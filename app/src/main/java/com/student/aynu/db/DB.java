package com.student.aynu.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {

	public DB(Context context) {
		super(context, "DB", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		arg0.execSQL("CREATE TABLE record("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "text TEXT DEFAULT \"\"," + "time TEXT DEFAULT \"\")");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

}
