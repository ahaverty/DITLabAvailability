package com.ditlabavailability.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class FilterPreferences extends Activity {

	private final String PREFS_NAME = "FilterPreferences";
	private SharedPreferences filterPreferences;
	private SharedPreferences.Editor filterPreferencesEditor;

	private final String KEY_ALL_FILTERS_ENABLED = "allFiltersEnabled";
	private final String KEY_FAVOURITES_ENABLED = "favouritesEnabled";

	private final String KEY_DEMO_TIME_HOUR = "demoTimeHour";
	private final String KEY_DEMO_TIME_MINUTE = "demoTimeMinute";

	boolean allFiltersEnabled;
	boolean favouritesEnabled;
	int demoTimeHour;
	int demoTimeMinute;

	public FilterPreferences(Context mContext) {
		filterPreferences = mContext.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		filterPreferencesEditor = filterPreferences.edit();
	}

	public boolean isAllFiltersEnabled() {
		return filterPreferences.getBoolean(KEY_ALL_FILTERS_ENABLED, false);
	}

	public void setAllFiltersEnabled(boolean allFiltersEnabled) {
		this.allFiltersEnabled = allFiltersEnabled;
		filterPreferencesEditor.putBoolean(KEY_ALL_FILTERS_ENABLED,
				allFiltersEnabled);
		filterPreferencesEditor.commit();
	}

	public boolean isFavouritesEnabled() {
		return filterPreferences.getBoolean(KEY_FAVOURITES_ENABLED, false);
	}

	public void setFavouritesEnabled(boolean favouritesEnabled) {
		this.favouritesEnabled = favouritesEnabled;
		filterPreferencesEditor.putBoolean(KEY_FAVOURITES_ENABLED,
				favouritesEnabled);
		filterPreferencesEditor.commit();
	}

	// Time used for demo purposes only, hard-coded hour 11 minute 5 as default
	public int getDemoTimeHour() {
		return filterPreferences.getInt(KEY_DEMO_TIME_HOUR, 11);
	}

	public void setDemoTimeHour(int demoTimeHour) {
		this.demoTimeHour = demoTimeHour;
		filterPreferencesEditor.putInt(KEY_DEMO_TIME_HOUR, demoTimeHour);
		filterPreferencesEditor.commit();
	}

	public int getDemoTimeMinute() {
		return filterPreferences.getInt(KEY_DEMO_TIME_MINUTE, 5);
	}

	public void setDemoTimeMinute(int demoTimeMinute) {
		this.demoTimeMinute = demoTimeMinute;
		filterPreferencesEditor.putInt(KEY_DEMO_TIME_MINUTE, demoTimeMinute);
		filterPreferencesEditor.commit();
	}

	public void setAllPreferences(boolean allFiltersEnabled,
			boolean favouritesEnabled, int demoTimeHour, int demoTimeMinute) {
		setAllFiltersEnabled(allFiltersEnabled);
		setFavouritesEnabled(favouritesEnabled);
		setDemoTimeHour(demoTimeHour);
		setDemoTimeMinute(demoTimeMinute);
	}
}
