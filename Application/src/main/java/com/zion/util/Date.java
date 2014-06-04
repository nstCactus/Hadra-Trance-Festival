package com.zion.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Date {
    public static boolean areSameDay(java.util.Date date1, java.util.Date date2){
        boolean ret = false;
        if(null != date1 && null != date2){
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            ret = df.format(date1).equals(df.format(date2));
        }
        return ret;
    }
}
