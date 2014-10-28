package com.ditlabavailability.creator;

import java.util.ArrayList;

import android.content.Context;

import com.ditlabavailability.dbutils.SelectedLabsDbManager;
import com.ditlabavailability.model.LabTime;

public class SelectedLabsCreator {
	public void createSelectedLabs(SelectedLabsDbManager db, Context context, ArrayList<LabTime> labTimesGrouped) {
		
		SelectedLabsDbManager dbSelected;
		
		// Clear out tables before inserts
		ArrayList<LabTime> allLabsPre = db.getAllLabs();
		for (LabTime lab : allLabsPre) {
			db.deleteLab(lab.getRoom());
		}
		
		// Take array of labs and input to Selected labs table
		dbSelected = new SelectedLabsDbManager(context);
		dbSelected.createLabFromArray(labTimesGrouped);
		
	}
}
