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
		lv.setAdapter(new LabCardBaseAdapter(this, refreshLabs()));

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				Object o = lv.getItemAtPosition(position);
				LabTime fullObject = (LabTime) o;
				
				Intent intent = new Intent(MainActivity.this,
						LabViewActivity.class);
				intent.putExtra("lab_name", fullObject.getRoom());
				startActivity(intent);
			}
		});

	}
	
	private ArrayList<LabTime> refreshLabs() {
		ceateDaysLabData();
		return getLabs();
	}

	private void ceateDaysLabData() {

		LabTimesDbManager dbLabTimes;
		SelectedLabsDbManager dbSelected;

		dbLabTimes = new LabTimesDbManager(getApplicationContext());
		DataPopulator.populate(dbLabTimes);
		dbLabTimes.close();

		// TODO create filteredTimestamp using filter activity
		DateTime filteredTimestamp = DateTime.parse(testingDate, fmt);

		ArrayList<LabTime> initialLabTimes = LabCreator.createAllLabInstances(
				dbLabTimes, filteredTimestamp);

		ArrayList<LabTime> labTimesGrouped = LabGrouper
				.groupSimilarLabsByAvailability(initialLabTimes);

		// insert grouped lab items into local database
		dbSelected = new SelectedLabsDbManager(getApplicationContext());
		SelectedLabsCreator.createSelectedLabs(dbSelected,
				getApplicationContext(), labTimesGrouped);
		dbSelected.close();
		
	}
	
	private ArrayList<LabTime> getLabs() {
		SelectedLabsDbManager dbSelected;
		dbSelected = new SelectedLabsDbManager(getApplicationContext());
		ArrayList<LabTime> labTimesGroupFuture = SelectedLabsCreator
				.getLabsAfterTime(testCurrentDate);
		dbSelected.close();

		ArrayList<LabTime> labTimesFltrAvail = Filterer
				.arrangeGroupedLabsByAvailability(labTimesGroupFuture);

		return labTimesFltrAvail;
	}
}