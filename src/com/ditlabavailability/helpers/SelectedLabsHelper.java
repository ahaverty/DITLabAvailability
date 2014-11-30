package com.ditlabavailability.helpers;

import java.util.ArrayList;

import org.joda.time.DateTime;

import android.content.Context;

import com.ditlabavailability.data.SelectedLabsDbManager;
import com.ditlabavailability.model.LabTime;

public class SelectedLabsHelper {
	
	private SelectedLabsDbManager mDbSelected;
	
	public SelectedLabsHelper(Context context) {
		mDbSelected = new SelectedLabsDbManager(context);
	}
	
	public void createSelectedLabs(ArrayList<LabTime> labTimesGrouped) {
		
		// Clear out tables before inserts
		ArrayList<LabTime> allLabsPre = mDbSelected.getAllLabs();
		for (LabTime lab : allLabsPre) {
			mDbSelected.deleteLab(lab.getRoom());
		}
		
		// Take array of labs and input to Selected labs table
		mDbSelected.createLabFromArray(labTimesGrouped);
	}
	
	public ArrayList<LabTime> getLabsAfterTime(DateTime timeFrom){
		return mDbSelected.getLabsAfterTime(timeFrom);
	}
	
	public ArrayList<LabTime> getLabsAfterTimeWithFilters(DateTime timeFrom){
		return mDbSelected.getLabsAfterTimeWithFilters(timeFrom);
	}
	
	public ArrayList<LabTime> getFutureLabsByRoom(String roomName, DateTime timeFrom){
		ArrayList<LabTime> labs = null;
		labs = mDbSelected.getFutureLabsByRoom(roomName, timeFrom);
		return labs;
	}
	
	public void closeDb() {
		mDbSelected.closeDB();
	}
}
