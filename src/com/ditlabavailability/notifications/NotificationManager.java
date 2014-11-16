package com.ditlabavailability.notifications;

import com.ditlabavailability.MainActivity;

import android.app.AlarmManager;
import android.content.Intent;

public class NotificationManager {
	
	
//	public void intentThing(){
//		Intent myIntent = new Intent();  
//		AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
//	}
}
//	Intent myIntent = new Intent(ThisApp.this , myService.class);     
//    AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
//    pendingIntent = PendingIntent.getService(ThisApp.this, 0, myIntent, 0);
//
//    Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 12);
//    calendar.set(Calendar.MINUTE, 00);
//    calendar.set(Calendar.SECOND, 00);
//
//   alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24*60*60*1000 , pendingIntent);  //set repeating every 24 hours
//}


//NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//Notification notify = new Notification(R.drawable.icon,"Its Time to Eat",when);
//
//Context context = GrubNOWActivity.this;
//CharSequence title = "Its Time to Eat";
//CharSequence details = "Click Here to Search for Restaurants";
//Intent intent = new Intent(context,Search.class);
//PendingIntent pending = PendingIntent.getActivity(context, 0, intent, 0);
//notify.setLatestEventInfo(context, title, details, pending);
//nm.notify(0,notify);