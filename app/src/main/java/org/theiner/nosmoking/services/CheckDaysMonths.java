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

import org.theiner.nosmoking.activities.OverviewActivity;
import org.theiner.nosmoking.R;
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
public class CheckDaysMonths extends Service {

    private static int ALARM_ID = 131313;

    private final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences settings = getSharedPreferences(OverviewActivity.PREFS_NAME, MODE_PRIVATE);
        int prevJahre = settings.getInt("jahre", 0);
        int prevMonate = settings.getInt("monate", 0);
        long prevGesamtTage = settings.getLong("gesamtTage", -1);

        Calendar heute = Calendar.getInstance();

        Calendar nichtrauchenAnfang = Calendar.getInstance();

        // hole Werte aus den Settings
        String strStartDatum = settings.getString("startDatum", "18.09.2015");
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        try {
            nichtrauchenAnfang.setTime(sdf.parse(strStartDatum));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Tempus tempus = DateHelper.getTempus(heute, nichtrauchenAnfang);

        // Nur vergleichen, wenn es schon Vergleichswerte gibt!
        if(prevGesamtTage>-1) {
            // Ist ein Jahr oder ein weiterer Monat vergangen?
            long gesamtTage = tempus.getGesamtTage();
            if (tempus.getJahre() > prevJahre || tempus.getMonate() > prevMonate) {
                sendNotification(tempus.toStringWithoutDays());
            } else if (Math.floor(gesamtTage / 100) > Math.floor(prevGesamtTage / 100)) {
                sendNotification(gesamtTage + (gesamtTage == 1 ? " Tag" : " Tagen"));
            }
        }

        OverviewActivity.updatePrefs(this, tempus);

        // Set an alarm for the next time this service should run:
        setAlarm();

        // end the service
        stopSelf();
    }

    public void setAlarm() {

        // Morgens um 2 Uhr
        AlarmHelper.setAlarm(this, ALARM_ID, 2);
    }

    public void sendNotification(String notifyText) {

        if(isNotificationEnabled(this)) {
            Intent mainIntent = new Intent(this, OverviewActivity.class);
            @SuppressWarnings("deprecation")
            Notification noti = new Notification.Builder(this)
                    .setAutoCancel(true)
                    .setContentIntent(PendingIntent.getActivity(this, 131314, mainIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT))
                    .setContentTitle("Gratuliere!")
                    .setContentText("Du bist seit " + notifyText + " rauchfrei!")
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setSmallIcon(R.mipmap.icon_white)
                    .setTicker("Du bist seit " + notifyText + " rauchfrei!")
                    .setWhen(System.currentTimeMillis())
                    .getNotification();

            NotificationManager notificationManager
                    = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(131315, noti);
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private boolean isNotificationEnabled(Context context) {

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);

        ApplicationInfo appInfo = context.getApplicationInfo();

        String pkg = context.getApplicationContext().getPackageName();

        int uid = appInfo.uid;

        Class appOpsClass = null; /* Context.APP_OPS_MANAGER */

        try {

            appOpsClass = Class.forName(AppOpsManager.class.getName());

            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);

            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
            int value = (int)opPostNotificationValue.get(Integer.class);

            return ((int)checkOpNoThrowMethod.invoke(mAppOps,value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }
}
