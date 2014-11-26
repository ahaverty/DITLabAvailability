package com.ditlabavailability;

import java.util.ArrayList;
import java.util.List;

import com.ditlabavailability.data.FiltersDbManager;
import com.ditlabavailability.data.LabTimesDbManager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.Switch;

public class FilterActivity extends Activity {

	private Context mContext;
	LinearLayout filterLinear;
	OnClickListener checkBoxListener;
	
	public static final String PREFS_NAME = "FilterPreferences";
	SharedPreferences filterPreferences;
	String KEY_ALL_FILTERS_ENABLED = "allFiltersEnabled";
	String KEY_FAVOURITES_ENABLED = "favouritesEnabled";
	boolean allFiltersEnabled;
	boolean favouritesEnabled;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filter_view);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		// Load Preferences
		filterPreferences = getSharedPreferences(PREFS_NAME, 0);
		allFiltersEnabled = filterPreferences.getBoolean(KEY_ALL_FILTERS_ENABLED, false);
		favouritesEnabled = filterPreferences.getBoolean(KEY_FAVOURITES_ENABLED, false);
		
		Switch filtersOnOffSwitch = (Switch)  findViewById(R.id.filters_onoff_switch);
		filtersOnOffSwitch.setChecked(allFiltersEnabled);
		filtersOnOffSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		    Log.v("Switch State=", ""+isChecked);
		    allFiltersEnabled = isChecked;
		    SharedPreferences.Editor editor = filterPreferences.edit();
		    editor.putBoolean(KEY_ALL_FILTERS_ENABLED, allFiltersEnabled);
		    editor.putBoolean(KEY_FAVOURITES_ENABLED, favouritesEnabled);
		    editor.commit();
		}       

		});
		
		filterLinear = (LinearLayout) findViewById(R.id.location_selection);
		mContext = getApplicationContext();
		createLocationCheckboxes();
	}
	
	private void createLocationCheckboxes() {
		List<String> locationNames = new ArrayList<String>();
		locationNames = new LabTimesDbManager(mContext).getAllLocationNames();
		boolean status;
		
		for (String location:locationNames){
			status = new FiltersDbManager(mContext).getLocationStatus(location);
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
				boolean status = ((CheckBox)v).isChecked();
				String location = (String) ((CheckBox)v).getText();
				updateLocationStatus(location, status);
			}
        });
		filterLinear.addView(checkBox);
    }
	
	private void updateLocationStatus(String location, boolean status) {
		new FiltersDbManager(mContext).updateLocationStatus(location, status);
	}
	
	
	
}
