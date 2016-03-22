package com.permoveo.weworkweather.network;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.permoveo.weworkweather.application.WeWorkWeatherApplication;
import com.permoveo.weworkweather.constants.AppConstants;
import com.permoveo.weworkweather.fragments.CurrentWeatherFragment;
import com.permoveo.weworkweather.interfaces.OnRequestCompletedListener;
import com.permoveo.weworkweather.models.WeatherForecast;
import com.permoveo.weworkweather.models.WeatherItem;

import org.json.JSONObject;

/**
 * Created by byfieldj on 3/19/16.
 */
public class WeatherRequest {


    public WeatherItem mCurrentWeatherItem;
    private OnRequestCompletedListener mListener;
    private String mLongitude, mLatitude;
    private static WeatherForecast mForecast;

    public String OWM_WEATHER_SEARCH_ENDPOINT = "http://api.openweathermap.org/data/2.5/weather?APPID=" + AppConstants.OWM_API_KEY;
    public String OWM_WEATHER_FORECAST_ENDPOINT = "http://api.openweathermap.org/data/2.5/forecast?&APPID=" + AppConstants.OWM_API_KEY;

    public void setListener(OnRequestCompletedListener listener) {
        mListener = listener;
    }

    public WeatherItem requestCurrentWeather(final String longitude, final String latitude, final String cityName) {


        if (latitude != null && longitude != null) {
            OWM_WEATHER_SEARCH_ENDPOINT += "&lon=" + longitude + "&lat=" + latitude;
            Log.d("WeatherRequest", "Making request to -> " + OWM_WEATHER_SEARCH_ENDPOINT);
        } else {
            OWM_WEATHER_SEARCH_ENDPOINT += "&q=" + cityName;
            OWM_WEATHER_SEARCH_ENDPOINT = OWM_WEATHER_SEARCH_ENDPOINT.replace(" ", "");
            Log.d("WeatherRequest", "Making request to -> " + OWM_WEATHER_SEARCH_ENDPOINT);


        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, OWM_WEATHER_SEARCH_ENDPOINT, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.d("WeatherRequest", "JSON RESPONSE -> " + jsonObject.toString());
                mCurrentWeatherItem = new WeatherItem(jsonObject);

                if (latitude != null && longitude != null) {
                    requestForecast(latitude, longitude, null);
                } else {
                    requestForecast(null, null, cityName);
                }

                mListener.onRequestComplete(mCurrentWeatherItem);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Log.e("WeatherRequest", "Error executing request! -> " + volleyError.toString());

            }
        });

        WeWorkWeatherApplication.getInstance().addToRequestQueue(request);


        return mCurrentWeatherItem;
    }

    public void requestForecast(String latitude, String longitude, String cityName) {

        if (latitude != null && longitude != null) {
            OWM_WEATHER_FORECAST_ENDPOINT += "&lon=" + longitude + "&lat=" + latitude;
            Log.d("WeatherRequest", "Making request to -> " + OWM_WEATHER_FORECAST_ENDPOINT);
        } else {
            OWM_WEATHER_FORECAST_ENDPOINT += "&q=" + cityName;
            OWM_WEATHER_FORECAST_ENDPOINT = OWM_WEATHER_FORECAST_ENDPOINT.replace(" ", "");
            Log.d("WeatherRequest", "Making request to -> " + OWM_WEATHER_FORECAST_ENDPOINT);
        }


        JsonObjectRequest forecastRequest = new JsonObjectRequest(Request.Method.GET, OWM_WEATHER_FORECAST_ENDPOINT, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.d("WeatherRequest", "Weather forecast -> " + jsonObject.toString());
                mForecast = new WeatherForecast(jsonObject);
                mListener.onRequestCompleteWithForecast(mForecast);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("WeatherRequest", "Error executing request!");
                mListener.onError();

            }
        });

        WeWorkWeatherApplication.getInstance().addToRequestQueue(forecastRequest);


    }


}
