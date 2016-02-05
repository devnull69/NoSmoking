package org.theiner.nosmoking.util;

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

        int monatDiff = Months.monthsBetween(nichtrauchenAnfangDate.withDayOfMonth(1), referenzDate.withDayOfMonth(1)).getMonths();

        long diffDays = heute.getTimeInMillis() - referenzTag.getTimeInMillis();
        float days = ((float) diffDays) / (24 * 60 * 60 * 1000);
        int jahrDiff = 0;

        if(monatDiff > 11) {
            jahrDiff = monatDiff / 12;
            monatDiff = monatDiff - jahrDiff * 12;
        }

        long diff = heute.getTimeInMillis() - nichtrauchenAnfang.getTimeInMillis();
        long tage = (long) (diff / (24 * 60 * 60 * 1000));

        Tempus returnValue = new Tempus();
        returnValue.setJahre(jahrDiff);
        returnValue.setMonate(monatDiff);
        returnValue.setTage((int) (days + 0.5));
        returnValue.setGesamtTage(tage);

        return returnValue;
    }
}
