package com.ditlabavailability.notifications;

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
	public void onReceive(Context context, Intent paramIntent) {
		
	// Request the notification manager
	NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	
	// Create a new intent which will be fired if you click on the notification
	Intent intent = new Intent("android.intent.action.VIEW");
	intent.setData(Uri.parse("http://www.papers.ch"));
	
	// Attach the intent to a pending intent
	PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

	Notification noti = new Notification.Builder(context)
    .setContentTitle("Lab available")
    .setContentText("Lab available in 10 minutes")
    .setSmallIcon(R.drawable.lab_availability_logo)
    .setContentIntent(pendingIntent)
    .build();
	
	// Fire the notification
	notificationManager.notify(1, noti);
	}

}