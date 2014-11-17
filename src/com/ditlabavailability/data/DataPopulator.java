package com.ditlabavailability.data;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.ditlabavailability.model.LabDetails;
import com.ditlabavailability.model.Reserved;

public class DataPopulator {
	
	static DateTimeFormatter fmt = DateTimeFormat
			.forPattern("YYYY-MM-dd HH:mm:ss.SSS");
	static DateTime currentDate = DateTime.now().withTime(0, 0, 0, 0);
	static LabTimesDbManager mDb;
	
	public static void populate(LabTimesDbManager db) {
		
		mDb = db;

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
		createLabInstance("AU101", 11);
		createLabInstance("AU101", 12);
		createLabInstance("AU101", 14);
		createLabInstance("AU101", 16);
		createLabInstance("AU105", 12);
		createLabInstance("KA305", 10);
		createLabInstance("KA305", 15);
		createLabInstance("KA311", 9);

	}
	
	private static String createTimeStamp(int hourOfDay){
		return currentDate.withHourOfDay(hourOfDay).toString(fmt);
	}
	
	private static void createLabInstance(String roomName, int labHour){
		mDb.createReservation(new Reserved(roomName, createTimeStamp(labHour)));
	}
}
