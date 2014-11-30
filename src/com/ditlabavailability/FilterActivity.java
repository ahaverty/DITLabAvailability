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

public class FilterActivity extends Activity {

	private Context mContext;
	LinearLayout filterLinear;
	OnClickListener checkBoxListener;

	private FilterPreferences mFilterPreferences;
	
	boolean allFiltersEnabled;
	boolean favouritesEnabled;
	int demoTimeHour;
	int demoTimeMinute;

	private LinearLayout mRestOfFiltersWrapper;
	private TimePicker mDemoTimePicker;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filter_view);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		mContext = getApplicationContext();

		// Load Preferences
		mFilterPreferences = new FilterPreferences(mContext);
		allFiltersEnabled = mFilterPreferences.isAllFiltersEnabled();
		favouritesEnabled = mFilterPreferences.isFavouritesEnabled();
		demoTimeHour = mFilterPreferences.getDemoTimeHour();
		demoTimeMinute = mFilterPreferences.getDemoTimeMinute();

		mRestOfFiltersWrapper = (LinearLayout) findViewById(R.id.rest_filters_wrapper);
		Switch filtersOnOffSwitch = (Switch) findViewById(R.id.filters_onoff_switch);
		filtersOnOffSwitch.setChecked(allFiltersEnabled);
		if (allFiltersEnabled == false) {
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

						allFiltersEnabled = isChecked;
					}

				});

		// TODO Remove demo TimePicker before release --------------
		// TimePicker used for demo purposes only ------------------
		mDemoTimePicker = (TimePicker) findViewById(R.id.demoTimePicker);
		mDemoTimePicker.setCurrentHour(demoTimeHour);
		mDemoTimePicker.setCurrentMinute(demoTimeMinute);
		// TimePicker used for demo purposes only -----------------------

		filterLinear = (LinearLayout) findViewById(R.id.location_selection);
		createLocationCheckboxes();
	}

	protected void onPause() {
		super.onPause();
		mFilterPreferences.setAllPreferences(allFiltersEnabled,
				favouritesEnabled, mDemoTimePicker.getCurrentHour(),
				mDemoTimePicker.getCurrentMinute());
	}

	private void createLocationCheckboxes() {
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

	private void updateLocationStatus(String location, boolean status) {
		FiltersDbManager dbFilters = new FiltersDbManager(mContext);
		dbFilters.updateLocationStatus(location, status);
		dbFilters.closeDB();
	}
}
