package com.ditlabavailability.notifications;

import java.util.Locale;

import com.ditlabavailability.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

// The class has to extend the BroadcastReceiver to get the notification from the system
public class TimeAlarm extends BroadcastReceiver {

	@Override
	public void onReceive(Context mContext, Intent paramIntent) {

		String availabilityMessage;
		String availLongMessage;

		String labName = paramIntent.getStringExtra("labName");
		String labAvailability = paramIntent.getStringExtra("availability");
		String labUntilTimeHour = paramIntent.getStringExtra("labUntilTimeHour");

		// Request the notification manager
		NotificationManager notificationManager = (NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE);

		// Create a new intent which will be fired if you click on the
		// notification
		Intent intent = new Intent("android.intent.action.VIEW");
		
		// TODO: Notification should tap to bring to lab full view intent.
		//intent.setData(data)

		// Attach the intent to a pending intent
		PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		if (labAvailability.toLowerCase(Locale.ENGLISH) == "available") {
			availabilityMessage = "becoming available";
			availLongMessage = "will be available at";
		} else {
			availabilityMessage = "becoming unavailable";
			availLongMessage = "will become unavailable at";
		}

		Notification noti = new Notification.Builder(mContext)
				.setPriority(200)
				.setContentTitle("Lab " + availabilityMessage)
				.setContentText(
						labName + " " + availLongMessage + " "
								+ labUntilTimeHour + ":00")
				.setSmallIcon(R.drawable.lab_availability_logo)
				.setContentIntent(pendingIntent).build();

		// Fire the notification
		notificationManager.notify(1, noti);
	}

}