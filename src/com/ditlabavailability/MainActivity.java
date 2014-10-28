package com.ditlabavailability;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.ditavailability.filter.Filterer;
import com.ditlabavailability.grouper.LabGrouper;
import com.ditlabavailability.labcreation.LabCreator;
import com.ditlabavailability.model.LabTime;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {

	// Database Helper
	DatabaseHelper db;
	
	DateTimeFormatter fmt = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss.SSS");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		db = new DatabaseHelper(getApplicationContext());

		// TODO re-factor this, change from a static function possibly
		DataPopulator.populate(db);

		LabCreator creator = new LabCreator();
		LabGrouper grouper = new LabGrouper();
		Filterer filterer = new Filterer();
		
		String testingDate = "2014-10-27 00:00:00.000";
		DateTime testCurrentDate = DateTime.parse("2014-10-27 11:05:00.000", fmt);
		
		// TODO create filteredTimestamp using filter activity
		DateTime filteredTimestamp = DateTime.parse(testingDate, fmt);
		ArrayList<LabTime> labTimeResults = creator.createLabInstances(db, filteredTimestamp);
		
		// Closing database connection
		db.closeDB();
		
		ArrayList<LabTime> labTimesGrouped = grouper.groupLabs(labTimeResults);
		ArrayList<LabTime> labTimesGroupFuture = filterer.removePastLabsUsingUntil(labTimesGrouped, testCurrentDate);
        ArrayList<LabTime> labTimesFltrAvail = filterer.arrangeByAvailability(labTimesGroupFuture);
		
        final ListView lv = (ListView) findViewById(R.id.labListView);
        lv.setAdapter(new MyCustomBaseAdapter(this, labTimesFltrAvail));
         
         
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = lv.getItemAtPosition(position);
                LabTime fullObject = (LabTime)o;
                Toast.makeText(MainActivity.this, "You have chosen: " + " " + fullObject.getRoom(), Toast.LENGTH_LONG).show();
            }
        });
		
		
	}
}