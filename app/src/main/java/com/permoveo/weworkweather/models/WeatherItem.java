package com.permoveo.weworkweather.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by byfieldj on 3/19/16.
 */
public class WeatherItem implements Serializable {

    private static final String TAG = "WeatherItem";

    private String mTemp;
    private String mDescription;
    private String mShortDescription;

    private String mPressure;
    private String mTempMin, mTempMax;
    private String mWindSpeed;
    private String mHumidity;
    private String mLastUpdatedDate;

    private String mName;

    public WeatherItem(JSONObject object) {

        try {
            JSONObject mainObj = object.getJSONObject("main");
            mTemp = mainObj.getString("temp");
            setTemperature(mTemp);
            Log.d(TAG, "Temperature -> " + mTemp);

            mHumidity = mainObj.getString("humidity");
            setHumidity(mHumidity);
            Log.d(TAG, "Humidity -> " + mHumidity);

            mPressure = mainObj.getString("pressure");
            setPressure(mPressure);
            Log.d(TAG, "Pressure -> " + mPressure);


            JSONArray weatherArray = object.getJSONArray("weather");
            JSONObject weatherObj = weatherArray.getJSONObject(0);
            mShortDescription = weatherObj.getString("main");
            setShortDescription(mShortDescription);
            Log.d(TAG, "Short Description -> " + mShortDescription);

            mTempMax = mainObj.getString("temp_max");
            Log.d(TAG, "Temp HIGH -> " + mTempMax);
            setTempMax(mTempMax);

            mTempMin = mainObj.getString("temp_min");
            Log.d(TAG, "Temp LOW -> " + mTempMin);
            setTempMin(mTempMin);

            mDescription = weatherObj.getString("description");
            setDescription(mDescription);
            Log.d(TAG, "Description -> " + mDescription);

            mLastUpdatedDate = object.getString("dt");
            setLastUpdatedDate(mLastUpdatedDate);
            Log.d(TAG, "Last updated at -> " + mLastUpdatedDate);

            JSONObject windObj = object.getJSONObject("wind");
            mWindSpeed = windObj.getString("speed");
            setWindspeed(mWindSpeed);
            Log.d(TAG, "Windspeed -> " + mWindSpeed);


            if (object.has("name")) {
                try {
                    mName = object.getString("name");
                    setName(mName);
                    Log.d(TAG, "Name -> " + mName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "Error parsing JSON to a WeatherItem!");
        }

    }


    public void setTemperature(String temp) {
        this.mTemp = temp;
    }

    public String getTemperature() {
        return this.mTemp;
    }

    public void setHumidity(String humidity) {
        this.mHumidity = humidity;
    }

    public String getHumidity() {
        return this.mHumidity;
    }

    public void setPressure(String pressure) {
        this.mPressure = pressure;
    }

    public String getPressure() {
        return this.mPressure;
    }

    public void setShortDescription(String desc) {
        mShortDescription = desc;
    }

    public String getShortDescription() {
        return mShortDescription;
    }

    public void setDescription(String desc) {
        mDescription = desc;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setLastUpdatedDate(String date) {
        mLastUpdatedDate = date;
    }

    public String getLastUpdatedDate() {
        return mLastUpdatedDate;
    }

    public void setWindspeed(String speed) {
        mWindSpeed = speed;
    }

    public String getWindspeed() {
        return mWindSpeed;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    private void setTempMax(String max) {
        mTempMax = max;
    }

    public String getTempMax() {
        return mTempMax;
    }

    private void setTempMin(String min) {
        mTempMin = min;
    }

    public String getTempMin() {
        return mTempMin;
    }
}

