package com.permoveo.weworkweather.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.permoveo.weworkweather.fragments.CurrentWeatherFragment;
import com.permoveo.weworkweather.fragments.WeatherForecastFragment;
import com.permoveo.weworkweather.models.WeatherForecast;
import com.permoveo.weworkweather.models.WeatherItem;

import java.util.ArrayList;

/**
 * Created by byfieldj on 3/18/16.
 */
public class WeatherPagerAdapter extends FragmentPagerAdapter {

    private static final int NUM_PAGES = 2;
    private WeatherItem mWeatherItem;
    private WeatherForecast mForecast;

    private CurrentWeatherFragment currentWeatherFragment;
    private WeatherForecastFragment weatherForecastFragment;

    public WeatherPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);

    }


    public void setWeatherItem(WeatherItem item) {
        mWeatherItem = item;
    }

    public void setWeatherForecast(WeatherForecast forecast) {
        mForecast = forecast;
    }

    public void updateCurrentWeather() {
        currentWeatherFragment.updateUI(mWeatherItem);
    }

    public void updateForecast() {
        weatherForecastFragment.updateUI(mForecast);
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                currentWeatherFragment = CurrentWeatherFragment.newInstance(0, mWeatherItem);
                return currentWeatherFragment;
            case 1:
                weatherForecastFragment = weatherForecastFragment.newInstance(1, mForecast);
                return weatherForecastFragment;

            default:
                return null;


        }
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}
