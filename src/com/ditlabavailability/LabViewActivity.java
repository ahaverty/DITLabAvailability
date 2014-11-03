package com.ditlabavailability;

import java.util.ArrayList;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormat;

import com.ditlabavailability.adapters.LabCardBaseAdapter;
import com.ditlabavailability.adapters.LabCardSubOnlyBaseAdapter;
import com.ditlabavailability.creator.SelectedLabsCreator;
import com.ditlabavailability.model.LabTime;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class LabViewActivity extends Activity implements View.OnClickListener {

	TextView labName;
	TextView labLocation;
	TextView labAvailability;
	TextView futureLabsTitle;
	Button reminderButton;
	
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

		fillLabContent();
	}

	@Override
	public void onClick(View v) {
		finish();
	}

	private void fillLabContent() {

		Period periodUntilChange;

		// TODO get array of labs for this lab name, filtering out past labs and
		// grouping as normal

		ArrayList<LabTime> labs = SelectedLabsCreator.getFutureLabsByRoom(
				getApplicationContext(), roomName, testCurrentDate);

		labName = (TextView) findViewById(R.id.lab_name);
		labLocation = (TextView) findViewById(R.id.lab_location);
		labAvailability = (TextView) findViewById(R.id.lab_availability);
		futureLabsTitle = (TextView) findViewById(R.id.future_labs_header);

		periodUntilChange = new Period(testCurrentDate.withSecondOfMinute(0)
				.withMillisOfSecond(0), labs.get(0).getUntilTime());
		String periodUntilStr = PeriodFormat.getDefault().print(
				periodUntilChange);
		
		// General lab data
		labName.setText(labs.get(0).getRoom());
		labLocation.setText(labs.get(0).getLocation());

		if (labs.get(0).getAvailability()) {
			labAvailability.setText("Available for "
					+ periodUntilStr + ".");
			
			
		} else {
			labAvailability.setText("Unavailable for "
					+ periodUntilStr + ".");
			labAvailability.setTextColor(0xffff0000);
		}
		
		// Rest of lab's times as list for day
		ArrayList<LabTime> restOfLabs = labsMinusFirstlab(labs);
		
		if(restOfLabs.isEmpty() == false){
			final ListView lv = (ListView) findViewById(R.id.labListView);
			lv.setAdapter(new LabCardSubOnlyBaseAdapter(this, restOfLabs));
		}
		else{
			futureLabsTitle.setVisibility(View.GONE);
		}

	}
	
	private ArrayList<LabTime> labsMinusFirstlab(ArrayList<LabTime> labs) {
		labs.remove(0);
		return labs;
	}
}
