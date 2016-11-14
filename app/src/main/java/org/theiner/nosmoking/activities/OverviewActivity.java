package org.theiner.nosmoking.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.theiner.nosmoking.R;
import org.theiner.nosmoking.context.NoSmokingApplication;
import org.theiner.nosmoking.services.AlarmStarterService;
import org.theiner.nosmoking.services.CheckDaysMonthsService;
import org.theiner.nosmoking.util.DateHelper;
import org.theiner.nosmoking.util.Tempus;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class OverviewActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "NoSmokingFile";

    private static int clickCount = 0;
    private static boolean isAdmin = false;

    private NoSmokingApplication myApp;

    private void zeigeWerte() throws ParseException {
        TextView txtNichtraucher = (TextView) findViewById(R.id.txtNichtraucher);
        TextView txtGeld = (TextView) findViewById(R.id.txtGeld);
        TextView txtAktualisiert = (TextView) findViewById(R.id.txtAktualisiert);
        TextView txtMonateTage = (TextView) findViewById(R.id.txtMonateTage);

        Calendar heute = Calendar.getInstance();

        Calendar nichtrauchenAnfang = Calendar.getInstance();

        // hole Werte aus den Settings
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String strStartDatum = settings.getString("startDatum", "18.09.2015");
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        nichtrauchenAnfang.setTime(sdf.parse(strStartDatum));

        int anzahlZiggis = settings.getInt("anzahlZiggis", 25);
        int anzahlInPackung = settings.getInt("anzahlInPackung", 22);
        float kostenProPackung = settings.getFloat("kostenProPackung", 6.0f);

        Tempus tempus = DateHelper.getTempus(heute, nichtrauchenAnfang);
        long tage = tempus.getGesamtTage();
        if(tage>999)
            txtNichtraucher.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 61.0f);
        else
            txtNichtraucher.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 70.0f);

        txtNichtraucher.setText(tage + (tage == 1 ? " Tag" : " Tagen"));

        txtMonateTage.setText(tempus.toString());

        float euro = tage * kostenProPackung * anzahlZiggis / anzahlInPackung;
        if(euro>9999.99f)
            txtGeld.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 63.0f);
        else
            txtGeld.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 70.0f);
        txtGeld.setText("€ " + new DecimalFormat("#.00").format(euro));

        sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        txtAktualisiert.setText("Zuletzt aktualisiert: " + sdf.format(heute.getTime()));

    }

    private void zeigeOptionen() {
        Intent intent = new Intent(this, OptionActivity.class);
        startActivity(intent);
    }

    private void zeigeAdmin() {
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
    }

    public static void updatePrefs(Context context, Tempus tempus) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("jahre", tempus.getJahre());
        editor.putInt("monate", tempus.getMonate());
        editor.putLong("gesamtTage", tempus.getGesamtTage());
        editor.putLong("lastChecked", System.currentTimeMillis());
        editor.commit();
    }

    public void setAdminMode() {
        isAdmin = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);

        myApp = (NoSmokingApplication) getApplicationContext();

        final Activity that = this;

        toolbar.setOnClickListener(new View.OnClickListener() {
            private int clickCount = 0;

            @Override
            public void onClick(View v) {
                clickCount++;
                if (clickCount >= 7) {
                    clickCount = 0;
                    setAdminMode();
                    invalidateOptionsMenu();
                    Toast.makeText(that, "Admin mode enabled", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Log.d("NoSmoking", "Service wird gestartet.");

        startService(new Intent(this, AlarmStarterService.class));

        try {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            String strStartDatum = settings.getString("startDatum", null);

            // Density metrics merken für Large Icon der Notification
            DisplayMetrics metrics = this.getResources().getDisplayMetrics();
            float multiplier = metrics.density/3f;   // Bitmap liegt mit 480dpi vor (density Faktor 3), die Bildschirmauflösung kann aber geringer sein

            SharedPreferences.Editor myEditor = settings.edit();
            myEditor.putFloat("multiplier", multiplier);
            myEditor.commit();

            if(strStartDatum != null) {
                zeigeWerte();
            } else {
                zeigeOptionen();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            zeigeWerte();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_overview, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if(isAdmin)
            menu.getItem(1).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // OptionActivity aufrufen
            zeigeOptionen();
        }

        if(id == R.id.action_close) {
            System.exit(0);
        }

        if(id == R.id.action_admin) {
            zeigeAdmin();
        }

        return super.onOptionsItemSelected(item);
    }

}
