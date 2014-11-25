package com.ditlabavailability.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FiltersDbManager extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "filters.db";

	// Logcat tag
	private static final String LOG = "FiltersDbManager";

	// Table Names
	private static final String TABLE_FILTER_LOCATIONS = "filter_locations";

	// Column Names
	private static final String KEY_LOCATION = "location";
	private static final String KEY_STATUS = "status";

	// Table Create Statement
	private static final String CREATE_TABLE_FILTER_LOCATIONS = "CREATE TABLE "
			+ TABLE_FILTER_LOCATIONS + " (" + KEY_LOCATION
			+ " TEXT PRIMARY KEY, " + KEY_STATUS + " BOOLEAN NOT NULL CHECK ("
			+ KEY_STATUS + " IN (0,1)) )";

	public FiltersDbManager(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_FILTER_LOCATIONS);
	}

	public void closeDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILTER_LOCATIONS);
		onCreate(db);
	}

	public void dropTables(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILTER_LOCATIONS);
	}

	public void insertIntoFilterLocationsTable(String location, boolean status) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(KEY_LOCATION, location);
		values.put(KEY_STATUS, setStatusToInt(status));

		db.insert(TABLE_FILTER_LOCATIONS, null, values);
	}

	public boolean getLocationStatus(String location) {
		SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT " + KEY_STATUS + " FROM "
				+ TABLE_FILTER_LOCATIONS + " WHERE " + KEY_LOCATION + " LIKE '"
				+ location + "'";

		Log.i(LOG, selectQuery);
		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		boolean status = getStatusBooleanFromInt(c.getInt(0));

		return status;
	}

	public void updateLocationStatus(String location, boolean status) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_LOCATION, location);
		values.put(KEY_STATUS, status);

		db.update(TABLE_FILTER_LOCATIONS, values, KEY_LOCATION + " = ?",
				new String[] { location });
	}

	private int setStatusToInt(boolean status) {
		if (status) {
			return 1;
		}
		return 0;
	}

	private boolean getStatusBooleanFromInt(int status) {
		if (status == 0) {
			return false;
		}
		return true;
	}

}
