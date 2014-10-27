package com.ditlabavailability.model;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Reserved {

	String room;
	DateTime datetime;
	String datetimeStr;
	
	DateTimeFormatter fmt = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss.SSS");

	public Reserved() {
	}

	public Reserved(String room) {
		this.room = room;
	}

	public Reserved(String room, String datetimeStr) {
		this.room = room;
		this.datetime = getDatetime(datetimeStr);
	}

	// getters
	public String getRoom() {
		return room;
	}

	public DateTime getDatetime() {
		return datetime;
	}

	public String getDatetimeStr() {
		return fmt.print(datetime);
	}

	public DateTime getDatetime(String datetimeStr) {
		return DateTime.parse(datetimeStr, fmt);
	}

	public int getDayOfWeekInt() {
		return datetime.getDayOfWeek();
	}

	public String getHourOfDay() {
		return Integer.toString(datetime.getHourOfDay());
	}

	// setters
	public void setRoom(String room) {
		this.room = room;
	}

	public void setDatetime(String datetimeStr) {
		this.datetime = getDatetime(datetimeStr);
	}

}
