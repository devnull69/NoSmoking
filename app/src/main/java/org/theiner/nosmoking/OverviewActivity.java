package org.theiner.nosmoking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.theiner.nosmoking.util.DateHelper;
import org.theiner.nosmoking.util.Tempus;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class OverviewActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "NoSmokingFile";

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
            txtNichtraucher.setTextSize(61.0f);
        else
            txtNichtraucher.setTextSize(70.0f);

        txtNichtraucher.setText(tage + (tage == 1 ? " Tag" : " Tagen"));

        txtMonateTage.setText(tempus.toString());

        float euro = tage * kostenProPackung * anzahlZiggis / anzahlInPackung;
        if(euro>9999.99f)
            txtGeld.setTextSize(63.0f);
        else
            txtGeld.setTextSize(70.0f);
        txtGeld.setText("€ " + new DecimalFormat("#.00").format(euro));

        sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        txtAktualisiert.setText("Zuletzt aktualisiert: " + sdf.format(heute.getTime()));

    }

    private void zeigeOptionen() {
        Intent intent = new Intent(this, OptionActivity.class);
        startActivity(intent);
    }

    public static void updatePrefs(Context context, Tempus tempus) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("jahre", tempus.getJahre());
        editor.putInt("monate", tempus.getMonate());
        editor.putLong("gesamtTage", tempus.getGesamtTage());
        editor.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d("NoSmoking", "Service wird gestartet.");

        startService(new Intent(this, CheckDaysMonths.class));

        try {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            String strStartDatum = settings.getString("startDatum", null);

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

        return super.onOptionsItemSelected(item);
    }
}
