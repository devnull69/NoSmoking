package org.theiner.nosmoking.util;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Months;

import java.util.Calendar;

/**
 * Created by Thomas on 20.01.2016.
 */
public class DateHelper {
    public static Tempus getTempus(Calendar heute, Calendar nichtrauchenAnfang) {
        int tagHeute = heute.get(Calendar.DAY_OF_MONTH);
        int monatHeute = heute.get(Calendar.MONTH);
        int jahrHeute = heute.get(Calendar.YEAR);

        int tagDesMonats = nichtrauchenAnfang.get(Calendar.DAY_OF_MONTH);

        if(tagHeute<tagDesMonats) {
            monatHeute--;
            if(monatHeute<0) {
                monatHeute = 11;
                jahrHeute--;
            }
        }

        Calendar referenzTag = Calendar.getInstance();
        referenzTag.set(jahrHeute, monatHeute, tagDesMonats);

        LocalDate referenzDate = LocalDate.fromCalendarFields(referenzTag);
        LocalDate nichtrauchenAnfangDate = LocalDate.fromCalendarFields(nichtrauchenAnfang);
        LocalDate heuteDate = LocalDate.fromCalendarFields(heute);

        int monatDiff = Months.monthsBetween(nichtrauchenAnfangDate.withDayOfMonth(1), referenzDate.withDayOfMonth(1)).getMonths();

        //long diffDays = heute.getTimeInMillis() - referenzTag.getTimeInMillis();
        long days = Days.daysBetween(referenzDate, heuteDate).getDays();
        int jahrDiff = 0;

        if(monatDiff > 11) {
            jahrDiff = monatDiff / 12;
            monatDiff = monatDiff - jahrDiff * 12;
        }

        //long diff = heute.getTimeInMillis() - nichtrauchenAnfang.getTimeInMillis();
        long tage = Days.daysBetween(nichtrauchenAnfangDate, heuteDate).getDays();

        Tempus returnValue = new Tempus();
        returnValue.setJahre(jahrDiff);
        returnValue.setMonate(monatDiff);
        returnValue.setTage((int)days);
        returnValue.setGesamtTage(tage);

        return returnValue;
    }
}
