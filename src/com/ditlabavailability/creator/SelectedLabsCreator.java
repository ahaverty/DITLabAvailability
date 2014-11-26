package com.ditlabavailability.creator;

import java.util.ArrayList;

import org.joda.time.DateTime;

import android.content.Context;

import com.ditlabavailability.data.SelectedLabsDbManager;
import com.ditlabavailability.model.LabTime;

public class SelectedLabsCreator {
	
	static SelectedLabsDbManager dbSelected;
	
	public static void createSelectedLabs(SelectedLabsDbManager db, Context context, ArrayList<LabTime> labTimesGrouped) {
		
		// Clear out tables before inserts
		ArrayList<LabTime> allLabsPre = db.getAllLabs();
		for (LabTime lab : allLabsPre) {
			db.deleteLab(lab.getRoom());
		}
		
		// Take array of labs and input to Selected labs table
		dbSelected = new SelectedLabsDbManager(context);
		dbSelected.createLabFromArray(labTimesGrouped);
	}
	
	static public ArrayList<LabTime> getLabsAfterTime(DateTime timeFrom){
		return dbSelected.getLabsAfterTime(timeFrom);
	}
	
	static public ArrayList<LabTime> getLabsAfterTimeWithFilters(DateTime timeFrom){
		return dbSelected.getLabsAfterTimeWithFilters(timeFrom);
	}
	
	static public ArrayList<LabTime> getFutureLabsByRoom(Context context, String roomName, DateTime timeFrom){
		dbSelected = new SelectedLabsDbManager(context);
		return dbSelected.getFutureLabsByRoom(roomName, timeFrom);
	}
}
