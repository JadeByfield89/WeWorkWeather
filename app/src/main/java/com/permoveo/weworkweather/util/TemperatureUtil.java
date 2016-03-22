package com.permoveo.weworkweather.util;

/**
 * Created by byfieldj on 3/19/16.
 */
public class TemperatureUtil {

    public static int fromKelvinToFahrenheit(String kelvins) {

        double inKelvins = Double.parseDouble(kelvins);
        double inFaranheit = 1.8 * (inKelvins - 273) + 32;
        int faranheit = (int) Math.round(inFaranheit);


        return faranheit;
    }
}
