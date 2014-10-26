package com.ditlabavailability.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Reserved {

	String room;
	Timestamp datetime;
	String datetimeStr;
	Locale locale = java.util.Locale.getDefault();
	
	
	Calendar cal = Calendar.getInstance(locale);
	SimpleDateFormat dayStringFormat = new SimpleDateFormat("E", locale);
	SimpleDateFormat dayOfMonthFormat = new SimpleDateFormat("d", locale);
	SimpleDateFormat hourOfDayFormat = new SimpleDateFormat("H", locale);

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

	public Timestamp getDatetime() {
		return datetime;
	}

	public String getDatetimeStr() {
		return datetime.toString();
	}

	public Timestamp getDatetime(String datetimeStr) {
		return Timestamp.valueOf(datetimeStr);
	}

	public int getDayOfWeekInt() {
		cal.setTime(datetime);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	public String getDayOfWeek() {
		cal.setTime(datetime);
		return dayStringFormat.format(cal.getTime());
	}

	public String getDayOfMonth() {
		cal.setTime(datetime);
		return dayOfMonthFormat.format(cal.getTime());
	}

	public String getHourOfDay() {
		cal.setTime(datetime);
		return hourOfDayFormat.format(cal.getTime());
	}

	// setters
	public void setRoom(String room) {
		this.room = room;
	}

	public void setDatetime(String datetimeStr) {
		this.datetime = getDatetime(datetimeStr);
	}

}
