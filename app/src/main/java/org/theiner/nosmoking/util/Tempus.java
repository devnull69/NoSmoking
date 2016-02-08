package org.theiner.nosmoking.util;

/**
 * Created by Thomas on 20.01.2016.
 */
public class Tempus {
    private int jahre;
    private int monate;
    private int tage;
    private long gesamtTage;

    public long getGesamtTage() {
        return gesamtTage;
    }

    public void setGesamtTage(long gesamtTage) {
        this.gesamtTage = gesamtTage;
    }

    public int getJahre() {
        return jahre;
    }

    public void setJahre(int jahre) {
        this.jahre = jahre;
    }

    public int getMonate() {
        return monate;
    }

    public void setMonate(int monate) {
        this.monate = monate;
    }

    public int getTage() {
        return tage;
    }

    public void setTage(int tage) {
        this.tage = tage;
    }

    public String toString() {
        String returnValue = "";
        if(jahre>0)
            returnValue += jahre + (jahre==1 ? " Jahr, " : " Jahre, ");
        if(monate>0)
            returnValue += monate + (monate==1 ? " Monat, " : " Monate, ");
        returnValue += tage + (tage==1 ? " Tag" : " Tage");

        return returnValue;
    }

    public String toStringWithoutDays() {
        String returnValue = "";
        if(jahre>0)
            returnValue += jahre + (jahre==1 ? " Jahr" : " Jahren");
        if(monate>0)
            returnValue += monate + (returnValue.isEmpty()?"":"und ") + (monate==1 ? " Monat" : " Monaten");

        return returnValue;
    }
}
