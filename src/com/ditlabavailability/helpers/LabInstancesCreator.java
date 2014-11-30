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

/**
 * Helper class for creating the Labs for the current date supplied.
 * @author Alan Haverty
 *
 */
public class LabInstancesCreator {

	private static LabTimesDbManager sDbLabTimes;

	/**
	 * Populates the Selected Labs Database with all possible labs, available
	 * and unavailable, based on the data in the Reserved and Lab Details
	 * Databases. For every lab room from the Lab Details Table, there is a lab
	 * instance created for it for every hour between the integer
	 * {@link Constants}. If a reserved time for a lab doesn't exist, it will
	 * create an 'available' lab for that hour.
	 * 
	 * @param context
	 * @param selectedDate
	 * @return The newly created ArrayList of Labs for the current date.
	 */
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

	/**
	 * @param allReservations
	 * @param room
	 * @param labDatetime
	 * @return <h1>True</h1> if the supplied room name and timestamp matches a
	 *         lab record in the list supplied. <h1>False</h1> if the parameters
	 *         do not match any lab records in the list supplied.
	 */
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
