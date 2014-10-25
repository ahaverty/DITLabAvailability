package com.ditlabavailability;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	// Logcat tag
	private static final String LOG = "DatabaseHelper";

	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "labTimes";

	// Table Names
	private static final String TABLE_LABS = "labs";
	private static final String TABLE_RESERVED = "reserved";

	// Common columns between tables
	private static final String KEY_ROOM = "room";

	// LABS Table - column names
	private static final String KEY_LOCATION = "location";

	// RESERVED Table - column names
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

	public DatabaseHelper(Context context) {
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

	/**
	 * Drop LAB and TIMES tables
	 * 
	 * @param db
	 */
	public void dropTables(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LABS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVED);
	}

	/**
	 * Creating a lab
	 */
	public long createLab(Labs lab) {
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
	public Labs getLabByRoom(String roomName) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_LABS + " WHERE "
				+ KEY_ROOM + " = " + roomName;

		Log.e(LOG, selectQuery);

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		Labs lab = new Labs();
		lab.setRoom((c.getString(c.getColumnIndex(KEY_ROOM))));
		lab.setLocation(c.getString(c.getColumnIndex(KEY_LOCATION)));

		return lab;
	}

	/**
	 * getting all labs
	 * */
	public List<Labs> getAllLabs() {
		List<Labs> labs = new ArrayList<Labs>();
		String selectQuery = "SELECT  * FROM " + TABLE_LABS;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Labs lab = new Labs();
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
	public int updateLab(Labs lab) {
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
				// TODO below may not work since return will be a date
				// may have to parse as a string
				r.setDatetime(c.getString(c.getColumnIndex(KEY_DATETIME)));

				// adding to lab times list
				labReservations.add(r);
			} while (c.moveToNext());
		}
		return labReservations;
	}

	/**
	 * getting all lab reservations
	 * */
	public List<Reserved> getReservationsByDate(String dateStr) {
		
		Calendar cal = Calendar.getInstance();
		DateFormat dateFormatSql = new SimpleDateFormat("y-M-d HH:mm:ss.S");
		
		Timestamp dateBegin = Timestamp.valueOf(dateStr);

		cal.setTime(dateBegin);
		cal.add(Calendar.DAY_OF_WEEK, 1);

		String dateEnd = dateFormatSql.format(cal.getTime());

		List<Reserved> labReservations = new ArrayList<Reserved>();
		String selectQuery = "SELECT  * FROM " + TABLE_RESERVED
				+ " WHERE datetime >= Datetime('" + dateBegin.toString()
				+ "') AND datetime <= Datetime('" + dateEnd.toString() + "')";

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

	// closing database
	public void closeDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
	}

}
