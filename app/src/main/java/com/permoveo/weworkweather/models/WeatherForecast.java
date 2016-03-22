package com.permoveo.weworkweather.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by byfieldj on 3/19/16.
 */
public class WeatherForecast implements Serializable {

    private ArrayList mWeatherItems = new ArrayList<WeatherItem>();
    private static final String TAG = "WeatherForecast";

    public WeatherForecast(JSONObject object) {

        try {
            JSONArray listArray = object.getJSONArray("list");

            for (int i = 0; i < listArray.length(); i++) {

                if (i != 0 && i == 5 || i == 13 || i == 21 || i == 37) {
                    JSONObject obj = listArray.getJSONObject(i);


                    WeatherItem weatherItem = new WeatherItem(obj);
                    mWeatherItems.add(weatherItem);
                }

            }

            Log.d(TAG, "WeatherItem list contains -> " + mWeatherItems.size());


        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "Error creating parsing JSON to WeatherForecast object!");
        }


    }

    public ArrayList<WeatherItem> getWeatherItems() {
        return mWeatherItems;
    }


}
