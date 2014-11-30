package com.ditlabavailability.helpers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ditlabavailability.model.LabTime;

public class LabsFilterer {

	/**
	 * @param labTimeList
	 * @return Sorted list of labs, placing groups, at end of list, where first
	 *         occurrence in group is 'unavailable'.
	 */
	public static ArrayList<LabTime> arrangeGroupedLabsByAvailability(
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
					}
				}
			}
		}

		moveToEnd(toMove, labTimeList);

		return labTimeList;
	}

	/**
	 * 
	 * @param labTimeList
	 * @param lab
	 * @return boolean if supplied lab is the soonest to occur with the same
	 *         room name
	 */
	private static boolean isSoonestOfRoom(ArrayList<LabTime> labTimeList, LabTime lab) {
		for (LabTime lt : labTimeList) {
			if (lt.getLabtime().isBefore(lab.getLabtime())
					&& lt.getRoom().equals(lab.getRoom())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Pass in a list of indexes and a list, function moves the items, at each
	 * index, to the bottom of the list
	 * 
	 * @param listOfIndexes
	 * @param itemList
	 * @return list with items moved to the bottom, retaining their respective
	 *         order
	 */
	private static ArrayList<LabTime> moveToEnd(List<Integer> listOfIndexes,
			ArrayList<LabTime> itemList) {
		// pop lab items to bottom of list
		int count = 0;
		for (int i : listOfIndexes) {
			itemList.add(itemList.get(i));
		}
		for (int i : listOfIndexes) {
			itemList.remove(i - count);
			count++;
		}

		return itemList;
	}
}
