package com.ditlabavailability;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import com.ditlabavailability.adapters.LabCardBaseAdapter;
import com.ditlabavailability.creator.LabCreator;
import com.ditlabavailability.creator.SelectedLabsCreator;
import com.ditlabavailability.data.DataPopulator;
import com.ditlabavailability.data.FiltersDbManager;
import com.ditlabavailability.data.LabTimesDbManager;
import com.ditlabavailability.data.SelectedLabsDbManager;
import com.ditlabavailability.helpers.Constants;
import com.ditlabavailability.helpers.FilterPreferences;
import com.ditlabavailability.helpers.Filterer;
import com.ditlabavailability.helpers.LabGrouper;
import com.ditlabavailability.model.LabTime;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class MainActivity extends Activity {

	// Logcat tag
	private static final String LOG = "MainActivity";

	DateTimeFormatter fmt = Constants.FMT;
	protected String testingDate = DateTime.now().withTime(0, 0, 0, 0)
			.toString(fmt);
	DateTime testCurrentDate;

	private FilterPreferences filterPreferences;
	boolean allFiltersEnabled;
	boolean favouritesEnabled;
	int demoTimeHour;
	int demoTimeMinute;

	protected MenuItem menuItem;
	private Menu mOptionsMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_labs_main_view);

		Log.i(LOG, "onCreate Called");

		loadPreferences();
		setCurrentDateAndTime();

		createActionBar();
		ceateDaysLabData();
		getLabs();
		createFilterDatabase();
	}

	protected void onResume() {
		super.onResume();

		loadPreferences();
		setCurrentDateAndTime();

		// Setup main list of labs
		final ListView lv = (ListView) findViewById(R.id.labListView);
		lv.setAdapter(new LabCardBaseAdapter(getBaseContext(), refreshLabs()));
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		mOptionsMenu = menu;
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_load:
			menuItem = item;
			menuItem.setActionView(R.layout.progress_bar);
			menuItem.expandActionView();
			RefreshLabsInBackground task = new RefreshLabsInBackground();
			task.execute(getApplicationContext());
			break;
		case R.id.menu_filters:
			Intent intent = new Intent(MainActivity.this, FilterActivity.class);
			startActivity(intent);
		default:
			break;
		}
		return true;
	}

	private void createActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME
				| ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);
	}

	private void loadPreferences() {
		// Load Preferences
		filterPreferences = new FilterPreferences(getApplicationContext());
		allFiltersEnabled = filterPreferences.isAllFiltersEnabled();
		favouritesEnabled = filterPreferences.isFavouritesEnabled();
		demoTimeHour = filterPreferences.getDemoTimeHour();
		demoTimeMinute = filterPreferences.getDemoTimeMinute();
	}

	private void setCurrentDateAndTime() {
		testCurrentDate = DateTime.now().withTime(demoTimeHour, demoTimeMinute,
				0, 0);
	}

	private class RefreshLabsInBackground extends
			AsyncTask<Context, Void, ArrayList<LabTime>> {
		private Context mContext;

		protected ArrayList<LabTime> doInBackground(Context... context) {
			mContext = context[0];
			Log.i(LOG, "doInBackground Called");
			return refreshLabs();
		}

		protected void onPostExecute(ArrayList<LabTime> labs) {
			final ListView lv = (ListView) findViewById(R.id.labListView);
			lv.setAdapter(new LabCardBaseAdapter(mContext, labs));

			mOptionsMenu.clear();
			getMenuInflater().inflate(R.menu.main, mOptionsMenu);
			Toast.makeText(mContext, "Labs times have been updated",
					Toast.LENGTH_SHORT).show();
		}
	};

	private ArrayList<LabTime> refreshLabs() {
		Log.i(LOG, "refreshLabs Called");
		ceateDaysLabData();
		return getLabs();
	}

	private void createFilterDatabase() {
		Log.i(LOG, "createFilterDatabase Called");
		List<String> locationNames = new ArrayList<String>();
		FiltersDbManager dbFilters = new FiltersDbManager(
				getApplicationContext());
		LabTimesDbManager dbLabTimes = new LabTimesDbManager(
				getApplicationContext());
		locationNames = dbLabTimes.getAllLocationNames();

		for (String location : locationNames) {
			new FiltersDbManager(getApplicationContext())
					.insertIntoFilterLocationsTable(location, true);
		}

		dbLabTimes.closeDB();
		dbFilters.closeDB();
	}

	private void ceateDaysLabData() {

		LabTimesDbManager dbLabTimes;
		SelectedLabsDbManager dbSelected;

		dbLabTimes = new LabTimesDbManager(getApplicationContext());
		DataPopulator.populate(dbLabTimes);
		dbLabTimes.close();

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
		ArrayList<LabTime> labTimesGroupFuture;

		if (allFiltersEnabled) {
			labTimesGroupFuture = SelectedLabsCreator
					.getLabsAfterTimeWithFilters(testCurrentDate);
		} else {
			labTimesGroupFuture = SelectedLabsCreator
					.getLabsAfterTime(testCurrentDate);
		}

		dbSelected.close();

		ArrayList<LabTime> labTimesFltrAvail = Filterer
				.arrangeGroupedLabsByAvailability(labTimesGroupFuture);

		return labTimesFltrAvail;
	}
}