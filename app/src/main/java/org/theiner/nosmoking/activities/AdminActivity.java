package org.theiner.nosmoking.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.theiner.nosmoking.R;
import org.theiner.nosmoking.util.AlarmHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminActivity extends AppCompatActivity {

    private static int ALARM_ID = 131313;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        TextView txtJahre = (TextView) findViewById(R.id.txtJahre);
        TextView txtMonate = (TextView) findViewById(R.id.txtMonate);
        TextView txtGesamtTage = (TextView) findViewById(R.id.txtGesamtTage);
        TextView txtLastChecked = (TextView) findViewById(R.id.txtLastChecked);
        TextView txtAlarmGesetzt = (TextView) findViewById(R.id.txtAlarmGesetzt);

        SharedPreferences settings = getSharedPreferences(OverviewActivity.PREFS_NAME, 0);

        int iJahre = settings.getInt("jahre", -1);
        int iMonate = settings.getInt("monate", -1);
        long lGesamtTage = settings.getLong("gesamtTage", -1);
        long lLastChecked = settings.getLong("lastChecked", -1);

        txtJahre.setText(String.valueOf(iJahre));
        txtMonate.setText(String.valueOf(iMonate));
        txtGesamtTage.setText(String.valueOf(lGesamtTage));

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        if(lLastChecked==-1) {
            txtLastChecked.setText("never");
        } else {
            txtLastChecked.setText(sdf.format(lLastChecked));
        }

        txtAlarmGesetzt.setText((AlarmHelper.getPendingIntentFromAlarm(this, ALARM_ID)==null)?"Alarm ist nicht gesetzt!" : "Alarm ist gesetzt!");

    }

    public void btnOk_Click(View view) {
        this.finish();
    }

}
