package com.ditlabavailability.data;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import com.ditlabavailability.helpers.Constants;
import com.ditlabavailability.model.LabDetails;
import com.ditlabavailability.model.Reserved;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Database Helper class for the 'Labs' and 'Reserved' tables in the LabTimes database.
 * @author Alan Haverty
 *
 */
public class LabTimesDbManager extends SQLiteOpenHelper {

	private DateTimeFormatter mFmt = Constants.FMT;

	// Logcat tag
	private static final String LOG = "LabTimesDbManager";

	// Database Version
	private final static int DATABASE_VERSION = 1;
	private final static String DATABASE_NAME = "labTimes.db";

	// Table Names
	private final String TABLE_LABS = "labs";
	private final String TABLE_RESERVED = "reserved";

	// Common columns between tables
	private final String KEY_ROOM = "room";
	private final String KEY_LOCATION = "location";
	private final String KEY_DATETIME = "datetime";

	// Table Create Statements
	// LABS table create statement
	private final String CREATE_TABLE_LABS = "CREATE TABLE " + TABLE_LABS + "("
			+ KEY_ROOM + " TEXT PRIMARY KEY, " + KEY_LOCATION + " TEXT" + ")";

	// TIMES table create statement
	private final String CREATE_TABLE_RESERVED = "CREATE TABLE "
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
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LABS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVED);

		onCreate(db);
	}

	public void dropTables(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LABS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVED);
	}

	/**
	 * Inserts the supplied lab into the Labs Table.
	 * 
	 * @param lab
	 * @return The row ID of the newly inserted row, or -1 if an error occurred
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

	public void closeDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
	}

	/**
	 * Returns the lab from the table with the same room name as the String
	 * provided.
	 * 
	 * @param roomName
	 * @return The lab, of type {@link LabDetails}
	 */
	public LabDetails getLabByRoom(String roomName) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_LABS + " WHERE "
				+ KEY_ROOM + " = " + roomName;

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		LabDetails lab = new LabDetails();
		lab.setRoom((c.getString(c.getColumnIndex(KEY_ROOM))));
		lab.setLocation(c.getString(c.getColumnIndex(KEY_LOCATION)));

		return lab;
	}

	/**
	 * @return All labs, of type {@link LabDetails}, from the Labs Table.
	 */
	public List<LabDetails> getAllLabs() {
		List<LabDetails> labs = new ArrayList<LabDetails>();
		String selectQuery = "SELECT * FROM " + TABLE_LABS;

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
	 * Update the Labs Table by passing in a lab object, of type
	 * {@link LabDetails}. The update uses the room as its where equals clause.
	 * 
	 * @param lab
	 * @return The number of rows affected by the update.
	 */
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
	 * Delete a lab from the Table Labs, where the room matches the lab_room
	 * string provided.
	 * 
	 * @param lab_room
	 * @return the number of rows affected if a whereClause is passed in, 0
	 *         otherwise. To remove all rows and get a count pass "1" as the
	 *         whereClause.
	 */
	public int deleteLab(String lab_room) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.delete(TABLE_LABS, KEY_ROOM + " = ?",
				new String[] { String.valueOf(lab_room) });
	}

	/**
	 * Inserts into the Table Reserved using the supplied reservation, of type
	 * {@link Reserved}
	 * 
	 * @param reservation
	 * @return The row ID of the newly inserted row, or -1 if an error occurred.
	 */
	public long createReservation(Reserved reservation) {
		long reservation_id = -1;
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ROOM, reservation.getRoom());
		values.put(KEY_DATETIME, reservation.getDatetimeStr());

		try {
			reservation_id = db.insert(TABLE_RESERVED, null, values);
		} catch (Exception exception) {
			Log.e(LOG, "Failed to insert into: " + TABLE_RESERVED
					+ "\n with data: " + values.getAsString(KEY_ROOM) + " | "
					+ values.getAsString(KEY_DATETIME));
		}

		return reservation_id;
	}

	/**
	 * @return A list of reservations, of type {@link Reserved}, from the Table
	 *         Reserved.
	 */
	public List<Reserved> getAllReservations() {
		List<Reserved> labReservations = new ArrayList<Reserved>();
		String selectQuery = "SELECT  * FROM " + TABLE_RESERVED;

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
	 * @param dateBegin
	 * @return A list of reservations within a 24hr window of the supplied
	 *         dateBegin {@link DateTime}, of type {@link Reserved}, from the
	 *         Table Reserved.
	 */
	public List<Reserved> getReservationsByDate(DateTime dateBegin) {

		DateTime dateEnd;
		dateEnd = dateBegin.plusHours(24);

		List<Reserved> labReservations = new ArrayList<Reserved>();
		String selectQuery = "SELECT * FROM " + TABLE_RESERVED
				+ " WHERE datetime >= Datetime('" + mFmt.print(dateBegin)
				+ "') AND datetime <= Datetime('" + mFmt.print(dateEnd) + "')";

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
	 * Update the Reserved Table using the supplied reservation, of type
	 * {@link Reserved}.
	 * 
	 * @param reservation
	 * @return The number of rows effected
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
	 * Delete from the Reserved Table, using the supplied lab room string as the
	 * where equals clause.
	 * 
	 * @param lab_room
	 */
	public void deleteReservationsByRoom(String lab_room) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_RESERVED, KEY_ROOM + " = ?",
				new String[] { String.valueOf(lab_room) });
	}

	/**
	 * @return A distinct list of every location name that exists in the Lab Table.
	 */
	public List<String> getAllLocationNames() {
		List<String> locationNames = new ArrayList<String>();
		String selectQuery = "SELECT DISTINCT " + KEY_LOCATION + " FROM "
				+ TABLE_LABS;
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor c = db.rawQuery(selectQuery, null);

		if (c.moveToFirst()) {
			do {
				locationNames.add(c.getString(0));
			} while (c.moveToNext());
		}

		return locationNames;
	}
}
