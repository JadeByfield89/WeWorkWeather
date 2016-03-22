package com.permoveo.weworkweather.activities;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.permoveo.weworkweather.R;
import com.permoveo.weworkweather.adapters.WeatherPagerAdapter;
import com.permoveo.weworkweather.constants.AppConstants;
import com.permoveo.weworkweather.fragments.CurrentWeatherFragment;
import com.permoveo.weworkweather.fragments.SearchFragment;
import com.permoveo.weworkweather.fragments.WeatherForecastFragment;
import com.permoveo.weworkweather.gps.GPSTracker;
import com.permoveo.weworkweather.interfaces.OnAutoCompleteRequestListener;
import com.permoveo.weworkweather.interfaces.OnLocationSelectedListener;
import com.permoveo.weworkweather.interfaces.OnRequestCompletedListener;
import com.permoveo.weworkweather.models.AutoCompleteResult;
import com.permoveo.weworkweather.models.WeatherForecast;
import com.permoveo.weworkweather.models.WeatherItem;
import com.permoveo.weworkweather.network.GoogleAutoCompleteRequest;
import com.permoveo.weworkweather.network.WeatherRequest;
import com.viewpagerindicator.CirclePageIndicator;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, OnLocationSelectedListener {


    private static final String TAG = "MainActivity";

    private WeatherItem mWeatherItem;
    private WeatherForecast mWeatherForecast;

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawer;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.pager)
    ViewPager mPager;

    @Bind(R.id.pager_indicator)
    CirclePageIndicator mPagerIndicator;

    @Bind(R.id.main_activity_container)
    RelativeLayout mLayout;

    private WeatherPagerAdapter mPagerAdapter;


    ActionBarDrawerToggle mToggle;

    private BroadcastReceiver mLocationReceiver;

    private CurrentWeatherFragment mCurrentWeatherFragment;
    private WeatherForecastFragment mWeatherForecastFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setElevation(0f);

        mCurrentWeatherFragment = new CurrentWeatherFragment();
        mWeatherForecastFragment = new WeatherForecastFragment();


        mToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.open_drawer, R.string.close_drawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

        };


        mDrawer.setDrawerListener(mToggle);

        mToggle.syncState();

        mPagerAdapter = new WeatherPagerAdapter(getSupportFragmentManager());

        getWeatherData(null);


    }


    // Gets the weather conditions for the specified location
    // If no location is provided, get the weather for the current GPS location
    private void getWeatherData(String myLocation) {


        Location location = GPSTracker.get.getLocation();
        String lat = "" + location.getLatitude();
        String lon = "" + location.getLongitude();

        Log.d(TAG, "Lat -> " + lat);
        Log.d(TAG, "Lon -> " + lon);

        WeatherRequest request = new WeatherRequest();
        request.setListener(new OnRequestCompletedListener() {
            @Override
            public void onRequestComplete(WeatherItem item) {
                mWeatherItem = item;

                mPagerAdapter.setWeatherItem(item);


            }

            @Override
            public void onRequestCompleteWithForecast(WeatherForecast forecast) {
                mWeatherForecast = forecast;


                mPagerAdapter.setWeatherForecast(forecast);

                mPager.setAdapter(mPagerAdapter);
                mPagerIndicator.setViewPager(mPager, 0);
                mPager.setOffscreenPageLimit(2);

                mPagerAdapter.updateCurrentWeather();
                mPagerAdapter.updateForecast();


            }

            @Override
            public void onError() {

            }
        });

        if (myLocation == null || myLocation.isEmpty()) {
            request.requestCurrentWeather(lon, lat, null);
        } else {

            request.requestCurrentWeather(null, null, myLocation);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mToggle.syncState();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search:

                showSearchFragment();
                break;
        }

        if (mToggle != null && mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSearchFragment() {
        SearchFragment fragment = new SearchFragment();
        fragment.show(getSupportFragmentManager(), "search_fragment");
    }

    @Override
    public void OnLocationSelected(String name) {
        Log.d("MainActivity", "Getting Weather for -> " + name);
        getWeatherData(name);
    }
}
