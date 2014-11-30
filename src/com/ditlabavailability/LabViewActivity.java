package com.ditlabavailability;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormat;

import com.ditlabavailability.adapters.LabCardSubOnlyBaseAdapter;
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

/**
 * This activity is activated on pressing a lab from the main_activity view. The
 * activity displays a single lab's details along with future hours and the
 * hours availability status. The activity also provides an alarm/notification
 * creator button so that the user can notify themselves when a lab is becoming
 * available or unavailable.
 * 
 * @author Alan Haverty
 *
 */
public class LabViewActivity extends Activity implements View.OnClickListener {

	private TextView mLabNameView;
	private TextView mLabLocationView;
	private TextView mLabAvailabilityView;
	private TextView mFutureLabsTitleView;
	private ImageButton mReminderButton;

	private LabTime mPrimaryLab;
	private String mLabName;

	private Context mContext;
	private static final String LOG = "LabViewActivity";

	private FilterPreferences filterPreferences;
	boolean allFiltersEnabled;
	boolean favouritesEnabled;
	int demoTimeHour;
	int demoTimeMinute;

	private DateTime mTestCurrentDate;

	/**
	 * Create the specific lab view, setup action bar with back arrow, load
	 * preferences, set current debug/demo timestamp, setup the reminder button,
	 * get the extras sent from the main_activity intent, fill the views with
	 * the lab content.
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lab_full_view);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		mContext = getApplicationContext();

		loadPreferences();
		setCurrentDateAndTime();

		mReminderButton = (ImageButton) findViewById(R.id.reminder_button);
		mReminderButton.setOnClickListener(this);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			mLabName = extras.getString("lab_name");
		}

		fillLabContent();
	}

	/**
	 * onClick call the method to create a notification for this lab, display a
	 * toast message to the user confirming it has been created.
	 */
	@Override
	public void onClick(View v) {
		NotificationCreator.createScheduledNotification(mContext, mPrimaryLab);
		Toast.makeText(LabViewActivity.this,
				"Reminder set for " + mPrimaryLab.getUntilHourStr(),
				Toast.LENGTH_LONG).show();
	}

	/**
	 * Load shared preferences file for filter settings and initialize class
	 * variables
	 */
	private void loadPreferences() {
		filterPreferences = new FilterPreferences(mContext);
		allFiltersEnabled = filterPreferences.isAllFiltersEnabled();
		favouritesEnabled = filterPreferences.isFavouritesEnabled();
		demoTimeHour = filterPreferences.getDemoTimeHour();
		demoTimeMinute = filterPreferences.getDemoTimeMinute();
	}

	private void setCurrentDateAndTime() {
		mTestCurrentDate = DateTime.now().withTime(demoTimeHour,
				demoTimeMinute, 0, 0);
	}

	/**
	 * Fills the lab view with the labs data, taken from the Selected Labs
	 * Database using the intent.extra:lab_name as the basis
	 */
	private void fillLabContent() {

		Period periodUntilChange;

		SelectedLabsHelper labsHelper = new SelectedLabsHelper(mContext);
		ArrayList<LabTime> labs = labsHelper.getFutureLabsByRoom(mLabName,
				mTestCurrentDate);
		labsHelper.closeDb();
		
		// TODO Better control the expired notification error.
		// Catches error where user clicks a notification after the intent has expired.
		if (labs == null) {
			Log.w(LOG,
					"Intent could not retrieve extras: lab_name\n"
							+ "Possibly caused by opening intent call from expired notification");

			Toast.makeText(mContext, "Error: Lab not found.", Toast.LENGTH_LONG)
					.show();
			Intent startIntent = new Intent(mContext, MainActivity.class);
			startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(startIntent);
			finish();
			return;
		}

		mPrimaryLab = labs.get(0);

		mLabNameView = (TextView) findViewById(R.id.lab_name);
		mLabLocationView = (TextView) findViewById(R.id.lab_location);
		mLabAvailabilityView = (TextView) findViewById(R.id.lab_availability);
		mFutureLabsTitleView = (TextView) findViewById(R.id.future_labs_header);

		periodUntilChange = new Period(mTestCurrentDate.withSecondOfMinute(0)
				.withMillisOfSecond(0), mPrimaryLab.getUntilTime());
		String periodUntilStr = PeriodFormat.getDefault().print(
				periodUntilChange);

		// General lab data
		mLabNameView.setText(mPrimaryLab.getRoom());
		mLabLocationView.setText(mPrimaryLab.getLocation());

		if (labs.get(0).getAvailability()) {
			mLabAvailabilityView.setText("Available for " + periodUntilStr
					+ ".");
		} else {
			mLabAvailabilityView.setText("Unavailable for " + periodUntilStr
					+ ".");
			mLabAvailabilityView.setTextColor(0xffff0000);
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
			mFutureLabsTitleView.setVisibility(View.GONE);
		}

	}

	/**
	 * Removes the first lab (i.e: labList[0]) from an ArrayList and returns the
	 * amended list
	 * 
	 * @param labs
	 * @return the passed in ArrayList of labs, of type {@link LabTime}, without
	 *         the first lab item in the list
	 */
	private ArrayList<LabTime> labsMinusFirstlab(ArrayList<LabTime> labs) {
		labs.remove(0);
		return labs;
	}
}
