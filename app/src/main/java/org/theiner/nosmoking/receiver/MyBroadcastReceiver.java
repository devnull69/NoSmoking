package org.theiner.nosmoking.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.theiner.nosmoking.services.AlarmStarterService;
import org.theiner.nosmoking.services.CheckDaysMonthsService;

/**
 * Created by Thomas on 05.02.2016.
 */
public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, CheckDaysMonthsService.class);
        context.startService(serviceIntent);
    }
}
