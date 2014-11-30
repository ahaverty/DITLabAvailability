package com.ditlabavailability;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormat;

import com.ditlabavailability.adapters.LabCardSubOnlyBaseAdapter;
import com.ditlabavailability.helpers.Constants;
import com.ditlabavailability.helpers.FilterPreferences;
import com.ditlabavailability.helpers.SelectedLabsHelper;
import com.ditlabavailability.model.LabTime;
import com.ditlabavailability.notifications.NotificationCreator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LabViewActivity extends Activity implements View.OnClickListener {

	TextView labNameView;
	TextView labLocationView;
	TextView labAvailabilityView;
	TextView futureLabsTitleView;
	ImageButton reminderButton;

	LabTime primaryLab;
	String labName;

	Context mContext;
	private static final String LOG = "LabViewActivity";

	private FilterPreferences filterPreferences;
	boolean allFiltersEnabled;
	boolean favouritesEnabled;
	int demoTimeHour;
	int demoTimeMinute;

	DateTimeFormatter fmt = Constants.FMT;
	DateTime testCurrentDate;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lab_full_view);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		mContext = getApplicationContext();

		loadPreferences();
		setCurrentDateAndTime();

		reminderButton = (ImageButton) findViewById(R.id.reminder_button);
		reminderButton.setOnClickListener(this);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			labName = extras.getString("lab_name");
		}

		fillLabContent();
	}

	@Override
	public void onClick(View v) {
		NotificationCreator.createScheduledNotification(mContext, primaryLab);
		Toast.makeText(LabViewActivity.this,
				"Reminder set for " + primaryLab.getUntilHourStr(),
				Toast.LENGTH_LONG).show();
	}

	private void loadPreferences() {
		// Load Preferences
		filterPreferences = new FilterPreferences(mContext);
		allFiltersEnabled = filterPreferences.isAllFiltersEnabled();
		favouritesEnabled = filterPreferences.isFavouritesEnabled();
		demoTimeHour = filterPreferences.getDemoTimeHour();
		demoTimeMinute = filterPreferences.getDemoTimeMinute();
	}

	private void setCurrentDateAndTime() {
		testCurrentDate = DateTime.now().withTime(demoTimeHour, demoTimeMinute,
				0, 0);
	}

	private void fillLabContent() {

		Period periodUntilChange;

		SelectedLabsHelper labsHelper = new SelectedLabsHelper(mContext);
		ArrayList<LabTime> labs = labsHelper.getFutureLabsByRoom(labName,
				testCurrentDate);
		labsHelper.closeDb();
		
		if(labs==null){
			Log.w(LOG,
					"Intent could not retrieve extras: lab_name\n"
							+ "Possibly caused by opening intent call from expired notification");
			
			Toast.makeText(mContext, "Error: Lab not found.", Toast.LENGTH_LONG).show();
			Intent startIntent = new Intent(mContext, MainActivity.class);
			startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);        
			mContext.startActivity(startIntent);
			finish();
			return;
		}

		primaryLab = labs.get(0);

		labNameView = (TextView) findViewById(R.id.lab_name);
		labLocationView = (TextView) findViewById(R.id.lab_location);
		labAvailabilityView = (TextView) findViewById(R.id.lab_availability);
		futureLabsTitleView = (TextView) findViewById(R.id.future_labs_header);

		periodUntilChange = new Period(testCurrentDate.withSecondOfMinute(0)
				.withMillisOfSecond(0), primaryLab.getUntilTime());
		String periodUntilStr = PeriodFormat.getDefault().print(
				periodUntilChange);

		// General lab data
		labNameView.setText(primaryLab.getRoom());
		labLocationView.setText(primaryLab.getLocation());

		if (labs.get(0).getAvailability()) {
			labAvailabilityView
					.setText("Available for " + periodUntilStr + ".");
		} else {
			labAvailabilityView.setText("Unavailable for " + periodUntilStr
					+ ".");
			labAvailabilityView.setTextColor(0xffff0000);
		}

		// Rest of lab's times as list for day
		ArrayList<LabTime> restOfLabs = labsMinusFirstlab(labs);

		if (restOfLabs.isEmpty() == false) {
			final ListView restOfTimesList = (ListView) findViewById(R.id.labListView);
			restOfTimesList.setAdapter(new LabCardSubOnlyBaseAdapter(this,
					restOfLabs));
			restOfTimesList.setLongClickable(false);
			restOfTimesList.setClickable(false);
			restOfTimesList.setSelector(android.R.color.transparent);
		} else {
			futureLabsTitleView.setVisibility(View.GONE);
		}

	}

	private ArrayList<LabTime> labsMinusFirstlab(ArrayList<LabTime> labs) {
		labs.remove(0);
		return labs;
	}
}
