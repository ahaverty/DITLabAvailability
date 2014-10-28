package com.ditlabavailability.dbutils;

import java.util.ArrayList;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.ditlabavailability.model.LabTime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SelectedLabsDbManager extends SQLiteOpenHelper {

	DateTimeFormatter fmt = DateTimeFormat
			.forPattern("YYYY-MM-dd HH:mm:ss.SSS");

	// Logcat tag
	private static final String LOG = "SelectedLabsDbManager";

	// Database Version
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "selectedLabs.db";

	// Table Names
	private static final String TABLE_S_LABTIME = "s_labtime";

	// Common columns between tables
	private static final String KEY_ROOM = "s_room";
	private static final String KEY_LABTIME = "s_labtime";
	private static final String KEY_UNTILTIME = "s_until_time";
	private static final String KEY_LOCATION = "s_location";
	private static final String KEY_AVAILABILITY = "s_availability";

	// Table Create Statement
	private static final String CREATE_TABLE_S_LABTIME = "CREATE TABLE "
			+ TABLE_S_LABTIME + "(" + KEY_ROOM + " TEXT, " + KEY_LABTIME
			+ " DATETIME, " + KEY_UNTILTIME + " DATETIME NOT NULL,"
			+ KEY_LOCATION + " TEXT NOT NULL," + KEY_AVAILABILITY
			+ " BOOLEAN NOT NULL CHECK (" + KEY_AVAILABILITY + " IN (0,1))," + "PRIMARY KEY ("
			+ KEY_ROOM + ", " + KEY_LABTIME + "))";

	public SelectedLabsDbManager(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		// creating required tables
		db.execSQL(CREATE_TABLE_S_LABTIME);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_S_LABTIME);

		// create new tables
		onCreate(db);
	}

	// closing database
	public void closeDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
	}

	public void dropTable() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_S_LABTIME);
	}

	/**
	 * Creating a lab
	 */
	public long createLab(LabTime lab) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ROOM, lab.getRoom());
		values.put(KEY_LABTIME, lab.getLabtimeStr());
		values.put(KEY_UNTILTIME, lab.getUntilTimeStr());
		values.put(KEY_LOCATION, lab.getLocation());
		values.put(KEY_AVAILABILITY, lab.getAvailabilityInt());

		// insert row
		long lab_id = db.insert(TABLE_S_LABTIME, null, values);

		return lab_id;
	}
	
	public void deleteLab(String labRoom) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_S_LABTIME, KEY_ROOM + " = ?",
				new String[] { String.valueOf(labRoom) });
	}
	
	public ArrayList<LabTime> getAllLabs() {
		ArrayList<LabTime> selectedLabs = new ArrayList<LabTime>();
		String selectQuery = "SELECT  * FROM " + TABLE_S_LABTIME;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				LabTime lt = new LabTime();
				lt.setRoom(c.getString(c.getColumnIndex(KEY_ROOM)));
				lt.setLabtimeStr(c.getString(c.getColumnIndex(KEY_LABTIME)));
				lt.setUntilTimeStr(c.getString(c.getColumnIndex(KEY_UNTILTIME)));
				lt.setLocation(c.getString(c.getColumnIndex(KEY_LOCATION)));
				lt.setAvailabilityStr(c.getString(c.getColumnIndex(KEY_AVAILABILITY)));

				// adding to lab times list
				selectedLabs.add(lt);
			} while (c.moveToNext());
		}
		return selectedLabs;
		
	}

	public void createLabFromArray(ArrayList<LabTime> labArray) {
		for (LabTime lab : labArray) {
			createLab(lab);
		}
	}

}
