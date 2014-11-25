package com.ditlabavailability;

import java.util.ArrayList;
import java.util.List;

import com.ditlabavailability.data.FiltersDbManager;
import com.ditlabavailability.data.LabTimesDbManager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;

public class FilterActivity extends Activity {

	private Context mContext;
	LinearLayout filterLinear;
	OnClickListener checkBoxListener;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filter_view);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
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
