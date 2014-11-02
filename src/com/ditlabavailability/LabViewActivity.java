package com.ditlabavailability;

import java.util.ArrayList;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormat;

import com.ditlabavailability.creator.SelectedLabsCreator;
import com.ditlabavailability.model.LabTime;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LabViewActivity extends Activity implements View.OnClickListener {

	TextView labName;
	TextView labLocation;
	TextView labAvailability;
	String roomName;

	DateTimeFormatter fmt = DateTimeFormat
			.forPattern("YYYY-MM-dd HH:mm:ss.SSS");
	DateTime testCurrentDate = DateTime.parse("2014-10-27 11:05:00.000", fmt);

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lab_full_view);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			roomName = extras.getString("lab_name");
		}

		createLabContent();
	}

	@Override
	public void onClick(View v) {
		finish();
	}

	private void createLabContent() {

		Period periodUntilChange;

		// TODO get array of labs for this lab name, filtering out past labs and
		// grouping as normal

		ArrayList<LabTime> labs = SelectedLabsCreator.getFutureLabsByRoom(
				getApplicationContext(), roomName, testCurrentDate);

		labName = (TextView) findViewById(R.id.lab_name);
		labLocation = (TextView) findViewById(R.id.lab_location);
		labAvailability = (TextView) findViewById(R.id.lab_availability);

		periodUntilChange = new Period(testCurrentDate.withSecondOfMinute(0)
				.withMillisOfSecond(0), labs.get(0).getUntilTime());
		String periodUntilStr = PeriodFormat.getDefault().print(
				periodUntilChange);

		labName.setText(labs.get(0).getRoom());
		labLocation.setText(labs.get(0).getLocation());
		labAvailability.setText("This lab is "
				+ labs.get(0).getAvailabilityStr().toLowerCase(Locale.ENGLISH)
				+ " for the next " + periodUntilStr);

		if (!labs.get(0).getAvailability()) {
			labAvailability.setText("This lab will be free in "
					+ periodUntilStr);
			labAvailability.setTextColor(0xffff0000);
		} else {
			labAvailability.setText("This lab is available for the next "
					+ periodUntilStr);
		}

//		final ListView lv = (ListView) findViewById(R.id.labListView);
//		lv.setAdapter(new LabCardBaseAdapter(this, ceateLabData()));

	}
}
