package org.theiner.nosmoking.services;

import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.theiner.nosmoking.R;
import org.theiner.nosmoking.activities.OverviewActivity;
import org.theiner.nosmoking.util.AlarmHelper;
import org.theiner.nosmoking.util.DateHelper;
import org.theiner.nosmoking.util.Tempus;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Thomas on 01.02.2016.
 */
public class AlarmStarterService extends Service {

    private static int ALARM_ID = 131313;

    @Override
    public void onCreate() {
        super.onCreate();

        // Set an alarm for the next time the CheckDaysMonths service should run:
        setAlarm();

        // end the service
        stopSelf();
    }

    public void setAlarm() {

        // Falls er nicht schon läuft
        // Morgens um 2 Uhr
        if(AlarmHelper.getPendingIntentFromAlarm(this, ALARM_ID) == null)
            AlarmHelper.setAlarm(this, ALARM_ID, 2);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
