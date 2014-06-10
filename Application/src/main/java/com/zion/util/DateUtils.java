package com.zion.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static boolean areSameDay(Date date1, Date date2){
        boolean ret = false;
        if(null != date1 && null != date2){
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy", "fr".equals(Locale.getDefault().getLanguage()) ? Locale.FRANCE : Locale.ENGLISH);
            ret = df.format(date1).equals(df.format(date2));
        }
        return ret;
    }
}
