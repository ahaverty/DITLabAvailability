package com.ditlabavailability.grouper;

import java.util.ArrayList;

import com.ditlabavailability.model.LabTime;

public class LabGrouper {
	
	public ArrayList<LabTime> groupLabs(ArrayList<LabTime> labTimeResults) {
		ArrayList<LabTime> labTimesGrouped;
		
		LabTime tempLab;
		
		for (LabTime lt : labTimeResults) {
			
			// TODO make sure this isn't initialized every run!!!!
			tempLab = lt;
			
			if(tempLab.getAvailability() == lt.getAvailability()) {
				tempLab.setUntilTime(null);
			}
		
		}
		
		return null;
		//return labTimesGrouped;
	}
	
}
