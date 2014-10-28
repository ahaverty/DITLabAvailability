package com.ditlabavailability.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.joda.time.DateTime;

import com.ditlabavailability.model.LabTime;

public class Filterer {

	public ArrayList<LabTime> removePastLabsUsingUntil(
			ArrayList<LabTime> labTimeList, DateTime virtualTime) {

		for (Iterator<LabTime> iter = labTimeList.listIterator(); iter
				.hasNext();) {
			LabTime lab = iter.next();
			if (virtualTime.isAfter(lab.getUntilTime())) {
				iter.remove();
			}
		}

		return labTimeList;

	}

	public ArrayList<LabTime> arrangeByAvailability(
			ArrayList<LabTime> labTimeList) {

		for (Iterator<LabTime> iter = labTimeList.listIterator(); iter
				.hasNext();) {
			LabTime lab = iter.next();
			if (isSoonestOfRoom(labTimeList, lab) && !lab.getAvailability()) {
				for (LabTime roomLookup : labTimeList) {
					if (roomLookup.getRoom().equals(lab.getRoom())) {
						for (int i = labTimeList.indexOf(roomLookup); i < labTimeList
								.size()-1; i++) {
							Collections.swap(labTimeList, i, i + 1);
						}
					}
				}
			}
		}

		return labTimeList;
	}

	public boolean isSoonestOfRoom(ArrayList<LabTime> labTimeList, LabTime lab) {
		for (LabTime lt : labTimeList) {
			if (lt.getLabtime().isBefore(lab.getLabtime())
					&& lt.getRoom().equals(lab.getRoom())) {
				return false;
			}
		}
		return true;
	}

}
