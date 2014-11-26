package com.ditlabavailability;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormat;

import com.ditlabavailability.adapters.LabCardSubOnlyBaseAdapter;
import com.ditlabavailability.creator.SelectedLabsCreator;
import com.ditlabavailability.model.LabTime;
import com.ditlabavailability.notifications.NotificationCreator;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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

	DateTimeFormatter fmt = new MainActivity().fmt;
	DateTime testCurrentDate = new MainActivity().testCurrentDate;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lab_full_view);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		mContext = getApplicationContext();

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
		Toast.makeText(LabViewActivity.this, "Reminder set for " + primaryLab.getUntilHourStr(),
				Toast.LENGTH_LONG).show();
	}

	private void fillLabContent() {

		Period periodUntilChange;

		// TODO get array of labs for this lab name, filtering out past labs and
		// grouping as normal

		ArrayList<LabTime> labs = SelectedLabsCreator.getFutureLabsByRoom(
				getApplicationContext(), labName, testCurrentDate);

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
			final ListView lv = (ListView) findViewById(R.id.labListView);
			lv.setAdapter(new LabCardSubOnlyBaseAdapter(this, restOfLabs));
		} else {
			futureLabsTitleView.setVisibility(View.GONE);
		}

	}

	private ArrayList<LabTime> labsMinusFirstlab(ArrayList<LabTime> labs) {
		labs.remove(0);
		return labs;
	}
}
