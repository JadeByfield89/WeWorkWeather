package com.permoveo.weworkweather.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by byfieldj on 3/20/16.
 */
public class DateTimeUtil {


    public String convertDate(long date) {
        long dv = Long.valueOf(date) * 1000;// its need to be in milisecond
        Date df = new java.util.Date(dv);
        String dateString = new SimpleDateFormat("MM dd, yyyy hh:mma").format(df);

        return dateString;
    }

    public String getDayOfWeek() {

        String day ="";


        return day;
    }
}
