package com.kratav.tinySurprise.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.kratav.tinySurprise.bean.Product;

import java.util.ArrayList;
import java.util.List;

public class FevDbOperations {
	private static final String TAG = "selectStationDBControl";
	protected Context context;
	protected SQLiteDatabase db;
	protected FevDbHelper dbHelper;

	public FevDbOperations(Context context) {
		this.context = context;
		dbHelper = new FevDbHelper(context);
	}

	public FevDbOperations open() throws SQLiteException {
		db = dbHelper.getWritableDatabase();
		Log.i(TAG, "Opening the Database");
		return this;
	}

	public void close() {
		dbHelper.close();
		Log.i(TAG, "Database Closed");
	}

	public List<Product> getAllFevProducts() {
		try {
			this.open();
			String sql = "SELECT title,url,sku FROM fev";
			Cursor mCur = db.rawQuery(sql, null);
			mCur.moveToFirst();
			List<Product> fevSKUList = new ArrayList<Product>();
			Product product;
			while (!mCur.isAfterLast()) {
				product = new Product();
				product.setProductCode(mCur.getString(mCur
						.getColumnIndex("sku")));
				product.setProductName(mCur.getString(mCur
						.getColumnIndex("title")));
				product.setProductImageUrl(mCur.getString(mCur
						.getColumnIndex("url")));
				fevSKUList.add(product);
				mCur.moveToNext();
			}
			mCur.close();
			this.close();
			return fevSKUList;
		} catch (SQLException mSQLException) {
			Log.e(TAG, "getTestData >>" + mSQLException.toString());
			throw mSQLException;
		}
	}

	public boolean favSKU(Product product) {
		try {
			this.open();
			String sql = "SELECT count(sku) FROM fev WHERE sku = '"
					+ product.getProductCode() + "' LIMIT 1 ;";
			Cursor mCur = db.rawQuery(sql, null);
			mCur.moveToFirst();
			boolean status = false;
			if (mCur.getInt(0) < 1) {
				sql = "insert into fev(title,url,sku) values ('"
						+ product.getProductName() + "','"
						+ product.getProductImageUrl() + "','"
						+ product.getProductCode() + "');";
				db.execSQL(sql);
				status = true;
			} else {
				sql = "DELETE from fev where sku = '"
						+ product.getProductCode() + "';";
				db.execSQL(sql);
				status = false;
			}
			mCur.close();
			this.close();
			return status;
		} catch (SQLiteException sex) {
			sex.printStackTrace();
		}
		return false;
	}

	public boolean isFav(String sku) {
		try {
			this.open();
			Cursor cursor = db.rawQuery("select 1 from fev where sku='"+sku+"'",null);
			boolean exists = (cursor.getCount() > 0);
			cursor.close();
			this.close();
			return exists;
		} catch (SQLException sex) {
			sex.printStackTrace();
		}
		return false;
	}
}
