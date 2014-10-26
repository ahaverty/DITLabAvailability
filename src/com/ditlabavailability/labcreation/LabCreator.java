package com.ditlabavailability.labcreation;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.ditlabavailability.DatabaseHelper;
import com.ditlabavailability.model.LabDetails;
import com.ditlabavailability.model.LabTime;
import com.ditlabavailability.model.Reserved;

public class LabCreator {

	int dayStart = 9;
	int dayEnd = 21;

	public ArrayList<LabTime> createLabInstances(DatabaseHelper db, Timestamp selectedDate) {

		List<LabDetails> allLabs = db.getAllLabs();
		List<Reserved> reservationsByFilteredDate = db
				.getReservationsByDate(selectedDate);
		ArrayList<LabTime> allLabInstances = new ArrayList<LabTime>();

		String room;
		Timestamp labDatetime;
		String location;
		boolean availability;

		for (LabDetails lab : allLabs) {
			Log.d("Lab Name", lab.getRoom());

			for (int i = dayStart; i <= dayEnd; i++) {
				room = lab.getRoom();
				labDatetime = toSqlHourTimestamp(selectedDate, i);
				location = lab.getLocation();

				if (isReservationExists(reservationsByFilteredDate, room,
						labDatetime)) {
					availability = false;
				} else {
					availability = true;
				}

				allLabInstances.add(new LabTime(room, labDatetime, location,
						availability));
			}
		}

		for (LabTime lt : allLabInstances) {
			Log.d("Lab Time", lt.toString());
		}
		return allLabInstances;

	}

	private boolean isReservationExists(List<Reserved> allReservations,
			String room, Timestamp labDatetime) {
		for (Reserved r : allReservations) {
			if (r.getRoom().equals(room)) {
				if (r.getDatetime().equals(labDatetime)) {
					return true;
				}
			}
		}
		return false;
	}

	private Timestamp toSqlHourTimestamp(Timestamp date, int timeHour) {

		Timestamp sqlTimestamp;
		String tempTimeStr;
		String timeHourStr = Integer.toString(timeHour);

		if (timeHourStr.length() < 2) {
			timeHourStr = "0" + timeHourStr;
		}

		tempTimeStr = date.toString().subSequence(0, 11) + timeHourStr
				+ ":00:00.000";
		sqlTimestamp = Timestamp.valueOf(tempTimeStr);

		return sqlTimestamp;
	}

}
