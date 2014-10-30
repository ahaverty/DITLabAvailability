package com.ditlabavailability;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.ditlabavailability.adapters.LabCardBaseAdapter;
import com.ditlabavailability.creator.LabCreator;
import com.ditlabavailability.creator.SelectedLabsCreator;
import com.ditlabavailability.data.DataPopulator;
import com.ditlabavailability.data.LabTimesDbManager;
import com.ditlabavailability.data.SelectedLabsDbManager;
import com.ditlabavailability.helpers.Filterer;
import com.ditlabavailability.helpers.LabGrouper;
import com.ditlabavailability.model.LabTime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {

	DateTimeFormatter fmt = DateTimeFormat
			.forPattern("YYYY-MM-dd HH:mm:ss.SSS");
	String testingDate = "2014-10-27 00:00:00.000";
	DateTime testCurrentDate = DateTime.parse("2014-10-27 11:05:00.000", fmt);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_labs_main_view);

		final ListView lv = (ListView) findViewById(R.id.labListView);
		lv.setAdapter(new LabCardBaseAdapter(this, ceateLabData()));

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				Object o = lv.getItemAtPosition(position);
				LabTime fullObject = (LabTime) o;
				Toast.makeText(MainActivity.this,
						"You have chosen: " + " " + fullObject.getRoom(),
						Toast.LENGTH_LONG).show();

				Intent intent = new Intent(MainActivity.this,
						LabViewActivity.class);
				intent.putExtra("lab_name", fullObject.getRoom());
				startActivity(intent);
			}
		});

	}

	private ArrayList<LabTime> ceateLabData() {

		LabTimesDbManager dbLabTimes;
		SelectedLabsDbManager dbSelected;

		LabCreator creator = new LabCreator();
		LabGrouper grouper = new LabGrouper();
		Filterer filterer = new Filterer();
		SelectedLabsCreator selectedCreator = new SelectedLabsCreator();

		dbLabTimes = new LabTimesDbManager(getApplicationContext());
		DataPopulator.populate(dbLabTimes);
		dbLabTimes.close();

		// TODO create filteredTimestamp using filter activity
		DateTime filteredTimestamp = DateTime.parse(testingDate, fmt);

		// create all labs, available and unavailable, for specified day
		ArrayList<LabTime> labTimeResults = creator.createLabInstances(
				dbLabTimes, filteredTimestamp);

		// group lab items that are consecutive and of same room & availability
		ArrayList<LabTime> labTimesGrouped = grouper.groupLabs(labTimeResults);

		// insert grouped lab items into local database
		dbSelected = new SelectedLabsDbManager(getApplicationContext());
		selectedCreator.createSelectedLabs(dbSelected, getApplicationContext(),
				labTimesGrouped);
		ArrayList<LabTime> labTimesGroupFuture = selectedCreator
				.getLabsAfterTime(testCurrentDate);
		dbSelected.close();

		ArrayList<LabTime> labTimesFltrAvail = filterer
				.arrangeByAvailability(labTimesGroupFuture);

		return labTimesFltrAvail;
	}
}