package com.permoveo.weworkweather.interfaces;

import com.permoveo.weworkweather.models.WeatherForecast;
import com.permoveo.weworkweather.models.WeatherItem;

import java.util.ArrayList;

/**
 * Created by byfieldj on 3/19/16.
 */
public interface OnRequestCompletedListener {

    public abstract void onRequestComplete(WeatherItem item);
    public abstract void onRequestCompleteWithForecast(WeatherForecast forecast);
    public abstract void onError();
}
