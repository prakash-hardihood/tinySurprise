package com.kratav.tinySurprise.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;
import java.util.List;

public class DataAdapter {
	protected static final String TAG = "DataAdapter";
	private final Context mContext;
	private SQLiteDatabase mDb;
	private DataBaseHelper mDbHelper;

	public DataAdapter(Context context) {
		this.mContext = context;
		mDbHelper = new DataBaseHelper(mContext);
	}

	public DataAdapter createDatabase() throws SQLException {
		try {
			mDbHelper.createDataBase();
		} catch (IOException mIOException) {
			Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
			throw new Error("UnableToCreateDatabase");
		}
		return this;
	}

	public DataAdapter open() throws SQLException {
		try {
			mDbHelper.openDataBase();
			mDbHelper.close();
			mDb = mDbHelper.getReadableDatabase();
		} catch (SQLException mSQLException) {
			Log.e(TAG, "open >>" + mSQLException.toString());
			throw mSQLException;
		}
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	public List<String> getProductNameList(List<String> productNameList) {
		try {
			this.open();
			String sql = "SELECT DISTINCT name FROM search";

			Cursor mCur = mDb.rawQuery(sql, null);
			mCur.moveToFirst();
			while (!mCur.isAfterLast()) {
				productNameList
						.add(mCur.getString(mCur.getColumnIndex("name")));
				mCur.moveToNext();
			}
			mCur.close();
			this.close();
			return productNameList;
		} catch (SQLException mSQLException) {
			Log.e(TAG, "getTestData >>" + mSQLException.toString());
			throw mSQLException;
		}
	}

	public boolean getCategoryList(String query) {
		try {
			this.open();
			String sql = "SELECT * FROM search where sku = name and sku ='"
					+ query + "'";

			Cursor mCur = mDb.rawQuery(sql, null);
			mCur.moveToFirst();
			try {
				if ((mCur.getString(mCur.getColumnIndex("name"))).length() > 1) {
					mCur.close();
					this.close();
					return true;
				} else {
					mCur.close();
					this.close();
					return false;
				}
			} catch (Exception e) {
				return false;
			}

		} catch (SQLException mSQLException) {
			Log.e(TAG, "getTestData >>" + mSQLException.toString());
			throw mSQLException;
		}
	}

	public String getSKU(String query) {
		try {
			this.open();
			String sql = "SELECT sku FROM search where name = '" + query + "'";
			Cursor mCur = mDb.rawQuery(sql, null);
			mCur.moveToFirst();
			String temp = mCur.getString(mCur.getColumnIndex("sku"));
			Log.v("GETSKU", "The temp is: " + temp + " : " + query);
			return temp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
