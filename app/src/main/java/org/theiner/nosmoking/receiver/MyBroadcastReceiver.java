package org.theiner.nosmoking.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.theiner.nosmoking.services.CheckDaysMonths;

/**
 * Created by Thomas on 05.02.2016.
 */
public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, CheckDaysMonths.class);
        context.startService(serviceIntent);
    }
}
