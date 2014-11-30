package com.ditlabavailability.data;

import java.util.ArrayList;

import org.joda.time.DateTime;
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
	private Context mContext;

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
			+ " BOOLEAN NOT NULL CHECK (" + KEY_AVAILABILITY + " IN (0,1)),"
			+ "PRIMARY KEY (" + KEY_ROOM + ", " + KEY_LABTIME + "))";

	public SelectedLabsDbManager(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_S_LABTIME);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_S_LABTIME);
		onCreate(db);
	}

	private ArrayList<LabTime> retrieveLabData(Cursor c) {
		ArrayList<LabTime> labs = new ArrayList<LabTime>();
		if (c.moveToFirst()) {
			do {
				LabTime lt = new LabTime();
				lt.setRoom(c.getString(c.getColumnIndex(KEY_ROOM)));
				lt.setLabtimeStr(c.getString(c.getColumnIndex(KEY_LABTIME)));
				lt.setUntilTimeStr(c.getString(c.getColumnIndex(KEY_UNTILTIME)));
				lt.setLocation(c.getString(c.getColumnIndex(KEY_LOCATION)));
				lt.setAvailabilityStr(c.getString(c
						.getColumnIndex(KEY_AVAILABILITY)));

				// adding to lab times list
				labs.add(lt);
			} while (c.moveToNext());
		}
		return labs;
	}

	public void closeDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
	}

	public void dropTable() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_S_LABTIME);
	}

	public void deleteLab(String labRoom) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_S_LABTIME, KEY_ROOM + " = ?",
				new String[] { String.valueOf(labRoom) });
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

		long lab_id = db.insert(TABLE_S_LABTIME, null, values);

		return lab_id;
	}

	public void createLabFromArray(ArrayList<LabTime> labArray) {
		for (LabTime lab : labArray) {
			createLab(lab);
		}
	}

	public ArrayList<LabTime> getAllLabs() {
		ArrayList<LabTime> selectedLabs = new ArrayList<LabTime>();
		String selectQuery = "SELECT  * FROM " + TABLE_S_LABTIME;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		selectedLabs = retrieveLabData(c);

		return selectedLabs;

	}

	/**
	 * 
	 * @param timeFrom
	 * @return array of labs, occurring after or during specified time, ordered
	 *         by room name and time.
	 */
	public ArrayList<LabTime> getLabsAfterTime(DateTime timeFrom) {
		ArrayList<LabTime> selectedLabs = new ArrayList<LabTime>();
		String selectQuery = "SELECT  * FROM " + TABLE_S_LABTIME + " WHERE "
				+ KEY_UNTILTIME + " > Datetime('" + timeFrom.toString(fmt)
				+ "') ORDER BY " + KEY_ROOM + " ASC, " + KEY_LABTIME + " ASC";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		selectedLabs = retrieveLabData(c);

		return selectedLabs;
	}

	/**
	 * 
	 * @param timeFrom
	 * @return array of labs with filters, occurring after or during specified
	 *         time, ordered by room name and time.
	 */
	public ArrayList<LabTime> getLabsAfterTimeWithFilters(DateTime timeFrom) {
		ArrayList<LabTime> selectedLabs = new ArrayList<LabTime>();

		// where location in location_filter table and status = 1
		ArrayList<String> enabledLocations = new FiltersDbManager(mContext)
				.getAllStatusTrueLocations();

		String enabledLocationsFormatted = "";
		for (String location : enabledLocations) {
			enabledLocationsFormatted += "'" + location + "'";
			// if not at last element of enabled locations
			// then add comma and space
			if (enabledLocations.indexOf(location) < enabledLocations.size() - 1) {
				enabledLocationsFormatted += ", ";
			}
		}

		String selectQuery = "SELECT  * FROM " + TABLE_S_LABTIME + " WHERE "
				+ KEY_UNTILTIME + " > Datetime('" + timeFrom.toString(fmt)
				+ "') AND " + KEY_LOCATION + " IN ( "
				+ enabledLocationsFormatted + " ) ORDER BY " + KEY_ROOM
				+ " ASC, " + KEY_LABTIME + " ASC";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		selectedLabs = retrieveLabData(c);

		return selectedLabs;
	}

	/**
	 * 
	 * @param timeFrom
	 * @return array of labs, occurring after or during specified time, ordered
	 *         by room name and current time.
	 */
	public ArrayList<LabTime> getFutureLabsByRoom(String roomName) {
		DateTime timeFrom = DateTime.now();
		return getFutureLabsByRoom(roomName, timeFrom);
	}

	/**
	 * 
	 * @param timeFrom
	 * @return array of labs, occurring after or during specified time, ordered
	 *         by room name and time.
	 */
	public ArrayList<LabTime> getFutureLabsByRoom(String roomName,
			DateTime timeFrom) {
		ArrayList<LabTime> selectedLabs = new ArrayList<LabTime>();

		String selectQuery = "SELECT * FROM " + TABLE_S_LABTIME + " WHERE "
				+ KEY_ROOM + " = '" + roomName + "' AND " + KEY_UNTILTIME
				+ " > Datetime('" + timeFrom.toString(fmt) + "') ORDER BY "
				+ KEY_ROOM + " ASC, " + KEY_LABTIME + " ASC";
		
		try {
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor c = db.rawQuery(selectQuery, null);

			selectedLabs = retrieveLabData(c);
			
			if (c.getCount() == 0){
				throw new Exception();
			}
		} catch(Exception exception){
			Log.e(LOG, "Unable to retrieve future labs\n" + exception);
			selectedLabs = null;
		}

		return selectedLabs;
	}

}
