package com.ditlabavailability;

import java.sql.Timestamp;
import java.util.ArrayList;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		db = new DatabaseHelper(getApplicationContext());

		// TODO re-factor this, change from a static function possibly
		DataPopulator.populate(db);

		LabCreator creator = new LabCreator();

		// TODO create filteredTimetamp using filter activity
		Timestamp filteredTimestamp = Timestamp
				.valueOf("2014-10-27 00:00:00.000");
		ArrayList<LabTime> labTimeResults = creator.createLabInstances(db, filteredTimestamp);
		
        
        final ListView lv = (ListView) findViewById(R.id.labListView);
        lv.setAdapter(new MyCustomBaseAdapter(this, labTimeResults));
         
         
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = lv.getItemAtPosition(position);
                LabTime fullObject = (LabTime)o;
                Toast.makeText(MainActivity.this, "You have chosen: " + " " + fullObject.getRoom(), Toast.LENGTH_LONG).show();
            }
        });
		
		// Closing database connection
		db.closeDB();
	}
}