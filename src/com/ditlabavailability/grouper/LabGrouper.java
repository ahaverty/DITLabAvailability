package com.ditlabavailability.grouper;

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
			
			// TODO fix loop here
			// If tempLab's availability and room name matched current lab
			if (tempLab.getAvailability() == lt.getAvailability()
					&& tempLab.getRoom().equals(lt.getRoom()))
			{
				// Set tempLab's 'until' time to labTime + 1hr
				tempLab.setUntilTime(lt.getLabtime().plusHours(1));
			}
			else{
				
				if(tempLab!=null){
					labTimesGrouped.add(tempLab);
				}
				tempLab = lt;
			}
		}
		return labTimesGrouped;
	}
}
