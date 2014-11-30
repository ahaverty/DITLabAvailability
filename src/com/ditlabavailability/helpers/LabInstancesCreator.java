package com.ditlabavailability.helpers;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import android.content.Context;

import com.ditlabavailability.data.LabTimesDbManager;
import com.ditlabavailability.helpers.Constants;
import com.ditlabavailability.model.LabDetails;
import com.ditlabavailability.model.LabTime;
import com.ditlabavailability.model.Reserved;

public class LabInstancesCreator {

	private static LabTimesDbManager sDbLabTimes;

	public static ArrayList<LabTime> createAllLabInstances(Context context,
			DateTime selectedDate) {

		sDbLabTimes = new LabTimesDbManager(context);

		List<LabDetails> allLabs = sDbLabTimes.getAllLabs();
		List<Reserved> reservationsByFilteredDate = sDbLabTimes
				.getReservationsByDate(selectedDate);
		sDbLabTimes.closeDB();
		
		ArrayList<LabTime> allLabInstances = new ArrayList<LabTime>();

		String room;
		DateTime labDatetime;
		String location;
		boolean availability;

		for (LabDetails lab : allLabs) {

			for (int i = Constants.START_HOUR_OF_DAY; i <= Constants.END_HOUR_OF_DAY; i++) {
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
