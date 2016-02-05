package org.theiner.nosmoking.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import org.joda.time.DateTime;
import org.theiner.nosmoking.services.CheckDaysMonths;

/**
 * Created by Thomas on 04.02.2016.
 */
public class AlarmHelper {

    public static PendingIntent getPendingIntentFromAlarm(Context context, int alarmId) {
        return (PendingIntent.getService(context, alarmId,
                new Intent(context, CheckDaysMonths.class),
                PendingIntent.FLAG_NO_CREATE));
    }

    public static void setAlarm(Context context, int alarmId, int hourOfDay) {
        Intent serviceIntent = new Intent(context, CheckDaysMonths.class);
        PendingIntent pi = PendingIntent.getService(context, alarmId, serviceIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // How long until tomorrow to its hourOfDay?
        DateTime tomorrow = (new DateTime()).plusDays(1);
        DateTime tomorrowAtHourOfDay = new DateTime(tomorrow.getYear(), tomorrow.getMonthOfYear(), tomorrow.getDayOfMonth(), hourOfDay, 0);


        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, tomorrowAtHourOfDay.getMillis(), pi);
    }
}
