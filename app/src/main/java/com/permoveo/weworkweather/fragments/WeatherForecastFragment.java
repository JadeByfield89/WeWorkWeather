package com.permoveo.weworkweather.fragments;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.permoveo.weworkweather.R;
import com.permoveo.weworkweather.adapters.ForecastAdapter;
import com.permoveo.weworkweather.gps.GPSTracker;
import com.permoveo.weworkweather.interfaces.OnRequestCompletedListener;
import com.permoveo.weworkweather.models.WeatherForecast;
import com.permoveo.weworkweather.models.WeatherItem;
import com.permoveo.weworkweather.network.WeatherRequest;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by byfieldj on 3/18/16.
 */
public class WeatherForecastFragment extends Fragment {

    private static final String FRAGMENT_POSITION = "position";
    private static final String EXTRA_FORECAST = "extra_forecast";
    private WeatherForecast mWeatherForecast;
    private static final String TAG = "WeatherForecastFragment";

    @Bind(R.id.rl_forecast)
    RelativeLayout mLayout;

    @Bind(R.id.rv_forecast_list)
    RecyclerView mRecyclerView;

    private ForecastAdapter mAdapter;


    public WeatherForecastFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mWeatherForecast = (WeatherForecast) getArguments().getSerializable(EXTRA_FORECAST);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_forecast, container, false);
        ButterKnife.bind(this, v);


        // Simple color fade animation
        int colorFrom = getResources().getColor(R.color.main_activity_background_color);
        int colorTo = getResources().getColor(R.color.main_activity_background_color_end);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(5000); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                mLayout.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });


        colorAnimation.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimation.setRepeatCount(Animation.INFINITE);


        colorAnimation.setTarget(mLayout);
        colorAnimation.start();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(false);

        updateUI(mWeatherForecast);


        return v;
    }

    public void updateUI(WeatherForecast forecast) {
        mAdapter = new ForecastAdapter(getContext(), forecast);
        mRecyclerView.setAdapter(mAdapter);
    }


    public static WeatherForecastFragment newInstance(int position, WeatherForecast forecast) {

        Bundle args = new Bundle();
        args.putInt(FRAGMENT_POSITION, position);
        args.putSerializable(EXTRA_FORECAST, forecast);
        WeatherForecastFragment fragment = new WeatherForecastFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }
}
