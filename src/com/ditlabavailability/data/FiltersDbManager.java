package com.ditlabavailability.data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
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

		Log.d(LOG, "");
		try {
			db.insertOrThrow(TABLE_FILTER_LOCATIONS, null, values);
		} catch (SQLException exception) {
			if (exception instanceof SQLiteConstraintException) {
				Log.w(LOG, "Insert on table " + TABLE_FILTER_LOCATIONS
						+ " failed\nException: " + exception);
			} else {
				Log.w(LOG, "Insert on table " + TABLE_FILTER_LOCATIONS
						+ "failed\nException: " + exception);
			}
		} finally {
			db.close();
		}
	}

	public boolean getLocationStatus(String location) {
		SQLiteDatabase db = this.getWritableDatabase();
		boolean status;

		Cursor c = db.query(TABLE_FILTER_LOCATIONS,
				new String[] { KEY_STATUS }, KEY_LOCATION + " LIKE '" + location + "'",
				null, null, null, null);
		

		if (c.moveToFirst()) {
			status = getStatusBooleanFromInt(c.getInt(c
					.getColumnIndex(KEY_STATUS)));
		} else {
			Log.w(LOG,
					"getLocationStatus Cursor is null. FilterDatabase possibly not created yet.");
			Log.e(LOG, "Returning false");
			return false;
		}
		
		db.close();
		
		return status;
	}

	public void updateLocationStatus(String location, boolean status) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_LOCATION, location);
		values.put(KEY_STATUS, status);

		db.update(TABLE_FILTER_LOCATIONS, values, KEY_LOCATION + " = ?",
				new String[] { location });
		db.close();
	}

	/**
	 * Get all lab location names that are currently enabled in the filter
	 * location table
	 * 
	 * @return ArrayList of location string names
	 */
	public ArrayList<String> getAllStatusTrueLocations() {
		ArrayList<String> enabledLocations = new ArrayList<String>();
		String selectQuery = "SELECT " + KEY_LOCATION + " FROM "
				+ TABLE_FILTER_LOCATIONS + " WHERE " + KEY_STATUS + " = 1";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		if (c.moveToFirst()) {
			do {
				enabledLocations
						.add(c.getString(c.getColumnIndex(KEY_LOCATION)));
			} while (c.moveToNext());
		}
		db.close();

		return enabledLocations;
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
