package com.ditlabavailability.helpers;

import java.util.ArrayList;

import com.ditlabavailability.model.LabTime;

public class LabGrouper {
	
	/**
	 * Groups lab hours that have same room name and availability status
	 * @param labTimeResults
	 * @return labs grouped with new 'until_time' property
	 */
	public static ArrayList<LabTime> groupSimilarLabsByAvailability(
			ArrayList<LabTime> sortedLabs) {
		ArrayList<LabTime> labTimesGrouped = new ArrayList<LabTime>();

		LabTime tempLab = null;

		for (LabTime lt : sortedLabs) {

			if (tempLab == null) {
				tempLab = lt;
			}

			// NOTE: Inverted if statement!
			if (!(tempLab.getAvailability() == lt.getAvailability() && tempLab
					.getRoom().equals(lt.getRoom()))) {
				if (tempLab != null) {
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
