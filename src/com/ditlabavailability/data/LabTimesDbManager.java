package com.ditlabavailability.data;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.ditlabavailability.model.LabDetails;
import com.ditlabavailability.model.Reserved;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LabTimesDbManager extends SQLiteOpenHelper {

	DateTimeFormatter fmt = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss.SSS");

	// Logcat tag
	private static final String LOG = "LabTimesDbManager";

	// Database Version
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "labTimes.db";

	// Table Names
	private static final String TABLE_LABS = "labs";
	private static final String TABLE_RESERVED = "reserved";

	// Common columns between tables
	private static final String KEY_ROOM = "room";
	private static final String KEY_LOCATION = "location";
	private static final String KEY_DATETIME = "datetime";

	// Table Create Statements
	// LABS table create statement
	private static final String CREATE_TABLE_LABS = "CREATE TABLE "
			+ TABLE_LABS + "(" + KEY_ROOM + " TEXT PRIMARY KEY, "
			+ KEY_LOCATION + " TEXT" + ")";

	// TIMES table create statement
	private static final String CREATE_TABLE_RESERVED = "CREATE TABLE "
			+ TABLE_RESERVED + "(" + KEY_ROOM + " TEXT, " + KEY_DATETIME
			+ " DATETIME, " + "PRIMARY KEY (" + KEY_ROOM + ", " + KEY_DATETIME
			+ "))";
	
	

	public LabTimesDbManager(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		// creating required tables
		db.execSQL(CREATE_TABLE_LABS);
		db.execSQL(CREATE_TABLE_RESERVED);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LABS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVED);

		// create new tables
		onCreate(db);
	}

	public void dropTables(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LABS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVED);
	}

	/**
	 * Creating a lab
	 */
	public long createLab(LabDetails lab) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ROOM, lab.getRoom());
		values.put(KEY_LOCATION, lab.getLocation());

		// insert row
		long lab_id = db.insert(TABLE_LABS, null, values);

		return lab_id;
	}

	/**
	 * get single lab by room name
	 */
	public LabDetails getLabByRoom(String roomName) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_LABS + " WHERE "
				+ KEY_ROOM + " = " + roomName;

		Log.e(LOG, selectQuery);

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		LabDetails lab = new LabDetails();
		lab.setRoom((c.getString(c.getColumnIndex(KEY_ROOM))));
		lab.setLocation(c.getString(c.getColumnIndex(KEY_LOCATION)));

		return lab;
	}

	/**
	 * getting all labs
	 * */
	public List<LabDetails> getAllLabs() {
		List<LabDetails> labs = new ArrayList<LabDetails>();
		String selectQuery = "SELECT * FROM " + TABLE_LABS;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				LabDetails lab = new LabDetails();
				lab.setRoom((c.getString(c.getColumnIndex(KEY_ROOM))));
				lab.setLocation(c.getString(c.getColumnIndex(KEY_LOCATION)));

				// adding to lab list
				labs.add(lab);
			} while (c.moveToNext());
		}

		return labs;
	}

	/**
	 * Updating a lab
	 **/
	public int updateLab(LabDetails lab) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ROOM, lab.getRoom());
		values.put(KEY_LOCATION, lab.getLocation());

		// updating row
		return db.update(TABLE_LABS, values, KEY_ROOM + " = ?",
				new String[] { String.valueOf(lab.getRoom()) });
	}

	/**
	 * Deleting a lab
	 */
	public void deleteLab(String lab_room) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_LABS, KEY_ROOM + " = ?",
				new String[] { String.valueOf(lab_room) });
	}

	/**
	 * Creating lab time
	 */
	public long createReservation(Reserved reservation) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ROOM, reservation.getRoom());
		values.put(KEY_DATETIME, reservation.getDatetimeStr());

		// insert row
		long reservation_id = db.insert(TABLE_RESERVED, null, values);

		return reservation_id;
	}

	/**
	 * getting all lab reservations
	 * */
	public List<Reserved> getAllReservations() {
		List<Reserved> labReservations = new ArrayList<Reserved>();
		String selectQuery = "SELECT  * FROM " + TABLE_RESERVED;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Reserved r = new Reserved();
				r.setRoom(c.getString(c.getColumnIndex(KEY_ROOM)));
				r.setDatetime(c.getString(c.getColumnIndex(KEY_DATETIME)));

				// adding to lab times list
				labReservations.add(r);
			} while (c.moveToNext());
		}
		return labReservations;
	}

	/**
	 * getting all lab reservations by date
	 * */
	public List<Reserved> getReservationsByDate(DateTime dateBegin) {

		DateTime dateEnd;
		dateEnd = dateBegin.plusHours(24);

		List<Reserved> labReservations = new ArrayList<Reserved>();
		String selectQuery = "SELECT * FROM " + TABLE_RESERVED
				+ " WHERE datetime >= Datetime('" + fmt.print(dateBegin)
				+ "') AND datetime <= Datetime('" + fmt.print(dateEnd) + "')";

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Reserved r = new Reserved();
				r.setRoom(c.getString(c.getColumnIndex(KEY_ROOM)));
				r.setDatetime(c.getString(c.getColumnIndex(KEY_DATETIME)));

				// adding to lab reservations list
				labReservations.add(r);
			} while (c.moveToNext());
		}
		return labReservations;
	}

	/**
	 * Updating a lab reservation
	 */
	public int updateReservations(Reserved reservation) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ROOM, reservation.getRoom());

		// updating row
		return db.update(TABLE_RESERVED, values, KEY_ROOM + " = ?",
				new String[] { String.valueOf(reservation.getRoom()) });
	}
	
	/**
	 * Deleting a reservation
	 */
	public void deleteReservationsByRoom(String lab_room) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_RESERVED, KEY_ROOM + " = ?",
				new String[] { String.valueOf(lab_room) });
	}

	// closing database
	public void closeDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
	}

}
