package com.ditlabavailability;

import java.sql.Timestamp;
import java.util.List;

import android.util.Log;

public class DataPopulator {

	public static void populate(DatabaseHelper db) {

		List<Labs> allLabsPre = db.getAllLabs();
		for (Labs lab : allLabsPre) {
			db.deleteLab(lab.getRoom());
		}

		// Creating lab rooms
		Labs lab1 = new Labs("AU101", "Aungier Street");
		Labs lab2 = new Labs("AU105", "Aungier Street");
		Labs lab3 = new Labs("AU106", "Aungier Street");
		Labs lab4 = new Labs("KA305", "Kevin Street");
		Labs lab5 = new Labs("KA306", "Kevin Street");
		Labs lab6 = new Labs("KA311", "Kevin Street");

		// Inserting labs in db
		long lab1_id = db.createLab(lab1);
		long lab2_id = db.createLab(lab2);
		long lab3_id = db.createLab(lab3);
		long lab4_id = db.createLab(lab4);
		long lab5_id = db.createLab(lab5);
		long lab6_id = db.createLab(lab6);

		Log.d("Lab Count", "Lab Count: " + db.getAllLabs().size());

		// Getting all Labs
		Log.d("Get Labs", "Getting All Lab Room Names");

		List<Labs> allLabs = db.getAllLabs();
		for (Labs lab : allLabs) {
			Log.d("Lab Name", lab.getRoom());
		}

		List<Reserved> allReservationsPre = db.getAllReservations();
		for (Reserved reservation : allReservationsPre) {
			db.deleteLab(reservation.getRoom());
		}

		Reserved reservation1 = new Reserved(lab1.getRoom(),
				"2014-10-27 10:00:00.000");
		Reserved reservation2 = new Reserved(lab1.getRoom(),
				"2014-10-27 12:00:00.000");
		Reserved reservation3 = new Reserved(lab3.getRoom(),
				"2014-10-27 10:00:00.000");
		Reserved reservation4 = new Reserved(lab4.getRoom(),
				"2014-10-29 09:00:00.000");
		long reservation1_id = db.createReservation(reservation1);
		long reservation2_id = db.createReservation(reservation2);
		long reservation3_id = db.createReservation(reservation3);
		long reservation4_id = db.createReservation(reservation4);

		List<Reserved> allReservations = db.getAllReservations();
		for (Reserved r : allReservations) {
			Log.d("reservation Name", r.getRoom() + " | " + r.getDayOfWeek()
					+ " | " + r.getDayOfMonth() + " @ " + r.getHourOfDay());
		}
		
		List<Reserved> reservationsByDate = db.getReservationsByDate("2014-10-27 00:00:00.000");
		for (Reserved r : reservationsByDate) {
			Log.d("reservation Name", r.getRoom() + " | " + r.getDayOfWeek()
					+ " | " + r.getDayOfMonth() + " @ " + r.getHourOfDay());
		}

		// Closing database connection
		db.closeDB();
	}

}
