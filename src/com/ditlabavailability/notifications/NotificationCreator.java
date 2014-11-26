package com.ditlabavailability.notifications;

import java.util.Calendar;

import org.joda.time.format.DateTimeFormatter;

import com.ditlabavailability.helpers.Constants;
import com.ditlabavailability.model.LabTime;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NotificationCreator extends Activity {
	
	DateTimeFormatter fmt = Constants.FMT;
	
	public static void createScheduledNotification(Context mContext, LabTime lab)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.SECOND, 1);

		AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(mContext.ALARM_SERVICE);
		int id = (int) System.currentTimeMillis();
		Intent intent = new Intent(mContext, TimeAlarm.class);
		intent.putExtra("labName", lab.getRoom());
		intent.putExtra("availabilityBoolean", lab.getAvailability());
		intent.putExtra("labUntilTimeHour", Integer.toString(lab.getUntilTime().getHourOfDay()));
		
		PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
	}
}
