package com.ditlabavailability.model;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import com.ditlabavailability.helpers.Constants;

/**
 * Model class for a lab reservation time consisting of a room name and a start
 * datetime. Used primarily for setting up the initial reservation database
 * without other specifics like lab end time and building location.
 * 
 * @author Alan Haverty
 *
 */
public class Reserved {

	private String mRoom;
	private DateTime mDatetime;

	private DateTimeFormatter mFmt = Constants.FMT;

	public Reserved() {
	}

	public Reserved(String room) {
		this.mRoom = room;
	}

	public Reserved(String room, String datetimeStr) {
		this.mRoom = room;
		this.mDatetime = getDatetime(datetimeStr);
	}

	// getters
	public String getRoom() {
		return mRoom;
	}

	public DateTime getDatetime() {
		return mDatetime;
	}

	public String getDatetimeStr() {
		return mFmt.print(mDatetime);
	}

	public DateTime getDatetime(String datetimeStr) {
		return DateTime.parse(datetimeStr, mFmt);
	}
	
	/**
	 * @return The day of the week in integer format for the reservation.
	 */
	public int getDayOfWeekInt() {
		return mDatetime.getDayOfWeek();
	}
	
	/**
	 * @return The starting hour of the reservation in string format: hh
	 */
	public String getHourOfDay() {
		return Integer.toString(mDatetime.getHourOfDay());
	}

	// setters
	public void setRoom(String room) {
		this.mRoom = room;
	}
	
	/**
	 * Set the reservation datetime using a string in the format: 'YYYY-MM-dd HH:mm:ss.SSS'
	 * @param datetimeStr
	 */
	public void setDatetime(String datetimeStr) {
		this.mDatetime = getDatetime(datetimeStr);
	}

}
