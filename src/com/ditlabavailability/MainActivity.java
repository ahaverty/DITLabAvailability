package com.ditlabavailability;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import com.ditlabavailability.adapters.LabCardBaseAdapter;
import com.ditlabavailability.data.DataPopulator;
import com.ditlabavailability.data.FiltersDbManager;
import com.ditlabavailability.data.LabTimesDbManager;
import com.ditlabavailability.helpers.Constants;
import com.ditlabavailability.helpers.FilterPreferences;
import com.ditlabavailability.helpers.LabsFilterer;
import com.ditlabavailability.helpers.LabGrouper;
import com.ditlabavailability.helpers.LabInstancesCreator;
import com.ditlabavailability.helpers.SelectedLabsHelper;
import com.ditlabavailability.model.LabTime;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

	private Context mContext;

	private DateTimeFormatter mFmt = Constants.FMT;
	private String mTestingDate = DateTime.now().withTime(0, 0, 0, 0)
			.toString(mFmt);
	private DateTime mTestCurrentDate;

	private MenuItem mMenuItem;
	private Menu mOptionsMenu;

	private FilterPreferences mFilterPreferences;
	private boolean mAllFiltersEnabled;
	private int mDemoTimeHour;
	private int mDemoTimeMinute;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.all_labs_main_view);

		mContext = getApplicationContext();

		createActionBar();
		loadPreferences();
		setCurrentDateAndTime();

		createDaysLabData();
		getGroupedFilteredArrangedLabs();
		createFilterDatabase();
	}

	@Override
	protected void onResume() {
		super.onResume();

		loadPreferences();
		setCurrentDateAndTime();

		// Setup main list of labs
		final ListView mMainLabListView = (ListView) findViewById(R.id.labListView);
		mMainLabListView.setAdapter(new LabCardBaseAdapter(mContext,
				refreshLabDbAndReturnLabs()));
		mMainLabListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				Object unparsedLabObject = mMainLabListView
						.getItemAtPosition(position);
				LabTime lab = (LabTime) unparsedLabObject;

				Intent labViewIntent = new Intent(MainActivity.this,
						LabViewActivity.class);
				labViewIntent.putExtra("lab_name", lab.getRoom());
				startActivity(labViewIntent);
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
			mMenuItem = item;
			mMenuItem.setActionView(R.layout.progress_bar);
			mMenuItem.expandActionView();
			RefreshLabsAndListviewInBackground task = new RefreshLabsAndListviewInBackground();
			task.execute(mContext);
			break;
		case R.id.menu_filters:
			Intent filtersIntent = new Intent(MainActivity.this,
					FilterActivity.class);
			startActivity(filtersIntent);
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
		mFilterPreferences = new FilterPreferences(mContext);
		mAllFiltersEnabled = mFilterPreferences.isAllFiltersEnabled();
		mDemoTimeHour = mFilterPreferences.getDemoTimeHour();
		mDemoTimeMinute = mFilterPreferences.getDemoTimeMinute();
	}

	// TODO Setting current date and time for demo purposes only.
	private void setCurrentDateAndTime() {
		mTestCurrentDate = DateTime.now().withTime(mDemoTimeHour,
				mDemoTimeMinute, 0, 0);
	}

	private class RefreshLabsAndListviewInBackground extends
			AsyncTask<Context, Void, ArrayList<LabTime>> {
		private Context mContext;

		protected ArrayList<LabTime> doInBackground(Context... context) {
			// TODO re-factor, no need for context to be passed in
			this.mContext = context[0];
			return refreshLabDbAndReturnLabs();
		}

		protected void onPostExecute(ArrayList<LabTime> labs) {
			// TODO Re-factor listview object and assigning, avoid recreating
			final ListView lv = (ListView) findViewById(R.id.labListView);
			lv.setAdapter(new LabCardBaseAdapter(mContext, labs));

			mOptionsMenu.clear();
			getMenuInflater().inflate(R.menu.main, mOptionsMenu);
			Toast.makeText(mContext, "Labs have been updated",
					Toast.LENGTH_SHORT).show();
		}
	};

	private ArrayList<LabTime> refreshLabDbAndReturnLabs() {
		createDaysLabData();
		return getGroupedFilteredArrangedLabs();
	}

	private void createFilterDatabase() {
		List<String> locationNames = new ArrayList<String>();

		FiltersDbManager dbFilters = new FiltersDbManager(mContext);
		LabTimesDbManager dbLabTimes = new LabTimesDbManager(mContext);

		locationNames = dbLabTimes.getAllLocationNames();
		dbLabTimes.closeDB();

		for (String location : locationNames) {
			dbFilters.insertIntoFilterLocationsTable(location, true);
		}

		dbFilters.closeDB();
	}

	private void createDaysLabData() {

		DataPopulator.populate(mContext);

		DateTime filteredTimestamp = DateTime.parse(mTestingDate, mFmt);

		ArrayList<LabTime> initialLabTimes = LabInstancesCreator.createAllLabInstances(
				mContext, filteredTimestamp);

		ArrayList<LabTime> labTimesGrouped = LabGrouper
				.groupSimilarLabsByAvailability(initialLabTimes);

		// insert grouped lab items into local database
		SelectedLabsHelper labsHelper = new SelectedLabsHelper(mContext);
		labsHelper.createSelectedLabs(labTimesGrouped);
		labsHelper.closeDb();
	}

	private ArrayList<LabTime> getGroupedFilteredArrangedLabs() {
		SelectedLabsHelper labsHelper = new SelectedLabsHelper(mContext);
		ArrayList<LabTime> labTimesGroupFuture;

		if (mAllFiltersEnabled) {
			labTimesGroupFuture = labsHelper
					.getLabsAfterTimeWithFilters(mTestCurrentDate);
		} else {
			labTimesGroupFuture = labsHelper
					.getLabsAfterTime(mTestCurrentDate);
		}
		
		labsHelper.closeDb();

		ArrayList<LabTime> labTimes = LabsFilterer
				.arrangeGroupedLabsByAvailability(labTimesGroupFuture);

		return labTimes;
	}
}