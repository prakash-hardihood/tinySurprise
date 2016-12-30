package com.kratav.tinySurprise.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FevDbHelper extends SQLiteOpenHelper {

	private static final String TAG = "FevDbHelper";
	private static final String DATABASE_NAME = "fev"; // Name of
																// Database
	private static int DATABASE_VERSION = 1; // Version of database
	private static final String[] dbCreate = {
			"create table fev(" + "title text,"+"url text,"+"sku text);",
	};

	public FevDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
		for (int i = 0; i < dbCreate.length; i++) {
			db.execSQL(dbCreate[i]); // query executer execSQL .. execute the
										// database creating query
		}

		Log.i(TAG, "Database is Created Successfully");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		// creating log for the action
		Log.w(TAG, "Upgading db this will lead to loss of all data");
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);

		// creating new one
		FevDbHelper.DATABASE_VERSION = newVersion;
		onCreate(db);

		Log.i(TAG, "Upgading complete");

	}
}
