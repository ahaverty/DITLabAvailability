package com.ditlabavailability;

import java.util.ArrayList;
import java.util.List;

import com.ditlabavailability.data.FiltersDbManager;
import com.ditlabavailability.data.LabTimesDbManager;
import com.ditlabavailability.helpers.FilterPreferences;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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

	// Logcat tag
	private static final String LOG = "FilterActivity";

	private FilterPreferences filterPreferences;
	boolean allFiltersEnabled;
	boolean favouritesEnabled;
	int demoTimeHour;
	int demoTimeMinute;

	private LinearLayout restOfFiltersWrapper;
	private TimePicker demoTimePicker;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filter_view);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// Load Preferences
		filterPreferences = new FilterPreferences(getApplicationContext());
		allFiltersEnabled = filterPreferences.isAllFiltersEnabled();
		favouritesEnabled = filterPreferences.isFavouritesEnabled();
		demoTimeHour = filterPreferences.getDemoTimeHour();
		demoTimeMinute = filterPreferences.getDemoTimeMinute();

		restOfFiltersWrapper = (LinearLayout) findViewById(R.id.rest_filters_wrapper);
		Switch filtersOnOffSwitch = (Switch) findViewById(R.id.filters_onoff_switch);
		filtersOnOffSwitch.setChecked(allFiltersEnabled);
		if (allFiltersEnabled == false) {
			restOfFiltersWrapper.setVisibility(View.GONE);
		}
		filtersOnOffSwitch
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							restOfFiltersWrapper.setVisibility(View.VISIBLE);
						} else {
							restOfFiltersWrapper.setVisibility(View.GONE);
						}

						Log.v("Switch State = ", "" + isChecked);
						allFiltersEnabled = isChecked;
					}

				});

		// TODO Remove demo TimePicker before release --------------
		// TimePicker used for demo purposes only ------------------
		demoTimePicker = (TimePicker) findViewById(R.id.demoTimePicker);
		demoTimePicker.setCurrentHour(demoTimeHour);
		demoTimePicker.setCurrentMinute(demoTimeMinute);
		// TimePicker used for demo purposes only -----------------------

		filterLinear = (LinearLayout) findViewById(R.id.location_selection);
		mContext = getApplicationContext();
		createLocationCheckboxes();
	}

	protected void onPause() {
		super.onPause();
		filterPreferences.setAllPreferences(allFiltersEnabled,
				favouritesEnabled, demoTimePicker.getCurrentHour(),
				demoTimePicker.getCurrentMinute());
	}

	private void createLocationCheckboxes() {
		List<String> locationNames = new ArrayList<String>();
		locationNames = new LabTimesDbManager(mContext).getAllLocationNames();
		boolean status;

		for (String location : locationNames) {
			status = new FiltersDbManager(mContext).getLocationStatus(location);
			addCheckbox(locationNames.indexOf(location), location, status);
		}
	}

	private void addCheckbox(int number, String label, boolean status) {
		Log.i(LOG, "Starting addCheckbox");
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
		Log.i(LOG, "Ending addCheckbox");
	}

	private void updateLocationStatus(String location, boolean status) {
		new FiltersDbManager(mContext).updateLocationStatus(location, status);
	}
}
