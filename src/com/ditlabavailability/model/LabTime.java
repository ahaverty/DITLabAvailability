package com.ditlabavailability.model;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class LabTime {

	String room;
	// Expand labtime to day, date, hour etc
	DateTime labtime;
	String location;
	boolean availability;
	DateTime untilTime;

	DateTimeFormatter fmt = DateTimeFormat
			.forPattern("YYYY-MM-DD HH:mm:ss.SSS");

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
		this.room = room;
		this.labtime = labtime;
		this.location = location;
		this.availability = availability;
	}

	@Override
	public String toString() {
		String customOutput;
		String available = getAvailabilityStr();
		customOutput = room + " | " + fmt.print(labtime) + " | " + available
				+ " | " + location;
		return customOutput;
	}

	// getters
	public DateTime getLabtime() {
		return labtime;
	}

	public String getLabtimeStr() {
		return fmt.print(labtime);
	}

	public int getHour() {
		return labtime.getHourOfDay();
	}

	public String getHourStr() {
		return Integer.toString(labtime.getHourOfDay()) + ":00";
	}

	public String getRoom() {
		return room;
	}

	public String getLocation() {
		return location;
	}

	public boolean getAvailability() {
		return availability;
	}

	public String getAvailabilityStr() {
		String available = "Not Available";

		if (availability) {
			available = "Available";
		}
		return available;
	}

	public DateTime getUntilTime() {
		return untilTime;
	}

	public String getUntilStr() {
		String hourStr = Integer.toString(getUntilTime().getHourOfDay());
		return hourStr + ":00";
	}

	// setters
	public void setLabtime(DateTime labtime) {
		this.labtime = labtime;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setAvailability(boolean availability) {
		this.availability = availability;
	}

	public void setUntilTime(DateTime untilTime) {
		this.untilTime = untilTime;
	}

	// custom methods
	public boolean isAvailable() {
		return getAvailability();
	}

}
