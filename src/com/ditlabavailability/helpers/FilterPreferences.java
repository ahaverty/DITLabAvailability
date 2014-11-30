package com.ditlabavailability.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Helper class for the shared preferences for the filter settings.
 * 
 * @author Alan Haverty
 *
 */
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

	/**
	 * Creates new filter preference file and editor.
	 * 
	 * @param mContext
	 */
	public FilterPreferences(Context mContext) {
		filterPreferences = mContext.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		filterPreferencesEditor = filterPreferences.edit();
	}

	/**
	 * @return <h1>True</h1> if KEY_ALL_FILTERS_ENABLED exists in the preference
	 *         file with a value of True, <h1>False</h1> if the value is False
	 *         or if KEY_ALL_FILTERS_ENABLED doesn't exist in the file
	 */
	public boolean isAllFiltersEnabled() {
		return filterPreferences.getBoolean(KEY_ALL_FILTERS_ENABLED, false);
	}

	public void setAllFiltersEnabled(boolean allFiltersEnabled) {
		this.allFiltersEnabled = allFiltersEnabled;
		filterPreferencesEditor.putBoolean(KEY_ALL_FILTERS_ENABLED,
				allFiltersEnabled);
		filterPreferencesEditor.commit();
	}

	/**
	 * @return <h1>True</h1> if KEY_FAVOURITES_ENABLED exists in the preference
	 *         file with a value of True, <h1>False</h1> if the value is False
	 *         or if KEY_FAVOURITES_ENABLED doesn't exist in the file
	 */
	public boolean isFavouritesEnabled() {
		return filterPreferences.getBoolean(KEY_FAVOURITES_ENABLED, false);
	}

	public void setFavouritesEnabled(boolean favouritesEnabled) {
		this.favouritesEnabled = favouritesEnabled;
		filterPreferencesEditor.putBoolean(KEY_FAVOURITES_ENABLED,
				favouritesEnabled);
		filterPreferencesEditor.commit();
	}

	/**
	 * Used only for demo purposes while changing the 'current time' through the
	 * filter intent
	 * 
	 * @return The integer value of KEY_DEMO_TIME_HOUR if it exists in the
	 *         preference file, If the value does not exist, integer 11 will be
	 *         returned.
	 */
	public int getDemoTimeHour() {
		// TODO Time used for demo purposes only, hard-coded hour 11 minute 5 as
		// default
		return filterPreferences.getInt(KEY_DEMO_TIME_HOUR, 11);
	}

	public void setDemoTimeHour(int demoTimeHour) {
		this.demoTimeHour = demoTimeHour;
		filterPreferencesEditor.putInt(KEY_DEMO_TIME_HOUR, demoTimeHour);
		filterPreferencesEditor.commit();
	}

	/**
	 * Used only for demo purposes while changing the 'current time' through the
	 * filter intent
	 * 
	 * @return The integer value of KEY_DEMO_TIME_MINUTE if it exists in the
	 *         preference file, If the value does not exist, integer 5 will be
	 *         returned.
	 */
	public int getDemoTimeMinute() {
		return filterPreferences.getInt(KEY_DEMO_TIME_MINUTE, 5);
	}

	public void setDemoTimeMinute(int demoTimeMinute) {
		this.demoTimeMinute = demoTimeMinute;
		filterPreferencesEditor.putInt(KEY_DEMO_TIME_MINUTE, demoTimeMinute);
		filterPreferencesEditor.commit();
	}

	/**
	 * Set all filter preferences at the same time.
	 * 
	 * @param allFiltersEnabled
	 * @param favouritesEnabled
	 * @param demoTimeHour
	 * @param demoTimeMinute
	 */
	public void setAllPreferences(boolean allFiltersEnabled,
			boolean favouritesEnabled, int demoTimeHour, int demoTimeMinute) {
		setAllFiltersEnabled(allFiltersEnabled);
		setFavouritesEnabled(favouritesEnabled);
		setDemoTimeHour(demoTimeHour);
		setDemoTimeMinute(demoTimeMinute);
	}
}
