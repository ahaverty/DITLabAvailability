package com.ditlabavailability.creator;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import android.util.Log;

import com.ditlabavailability.data.LabTimesDbManager;
import com.ditlabavailability.model.LabDetails;
import com.ditlabavailability.model.LabTime;
import com.ditlabavailability.model.Reserved;

public class LabCreator {

	static int dayStart = 9;
	static int dayEnd = 21;

	public static ArrayList<LabTime> createAllLabInstances(LabTimesDbManager db,
			DateTime selectedDate) {

		List<LabDetails> allLabs = db.getAllLabs();
		List<Reserved> reservationsByFilteredDate = db
				.getReservationsByDate(selectedDate);
		ArrayList<LabTime> allLabInstances = new ArrayList<LabTime>();

		String room;
		DateTime labDatetime;
		String location;
		boolean availability;

		for (LabDetails lab : allLabs) {
			Log.d("Lab Name", lab.getRoom());

			for (int i = dayStart; i <= dayEnd; i++) {
				room = lab.getRoom();
				labDatetime = selectedDate.withHourOfDay(i);
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

	private static boolean isReservationExists(List<Reserved> allReservations,
			String room, DateTime labDatetime) {
		for (Reserved r : allReservations) {
			if (r.getRoom().equals(room)) {
				if (r.getDatetime().equals(labDatetime)) {
					return true;
				}
			}
		}
		return false;
	}

}
