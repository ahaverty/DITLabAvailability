package com.ditlabavailability;

import java.util.List;

import com.ditlabavailability.model.LabDetails;
import com.ditlabavailability.model.Reserved;

public class DataPopulator {

	public static void populate(DatabaseHelper db) {

		// Clear out tables before inserts
		List<LabDetails> allLabsPre = db.getAllLabs();
		for (LabDetails lab : allLabsPre) {
			db.deleteLab(lab.getRoom());
		}

		List<Reserved> allReservationsPre = db.getAllReservations();
		for (Reserved reservation : allReservationsPre) {
			db.deleteReservationsByRoom(reservation.getRoom());
		}

		// Inserting labDetails in local DB
		db.createLab(new LabDetails("AU101", "Aungier Street"));
		db.createLab(new LabDetails("AU105", "Aungier Street"));
		db.createLab(new LabDetails("AU106", "Aungier Street"));
		db.createLab(new LabDetails("KA305", "Kevin Street"));
		db.createLab(new LabDetails("KA306", "Kevin Street"));
		db.createLab(new LabDetails("KA311", "Kevin Street"));

		// Inserting Reservations in local DB
		db.createReservation(new Reserved("AU101", "2014-10-27 11:00:00.000"));
		db.createReservation(new Reserved("AU101", "2014-10-27 12:00:00.000"));
		db.createReservation(new Reserved("AU101", "2014-10-27 14:00:00.000"));
		db.createReservation(new Reserved("AU101", "2014-10-27 16:00:00.000"));
		db.createReservation(new Reserved("AU105", "2014-10-27 12:00:00.000"));
		db.createReservation(new Reserved("KA305", "2014-10-27 10:00:00.000"));
		db.createReservation(new Reserved("KA311", "2014-10-27 09:00:00.000"));

		

	}
}
