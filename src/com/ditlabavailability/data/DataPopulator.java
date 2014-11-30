package com.ditlabavailability.data;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import android.content.Context;

import com.ditlabavailability.helpers.Constants;
import com.ditlabavailability.model.LabDetails;
import com.ditlabavailability.model.Reserved;

/**
 * Database Helper class used for creating and inserting the initial lab details
 * and reservation data. Eventually this should call from an external database
 * to retrieve reservations.
 * 
 * @author Alan Haverty
 *
 */
public class DataPopulator {

	private static DateTimeFormatter sFmt = Constants.FMT;
	private static DateTime sTestingDate = DateTime.now().withTime(0, 0, 0, 0);
	private static LabTimesDbManager sDbLabTimes;

	/**
	 * Creates the Lab Details and Reservations Tables, then inserts some lab
	 * and reservation data, based of the current day timestamp.
	 * 
	 * @param context
	 */
	public static void populate(Context context) {

		sDbLabTimes = new LabTimesDbManager(context);

		// Clear out tables before inserts
		List<LabDetails> allLabsPre = sDbLabTimes.getAllLabs();
		for (LabDetails lab : allLabsPre) {
			sDbLabTimes.deleteLab(lab.getRoom());
		}

		List<Reserved> allReservationsPre = sDbLabTimes.getAllReservations();
		for (Reserved reservation : allReservationsPre) {
			sDbLabTimes.deleteReservationsByRoom(reservation.getRoom());
		}

		// Inserting labDetails in local DB
		sDbLabTimes.createLab(new LabDetails("AU101", "Aungier Street"));
		sDbLabTimes.createLab(new LabDetails("AU105", "Aungier Street"));
		sDbLabTimes.createLab(new LabDetails("AU106", "Aungier Street"));
		sDbLabTimes.createLab(new LabDetails("KA305", "Kevin Street"));
		sDbLabTimes.createLab(new LabDetails("KA306", "Kevin Street"));
		sDbLabTimes.createLab(new LabDetails("KA311", "Kevin Street"));

		// Inserting Reservations in local DB
		createLabInstance("AU101", 11);
		createLabInstance("AU101", 12);
		createLabInstance("AU101", 14);
		createLabInstance("AU101", 16);
		createLabInstance("AU105", 12);
		createLabInstance("AU105", 18);
		createLabInstance("KA305", 10);
		createLabInstance("KA305", 11);
		createLabInstance("KA305", 15);
		createLabInstance("KA306", 14);
		createLabInstance("KA306", 20);
		createLabInstance("KA311", 9);
		createLabInstance("KA311", 21);

		sDbLabTimes.closeDB();
	}

	/**
	 * 
	 * @param hourOfDay
	 * @return The formatted timestamp with the hour of day set to the supplied
	 *         integer.
	 */
	private static String createTimeStamp(int hourOfDay) {
		return sTestingDate.withHourOfDay(hourOfDay).toString(sFmt);
	}

	/**
	 * Creates the lab reservations in the Reserved Table.
	 * 
	 * @param roomName
	 * @param labHour
	 */
	private static void createLabInstance(String roomName, int labHour) {
		sDbLabTimes.createReservation(new Reserved(roomName,
				createTimeStamp(labHour)));
	}
}
