package com.ditlabavailability.notifications;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NotificationCreator extends Activity {
	
	public static void createScheduledNotification(Context mContext, int seconds)
	{
		// Get new calendar object and set the date to now
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		// Add defined amount of days to the date
		calendar.add(Calendar.SECOND, seconds);
		
		// Retrieve alarm manager from the system
		AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(mContext.ALARM_SERVICE);
		// Every scheduled intent needs a different ID, else it is just executed once
		int id = (int) System.currentTimeMillis();
		
		// Prepare the intent which should be launched at the date
		Intent intent = new Intent(mContext, TimeAlarm.class);
		
		// Prepare the pending intent
		PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		// Register the alert in the system. You have the option to define if the device has to wake up on the alert or not
		alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
	}
}
