package com.ditlabavailability.helpers;

import java.util.ArrayList;

import com.ditlabavailability.model.LabTime;

public class LabGrouper {

	public ArrayList<LabTime> groupLabs(ArrayList<LabTime> labTimeResults) {
		ArrayList<LabTime> labTimesGrouped = new ArrayList<LabTime>();

		LabTime tempLab = null;

		for (LabTime lt : labTimeResults) {
			
			if(tempLab==null){
				tempLab = lt;
			}
			
			// NOTE: Inverted if statement!
			if (! (tempLab.getAvailability() == lt.getAvailability()
					&& tempLab.getRoom().equals(lt.getRoom())))
			{
				if(tempLab!=null){
					labTimesGrouped.add(tempLab);
				}
				tempLab = lt;
			}
			
			tempLab.setUntilTime(lt.getLabtime().plusHours(1));
		
		}
		
		// add last lab item to list
		labTimesGrouped.add(tempLab);
		
		return labTimesGrouped;
	}
}
