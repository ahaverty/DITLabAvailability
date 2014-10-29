package com.ditlabavailability.filter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ditlabavailability.model.LabTime;

public class Filterer {

	/**
	 * Function assumes labs are being passed in already sorted by into groups
	 * of rooms.
	 * 
	 * @param labTimeList
	 * @return Sorted list of labs, placing groups, at end of list, where first
	 *         occurrence in group is 'unavailable'.
	 */
	public ArrayList<LabTime> arrangeByAvailability(
			ArrayList<LabTime> labTimeList) {

		List<Integer> toMove = new ArrayList<Integer>();

		for (Iterator<LabTime> iter = labTimeList.listIterator(); iter
				.hasNext();) {
			LabTime lab = iter.next();

			// if the soonest lab of room is not available
			if (isSoonestOfRoom(labTimeList, lab) && !lab.getAvailability()) {
				// take all labs of same room name and place them at the end of
				// the list
				for (LabTime roomLookup : labTimeList) {
					if (roomLookup.getRoom().equals(lab.getRoom())) {

						toMove.add(labTimeList.indexOf(roomLookup));

						// for (int i = labTimeList.indexOf(roomLookup); i <
						// labTimeList
						// .size()-1; i++) {
						// Collections.swap(labTimeList, i, i + 1);
						// }
					}
				}
			}
		}

		for (int i : toMove) {
			labTimeList.add(labTimeList.get(i));
		}

		int count = 0;
		for (int i : toMove) {
			labTimeList.remove(i - count);
			count++;
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
