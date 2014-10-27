package com.ditlabavailability.model;

import java.sql.Timestamp;

public class LabTime {

	String room;
	// Expand labtime to day, date, hour etc
	Timestamp labtime;
	String location;
	boolean availability;
	Timestamp untilTime;

	public LabTime() {
	}

	/**
	 * @param room
	 * @param labtime
	 * @param location
	 * @param availability
	 */
	public LabTime(String room, Timestamp labtime, String location,
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
		customOutput = room + " | " + labtime.toString() + " | " + available
				+ " | " + location;
		return customOutput;
	}

	// getters
	public Timestamp getLabtime() {
		return labtime;
	}
	
	public String getLabtimeStr() {
		return labtime.toString();
	}
	
	public String getHourStr() {
		String labtimeStr = getLabtimeStr();
		String hour = (String)labtimeStr.subSequence(11, 16);
		return hour;
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
	
	public Timestamp getUntilTime() {
		return untilTime;
	}

	// setters
	public void setLabtime(Timestamp labtime) {
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
	
	public void setUntilTime(Timestamp untilTime) {
		this.untilTime = untilTime;
	}
	
	// custom methods
	public boolean isAvailable(){
		return getAvailability();
	}
	
}
