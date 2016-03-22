package com.permoveo.weworkweather.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by byfieldj on 3/21/16.
 */
public class AutoCompleteResult implements Serializable {

    private String mName;
    private ArrayList<String> mPredictions = new ArrayList<String>();


    public AutoCompleteResult(JSONObject object) {

        try {
            JSONArray predictionsArray = object.getJSONArray("predictions");

            for (int i = 0; i < predictionsArray.length(); i++) {
                JSONObject currentObject = predictionsArray.getJSONObject(i);

                if (currentObject.has("description")) {
                    String name = currentObject.getString("description");
                    Log.d("AutoCompleteResult", "City name -> " + name);
                    mPredictions.add(name);
                }
            }

            Log.d("AutoCompleteResult", "Predictions List Size -> " + mPredictions.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public String getName() {
        return mName;
    }

    public ArrayList<String> getPredictions() {
        return mPredictions;
    }
}
