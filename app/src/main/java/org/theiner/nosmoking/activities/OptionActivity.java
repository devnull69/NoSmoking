package org.theiner.nosmoking.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.theiner.nosmoking.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class OptionActivity extends Activity {

    private final int DATEPICKER_ID = 4711;

    private int year;
    private int month;
    private int day;

    private TextView editStartDatum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        editStartDatum = (TextView) findViewById(R.id.editStartDatum);
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

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        if(!"".equals(strStartDatum)) {
            day = Integer.parseInt(strStartDatum.substring(0,2));
            month = Integer.parseInt(strStartDatum.substring(3,5)) - 1;
            year = Integer.parseInt(strStartDatum.substring(6,10));
        }

        editStartDatum.setText(strStartDatum);
        editAnzahlZiggis.setText(String.valueOf(anzahlZiggis));
        editAnzahlInPackung.setText(String.valueOf(anzahlInPackung));
        editKostenProPackung.setText(String.valueOf(kostenProPackung));
    }

    public void btnOk_Click(View view) {
        // Prüfen, ob gültiges Datum gewählt wurde
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

    public void setDate(View view) {
        showDialog(DATEPICKER_ID);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DATEPICKER_ID) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            showDate(arg1, arg2+1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        editStartDatum.setText(new StringBuilder().append(day<10?"0"+day:day).append(".")
                .append(month<10?"0"+month:month).append(".").append(year));
    }

}
