package com.ditlabavailability.notifications;

import java.math.BigInteger;

import com.ditlabavailability.LabViewActivity;
import com.ditlabavailability.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

/**
 * Notification builder used to build the 'becoming available/unavailable'
 * notifications. Requires an Intent from {@link NotificationCreator}
 * 
 * @see {@link BroadcastReceiver}
 * 
 * @author Alan Haverty
 *
 */
public class TimeAlarm extends BroadcastReceiver {

	/**
	 * Receives an Intent from {@link NotificationCreator} and adds lab data to
	 * the notification. The notification is then passed to
	 * {@link NotificationManager} where it is scheduled and delivered at the
	 * requested time.
	 */
	@Override
	public void onReceive(Context mContext, Intent paramIntent) {

		String availabilityMessage;
		String availLongMessage;
		int notificationId;

		String labName = paramIntent.getStringExtra("labName");
		Boolean labAvailabilityBoolean = paramIntent.getBooleanExtra(
				"availabilityBoolean", true);
		String labUntilTimeHour = paramIntent
				.getStringExtra("labUntilTimeHour");

		NotificationManager notificationManager = (NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent labViewIntent = new Intent(mContext, LabViewActivity.class);
		labViewIntent.putExtra("lab_name", labName);

		PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,
				labViewIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		if (labAvailabilityBoolean) {
			availabilityMessage = "becoming unavailable";
			availLongMessage = "will become unavailable at";
		} else {
			availabilityMessage = "becoming available";
			availLongMessage = "will be available at";
		}

		Bitmap largeIcon = BitmapFactory.decodeResource(null,
				R.drawable.lab_availability_logo);

		Notification noti = new Notification.Builder(mContext)
				.setPriority(NotificationCompat.PRIORITY_HIGH)
				.setAutoCancel(true)
				.setContentTitle("Lab " + availabilityMessage)
				.setContentText(
						labName + " " + availLongMessage + " "
								+ labUntilTimeHour + ":00")
				.setSmallIcon(R.drawable.lab_availability_notification_logo)
				.setLargeIcon(largeIcon).setContentIntent(pendingIntent)
				.build();

		notificationId = getLabSpecificId(labName);
		notificationManager.notify(notificationId, noti);
	}

	/**
	 * Creates an ID by parsing a lab room name into an integer value
	 * 
	 * @param labName
	 * @return An ID unique to each lab room
	 */
	private int getLabSpecificId(String labName) {
		StringBuilder sb = new StringBuilder();
		for (char c : labName.toCharArray())
			sb.append((int) c);

		BigInteger bigId = new BigInteger(sb.toString());
		int id = bigId.intValue();
		return id;
	}

}