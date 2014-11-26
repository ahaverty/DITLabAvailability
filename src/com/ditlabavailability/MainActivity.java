package com.ditlabavailability;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.ditlabavailability.adapters.LabCardBaseAdapter;
import com.ditlabavailability.creator.LabCreator;
import com.ditlabavailability.creator.SelectedLabsCreator;
import com.ditlabavailability.data.DataPopulator;
import com.ditlabavailability.data.FiltersDbManager;
import com.ditlabavailability.data.LabTimesDbManager;
import com.ditlabavailability.data.SelectedLabsDbManager;
import com.ditlabavailability.helpers.Filterer;
import com.ditlabavailability.helpers.LabGrouper;
import com.ditlabavailability.model.LabTime;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class MainActivity extends Activity {

	public DateTimeFormatter fmt = DateTimeFormat
			.forPattern("YYYY-MM-dd HH:mm:ss.SSS");
	protected String testingDate = DateTime.now().withTime(0, 0, 0, 0)
			.toString(fmt);
	protected DateTime testCurrentDate = DateTime.now().withTime(11, 01, 0, 0);
	
	public static final String PREFS_NAME = "FilterPreferences";
	SharedPreferences filterPreferences;
	String KEY_ALL_FILTERS_ENABLED = "allFiltersEnabled";
	String KEY_FAVOURITES_ENABLED = "favouritesEnabled";
	boolean allFiltersEnabled;
	boolean favouritesEnabled;

	protected MenuItem menuItem;
	private Menu mOptionsMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_labs_main_view);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME
				| ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);
		
		// Load Preferences
		filterPreferences = getSharedPreferences(PREFS_NAME, 0);
		allFiltersEnabled = filterPreferences.getBoolean(KEY_ALL_FILTERS_ENABLED, false);
		favouritesEnabled = filterPreferences.getBoolean(KEY_FAVOURITES_ENABLED, false);

		ArrayList<LabTime> labs = refreshLabs();

		createFilterDatabase();

		final ListView lv = (ListView) findViewById(R.id.labListView);
		lv.setAdapter(new LabCardBaseAdapter(this, labs));

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

	private class RefreshLabsInBackground extends
			AsyncTask<Context, Void, ArrayList<LabTime>> {
		private Context mContext;

		protected ArrayList<LabTime> doInBackground(Context... context) {
			mContext = context[0];
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
		ceateDaysLabData();
		return getLabs();
	}

	private void createFilterDatabase() {
		List<String> locationNames = new ArrayList<String>();
		locationNames = new LabTimesDbManager(getApplicationContext())
				.getAllLocationNames();

		for (String location : locationNames) {
			new FiltersDbManager(getApplicationContext())
					.insertIntoFilterLocationsTable(location, true);
		}
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
		ArrayList<LabTime> labTimesGroupFuture;
		
		if(allFiltersEnabled){
			labTimesGroupFuture = SelectedLabsCreator
					.getLabsAfterTimeWithFilters(testCurrentDate);
		}
		else{
			labTimesGroupFuture = SelectedLabsCreator
					.getLabsAfterTime(testCurrentDate);
		}
		
		dbSelected.close();

		ArrayList<LabTime> labTimesFltrAvail = Filterer
				.arrangeGroupedLabsByAvailability(labTimesGroupFuture);

		return labTimesFltrAvail;
	}
}