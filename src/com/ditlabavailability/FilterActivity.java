package com.ditlabavailability;

import java.util.ArrayList;
import java.util.List;

import com.ditlabavailability.data.FiltersDbManager;
import com.ditlabavailability.data.LabTimesDbManager;
import com.ditlabavailability.helpers.FilterPreferences;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TimePicker;

/**
 * Activity to allow the user to filter labs by location, demo/debug current
 * time and favourites (Not yet fully implemented). Activity also deals with
 * saving the shared preference file and making updated to the filter database
 * 
 * @author Alan Haverty
 *
 */
public class FilterActivity extends Activity {

	private Context mContext;
	LinearLayout filterLinear;
	OnClickListener checkBoxListener;

	private FilterPreferences mFilterPreferences;

	private boolean mAllFiltersEnabled;
	private boolean mFavouritesEnabled;
	private int mDemoTimeHour;
	private int mDemoTimeMinute;

	private LinearLayout mRestOfFiltersWrapper;
	private TimePicker mDemoTimePicker;

	/**
	 * Setup action bar and back button for returning to main activity, load
	 * preferences, initialise the switches and the location checkbox, setup
	 * demo time picker
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filter_view);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		mContext = getApplicationContext();

		loadPreferences();

		mRestOfFiltersWrapper = (LinearLayout) findViewById(R.id.rest_filters_wrapper);
		Switch filtersOnOffSwitch = (Switch) findViewById(R.id.filters_onoff_switch);
		filtersOnOffSwitch.setChecked(mAllFiltersEnabled);
		if (mAllFiltersEnabled == false) {
			mRestOfFiltersWrapper.setVisibility(View.GONE);
		}
		filtersOnOffSwitch
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							mRestOfFiltersWrapper.setVisibility(View.VISIBLE);
						} else {
							mRestOfFiltersWrapper.setVisibility(View.GONE);
						}

						mAllFiltersEnabled = isChecked;
					}

				});

		// TODO Remove demo TimePicker before release --------------
		// TimePicker used for demo purposes only ------------------
		mDemoTimePicker = (TimePicker) findViewById(R.id.demoTimePicker);
		mDemoTimePicker.setCurrentHour(mDemoTimeHour);
		mDemoTimePicker.setCurrentMinute(mDemoTimeMinute);
		// TimePicker used for demo purposes only -----------------------

		filterLinear = (LinearLayout) findViewById(R.id.location_selection);
		createLocationCheckboxesUsingExistingLocations();
		// TODO Setup favourites and implement favourite switch into project
	}

	/**
	 * When Filter Activity loses focus, save the shared preference file before
	 * returning to the main activity
	 */
	protected void onPause() {
		super.onPause();
		mFilterPreferences.setAllPreferences(mAllFiltersEnabled,
				mFavouritesEnabled, mDemoTimePicker.getCurrentHour(),
				mDemoTimePicker.getCurrentMinute());
	}

	/**
	 * Load shared preferences file for filter settings and initialize class
	 * variables
	 */
	private void loadPreferences() {
		mFilterPreferences = new FilterPreferences(mContext);
		mAllFiltersEnabled = mFilterPreferences.isAllFiltersEnabled();
		mDemoTimeHour = mFilterPreferences.getDemoTimeHour();
		mDemoTimeMinute = mFilterPreferences.getDemoTimeMinute();
	}

	/**
	 * Get the location names from the existing local database and pass the
	 * index, location and status to addCheckbox()
	 */
	private void createLocationCheckboxesUsingExistingLocations() {
		List<String> locationNames = new ArrayList<String>();
		LabTimesDbManager dbLabTimes = new LabTimesDbManager(mContext);
		locationNames = dbLabTimes.getAllLocationNames();
		dbLabTimes.closeDB();
		boolean status;

		for (String location : locationNames) {
			FiltersDbManager dbFilters = new FiltersDbManager(mContext);
			status = dbFilters.getLocationStatus(location);
			dbFilters.closeDB();
			addCheckbox(locationNames.indexOf(location), location, status);
		}
	}

	/**
	 * Create the filter checkboxes and fill them with the data from the
	 * supplied parameters. Setup the onClick listener for the checkboxes
	 * 
	 * @param number
	 * @param label
	 * @param status
	 */
	private void addCheckbox(int number, String label, boolean status) {
		CheckBox checkBox = new CheckBox(this);
		checkBox.setId(number);
		checkBox.setText(label);
		checkBox.setChecked(status);
		checkBox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean status = ((CheckBox) v).isChecked();
				String location = (String) ((CheckBox) v).getText();
				updateLocationStatus(location, status);
			}
		});
		filterLinear.addView(checkBox);
	}

	/**
	 * Update the location status of a location, in the filters location table,
	 * using the supplied parameters: location and status
	 * 
	 * @param location
	 * @param status
	 */
	private void updateLocationStatus(String location, boolean status) {
		FiltersDbManager dbFilters = new FiltersDbManager(mContext);
		dbFilters.updateLocationStatus(location, status);
		dbFilters.closeDB();
	}
}
