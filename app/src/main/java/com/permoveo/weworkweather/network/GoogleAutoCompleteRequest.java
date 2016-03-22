package com.permoveo.weworkweather.network;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.permoveo.weworkweather.application.WeWorkWeatherApplication;
import com.permoveo.weworkweather.constants.AppConstants;
import com.permoveo.weworkweather.interfaces.OnAutoCompleteRequestListener;
import com.permoveo.weworkweather.interfaces.OnRequestCompletedListener;
import com.permoveo.weworkweather.models.AutoCompleteResult;

import org.json.JSONObject;

/**
 * Created by byfieldj on 3/21/16.
 */
public class GoogleAutoCompleteRequest {

    private static final String TAG = "GoogleAutoComplete";
    private OnAutoCompleteRequestListener mListener;
    public String PLACES_AUTOCOMPLETE_URL = "https://maps.googleapis.com/maps/api/place/autocomplete/json?&key=" + AppConstants.GOOGLE_API_KEY + "&types=(cities)&input=%s";


    public void setListener(OnAutoCompleteRequestListener listener) {
        mListener = listener;
    }

    public void getCityNames(String query) {

        PLACES_AUTOCOMPLETE_URL = String.format(PLACES_AUTOCOMPLETE_URL, query);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, PLACES_AUTOCOMPLETE_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d(TAG, "JSON Response -> " + jsonObject);

                AutoCompleteResult result = new AutoCompleteResult(jsonObject);
                mListener.onAutoCompleteRequestComplete(result);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        Log.d(TAG, "Making request to -> " + PLACES_AUTOCOMPLETE_URL);

        WeWorkWeatherApplication.getInstance().addToRequestQueue(request, TAG);


    }
}
