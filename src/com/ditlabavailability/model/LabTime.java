package com.ditlabavailability.model;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import com.ditlabavailability.helpers.Constants;

/**
 * Model class for a lab with a room name, building location, availability,
 * start time and end time.
 * 
 * @author Alan Haverty
 *
 */
public class LabTime {

	private String mRoom;
	private DateTime mLabtime;
	private String mLocation;
	private boolean mAvailability;
	private DateTime mUntilTime;

	private DateTimeFormatter mFmt = Constants.FMT;

	public LabTime() {
	}

	/**
	 * @param room
	 * @param labtime
	 * @param location
	 * @param availability
	 */
	public LabTime(String room, DateTime labtime, String location,
			boolean availability) {
		this.mRoom = room;
		this.mLabtime = labtime;
		this.mLocation = location;
		this.mAvailability = availability;
	}

	@Override
	public String toString() {
		String customOutput;
		String available = getAvailabilityStr();
		customOutput = mRoom + " | " + mFmt.print(mLabtime) + " | " + available
				+ " | " + mLocation;
		return customOutput;
	}

	// getters
	public DateTime getLabtime() {
		return mLabtime;
	}

	public String getLabtimeStr() {
		return mFmt.print(mLabtime);
	}

	public int getHour() {
		return mLabtime.getHourOfDay();
	}

	/**
	 * @return The hour that the lab begins, in the form hh:mm
	 */
	public String getHourStr() {
		return Integer.toString(mLabtime.getHourOfDay()) + ":00";
	}

	public String getRoom() {
		return mRoom;
	}

	public String getLocation() {
		return mLocation;
	}

	public boolean getAvailability() {
		return mAvailability;
	}

	/**
	 * @return The labs availability in String form, 'Available' if true and
	 *         'Not Available' if false.
	 */
	public String getAvailabilityStr() {
		String available = "Not Available";

		if (mAvailability) {
			available = "Available";
		}
		return available;
	}

	/**
	 * @return The labs availability in integer form, 1 if true and 0 if false
	 */
	public int getAvailabilityInt() {
		if (mAvailability)
			return 1;
		else
			return 0;
	}

	/**
	 * @return The datetime stamp that the lab instance is on until.
	 */
	public DateTime getUntilTime() {
		return mUntilTime;
	}

	/**
	 * @return The datetime stamp that the lab instance is on until, in string
	 *         format 'YYYY-MM-dd HH:mm:ss.SSS'
	 */
	public String getUntilTimeStr() {
		return mUntilTime.toString(mFmt);
	}

	/**
	 * @return The hour that the lab instance is on until, in string format
	 *         hh:mm
	 */
	public String getUntilHourStr() {
		String hourStr = Integer.toString(getUntilTime().getHourOfDay());
		return hourStr + ":00";
	}

	// setters
	public void setLabtime(DateTime labtime) {
		this.mLabtime = labtime;
	}

	public void setLabtimeStr(String labtimeStr) {
		this.mLabtime = DateTime.parse(labtimeStr, mFmt);
	}

	public void setRoom(String room) {
		this.mRoom = room;
	}

	public void setLocation(String location) {
		this.mLocation = location;
	}

	public void setAvailability(boolean availability) {
		this.mAvailability = availability;
	}

	/**
	 * Set the labs availability using either '1' as true and '0', or any other
	 * string, as false.
	 * 
	 * @param availability
	 */
	public void setAvailabilityStr(String availability) {
		int value = Integer.parseInt(availability);
		if (value == 1)
			this.mAvailability = true;
		else
			this.mAvailability = false;
	}

	public void setUntilTime(DateTime untilTime) {
		this.mUntilTime = untilTime;
	}

	/**
	 * Set the labs until datetime by passing in a timestamp in the string
	 * format 'YYYY-MM-dd HH:mm:ss.SSS'
	 * 
	 * @param untilTimeStr
	 */
	public void setUntilTimeStr(String untilTimeStr) {
		this.mUntilTime = DateTime.parse(untilTimeStr, mFmt);
	}

	/**
	 * 
	 * @return True if the lab is available or False if the lab is unavailable
	 */
	public boolean isAvailable() {
		return getAvailability();
	}

}
