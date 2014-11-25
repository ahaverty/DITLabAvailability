package com.ditlabavailability;

import java.util.ArrayList;
import java.util.List;

import com.ditlabavailability.data.LabTimesDbManager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

public class FilterActivity extends Activity {

	private Context mContext;
	LinearLayout filterLinear;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filter_view);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		filterLinear = (LinearLayout) findViewById(R.id.location_selection);

		mContext = getApplicationContext();
		
		//call to get available buildings from database
		//use distinct select on lab details possibly
		
		createLocationCheckboxes();
	}

	public void onClick(View v) {
	}
	
	private void createLocationCheckboxes() {
		List<String> locationNames = new ArrayList<String>();
		locationNames = new LabTimesDbManager(mContext).getAllLocationNames();
		
		// TODO dynamically determine status using db call
		boolean status = true;
		
		for (String location:locationNames){
			addCheckbox(locationNames.indexOf(location), location, status);
		}
	}
	
	public void addCheckbox(int number, String label, boolean status) {
		CheckBox checkBox = new CheckBox(this);
		checkBox.setId(number);
		checkBox.setText(label);
		checkBox.setChecked(status);
		//checkBox.setOnClickListener(getOnClickDoSomething(checkBox));
		filterLinear.addView(checkBox);
    }
	
}
