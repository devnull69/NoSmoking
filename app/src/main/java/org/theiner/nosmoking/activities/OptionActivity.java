package org.theiner.nosmoking.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.theiner.nosmoking.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OptionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        EditText editStartDatum = (EditText) findViewById(R.id.editStartDatum);
        EditText editAnzahlZiggis = (EditText) findViewById(R.id.editAnzahlZiggis);
        EditText editAnzahlInPackung = (EditText) findViewById(R.id.editAnzahlInPackung);
        EditText editKostenProPackung = (EditText) findViewById(R.id.editKostenProPackung);

        SharedPreferences settings = getSharedPreferences(OverviewActivity.PREFS_NAME, 0);
        String strStartDatum = settings.getString("startDatum", null);

        if(strStartDatum == null) {
            Button btnCancel = (Button) findViewById(R.id.btnCancel);
            btnCancel.setEnabled(false);
        }

        strStartDatum = settings.getString("startDatum", "");
        int anzahlZiggis = settings.getInt("anzahlZiggis", 25);
        int anzahlInPackung = settings.getInt("anzahlInPackung", 22);
        float kostenProPackung = settings.getFloat("kostenProPackung", 6.0f);


        editStartDatum.setText(strStartDatum);
        editAnzahlZiggis.setText(String.valueOf(anzahlZiggis));
        editAnzahlInPackung.setText(String.valueOf(anzahlInPackung));
        editKostenProPackung.setText(String.valueOf(kostenProPackung));
    }

    public void btnOk_Click(View view) {
        // Prüfen, ob gültiges Datum gewählt wurde
        EditText editStartDatum = (EditText) findViewById(R.id.editStartDatum);
        EditText editAnzahlZiggis = (EditText) findViewById(R.id.editAnzahlZiggis);
        EditText editAnzahlInPackung = (EditText) findViewById(R.id.editAnzahlInPackung);
        EditText editKostenProPackung = (EditText) findViewById(R.id.editKostenProPackung);

        String strStartDatum = editStartDatum.getText().toString();

        SharedPreferences settings = getSharedPreferences(OverviewActivity.PREFS_NAME, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date startDatum = new Date();
        try {
            startDatum = sdf.parse(strStartDatum);
        } catch (ParseException e) {
            try {
                SimpleDateFormat sdf2 = new SimpleDateFormat("ddMMyyyy");
                startDatum = sdf2.parse(strStartDatum);
            } catch (ParseException e1) {
                Toast toast = Toast.makeText(getApplicationContext(), "Ungültiges Datum!", Toast.LENGTH_SHORT);
                toast.show();
                e1.printStackTrace();
            }
        }
        try {
            Date heute = new Date();
            if(heute.getTime() - startDatum.getTime() < 0)
                throw new ParseException("Date must be in the past", 0);

            int anzahlZiggis = Integer.parseInt(editAnzahlZiggis.getText().toString());
            int anzahlInPackung = Integer.parseInt(editAnzahlInPackung.getText().toString());
            float kostenProPackung = Float.parseFloat(editKostenProPackung.getText().toString());

            SharedPreferences.Editor editor = settings.edit();
            editor.putString("startDatum", sdf.format(startDatum));
            editor.putInt("anzahlZiggis", anzahlZiggis);
            editor.putInt("anzahlInPackung", anzahlInPackung);
            editor.putFloat("kostenProPackung", kostenProPackung);
            editor.commit();
            this.finish();
        } catch (ParseException e) {
            Toast toast = Toast.makeText(getApplicationContext(), "Ungültiges Datum!", Toast.LENGTH_SHORT);
            toast.show();
            e.printStackTrace();
        } catch(NumberFormatException e) {
            Toast toast = Toast.makeText(getApplicationContext(), "Falsches Zahlenformat!", Toast.LENGTH_SHORT);
            toast.show();
            e.printStackTrace();
        }

    }

    public void btnCancel_Click(View view) {
        this.finish();
    }
}
