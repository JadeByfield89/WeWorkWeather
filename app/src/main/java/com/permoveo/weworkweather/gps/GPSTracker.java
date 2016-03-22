package com.permoveo.weworkweather.gps;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


import com.permoveo.weworkweather.constants.AppConstants;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public enum GPSTracker implements LocationListener {
    get;

    private static final String TAG = GPSTracker.class.getSimpleName();
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    protected LocationManager locationManager;
    private Context context;
    private Location location;
    private Handler mHandler;
    private String mLocationName;

    private boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    public boolean isBetterAccuracy(Location location) {
        return location.getAccuracy() < 70.0f;
    }

    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    public void init(Context context) {
        this.context = context;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, 15, GPSTracker.this);
        if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 15000, 15, GPSTracker.this);
        try {
            switch (getProvider()) {
                case NONE:
                    break;
                case GPS:
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location == null)
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    break;
                case NETWORK:
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location == null)
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    break;
            }
        } catch (Exception e) {
        }
        Log.i(TAG, String.valueOf(location));
    }

    public void startOnMainLooper() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 25000, 25, GPSTracker.this);
                if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 25000, 25, GPSTracker.this);
                try {
                    switch (getProvider()) {
                        case NONE:
                            break;
                        case GPS:
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location == null)
                                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            break;
                        case NETWORK:
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location == null)
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            break;
                    }
                } catch (Exception e) {
                }
                Log.i(TAG, String.valueOf(location));
            }
        });
    }

    public void stopOnMainLooper() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (locationManager != null) {
                    locationManager.removeUpdates(GPSTracker.this);
                }
            }
        });
    }

    public void start() {
        Looper.prepare();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Looper.myLooper().quit();
            }
        };
        startOnMainLooper();
        mHandler.sendEmptyMessage(0);
        Looper.loop();
    }

    public void stop() {
        Looper.prepare();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Looper.myLooper().quit();
            }
        };
        stopOnMainLooper();
        mHandler.sendEmptyMessage(0);
        Looper.loop();
    }

    public Location getLocation() {

        //getLocationAddress();
        return location;
    }

    public boolean isGpsEnabled() {
        return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public Provider getProvider() {
        if (locationManager == null) return Provider.NONE;

        boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gpsEnabled)
            return Provider.GPS;
        if (networkEnabled)
            return Provider.NETWORK;
        return Provider.NONE;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (isBetterLocation(location, this.location)) {
            this.location = location;

            stopOnMainLooper();
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(AppConstants.ACTION_GET_LOCATION));
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(final String s) {
        Provider provider = Provider.NONE;
        try {
            provider = Provider.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
        }


    }

    @Override
    public void onProviderDisabled(final String s) {
        Provider provider = Provider.NONE;
        try {
            provider = Provider.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
        }


    }

    public enum Provider {
        NONE,
        GPS,
        NETWORK
    }

    private class GetLocationAddressTask extends AsyncTask<String, Void, String> {
        private Location location;

        public GetLocationAddressTask(Location location) {

            this.location = location;

        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);
                Log.e("Addresses", "-->" + addresses);
                result = addresses.get(0).toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!s.isEmpty()) {
                setLocationName(s);
                Log.d("GPSTracker", "Location name is -> " + s);
            }

        }
    }

    public void getLocationAddress() {
        GetLocationAddressTask task = new GetLocationAddressTask(getLocation());
        task.execute();
    }

    public void setLocationName(String name) {
        mLocationName = name;
    }

    public String getLocationName() {
        return mLocationName;
    }
}