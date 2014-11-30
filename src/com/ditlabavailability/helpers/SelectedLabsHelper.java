package com.ditlabavailability.helpers;

import java.util.ArrayList;

import org.joda.time.DateTime;

import android.content.Context;

import com.ditlabavailability.data.SelectedLabsDbManager;
import com.ditlabavailability.model.LabTime;

/**
 * Helper class for the Selected Labs Database.
 * 
 * @author Alan Haverty
 *
 */
public class SelectedLabsHelper {

	private SelectedLabsDbManager mDbSelected;

	public SelectedLabsHelper(Context context) {
		mDbSelected = new SelectedLabsDbManager(context);
	}

	/**
	 * Truncates Selected Labs Table and then inserts grouped LabTimes
	 * 
	 * @param labTimesGrouped
	 */
	public void createSelectedLabs(ArrayList<LabTime> labTimesGrouped) {

		// Clear out tables before inserts
		ArrayList<LabTime> allLabsPre = mDbSelected.getAllLabs();
		for (LabTime lab : allLabsPre) {
			mDbSelected.deleteLab(lab.getRoom());
		}

		// Take array of labs and input to Selected labs table
		mDbSelected.createLabFromArray(labTimesGrouped);
	}

	/**
	 * @param timeFrom
	 * @return Labs that occur after the supplied timestamp from the Selected
	 *         Labs Database.
	 */
	public ArrayList<LabTime> getLabsAfterTime(DateTime timeFrom) {
		return mDbSelected.getLabsAfterTime(timeFrom);
	}

	/**
	 * @param timeFrom
	 * @return Labs that occur after the supplied timestamp, with the location
	 *         filter applied, from the Selected Labs Database.
	 */
	public ArrayList<LabTime> getLabsAfterTimeWithFilters(DateTime timeFrom) {
		return mDbSelected.getLabsAfterTimeWithFilters(timeFrom);
	}

	/**
	 * 
	 * @param roomName
	 * @param timeFrom
	 * @return A list of labs by room, that exists beyond the supplied
	 *         timestamp, from the Selected Labs Database
	 */
	public ArrayList<LabTime> getFutureLabsByRoom(String roomName,
			DateTime timeFrom) {
		ArrayList<LabTime> labs = null;
		labs = mDbSelected.getFutureLabsByRoom(roomName, timeFrom);
		return labs;
	}

	/**
	 * Closes the Database connection that was opened in
	 * {@link SelectedLabsHelper}'s constructor.
	 */
	public void closeDb() {
		mDbSelected.closeDB();
	}
}
