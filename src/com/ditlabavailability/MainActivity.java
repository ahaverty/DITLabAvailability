package com.ditlabavailability;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

	// Database Helper
	DatabaseHelper db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		db = new DatabaseHelper(getApplicationContext());

		DataPopulator.populate(db);

	}
}