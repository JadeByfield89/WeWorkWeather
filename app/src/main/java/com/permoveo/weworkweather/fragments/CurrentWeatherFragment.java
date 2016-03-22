package com.permoveo.weworkweather.fragments;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.permoveo.weworkweather.R;
import com.permoveo.weworkweather.gps.GPSTracker;
import com.permoveo.weworkweather.interfaces.OnAutoCompleteRequestListener;
import com.permoveo.weworkweather.interfaces.OnRequestCompletedListener;
import com.permoveo.weworkweather.models.AutoCompleteResult;
import com.permoveo.weworkweather.models.WeatherForecast;
import com.permoveo.weworkweather.models.WeatherItem;
import com.permoveo.weworkweather.network.GoogleAutoCompleteRequest;
import com.permoveo.weworkweather.network.WeatherRequest;
import com.permoveo.weworkweather.util.DateTimeUtil;
import com.permoveo.weworkweather.util.TemperatureUtil;
import com.permoveo.weworkweather.views.BounceProgressBar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by byfieldj on 3/18/16.
 */
public class CurrentWeatherFragment extends Fragment {

    private static final String EXTRA_ITEM = "extra_item";
    private static final String FRAGMENT_POSITION = "position";
    private WeatherItem mWeatherItem;

    @Bind(R.id.rl_current_weather)
    RelativeLayout mLayout;

    @Bind(R.id.tv_city_name)
    TextView mCityName;

    @Bind(R.id.tv_last_updated)
    TextView mLastUpdatedAt;

    @Bind(R.id.tv_last_updated_today)
    TextView mToday;

    @Bind(R.id.iv_weather_icon)
    ImageView mWeatherIcon;

    @Bind(R.id.tv_description)
    TextView mDescription;

    @Bind(R.id.tv_weather_comment)
    TextView mComment;

    @Bind(R.id.tv_weather_unit)
    TextView mDegrees;


    @Bind(R.id.iv_rain)
    ImageView mHumidity;

    @Bind(R.id.tv_rain_percentage)
    TextView mHumidityPercentage;

    @Bind(R.id.iv_wind)
    ImageView mWindIcon;

    @Bind(R.id.tv_wind_speed)
    TextView mWindspeed;

    @Bind(R.id.iv_pressure)
    ImageView mPressureIcon;

    @Bind(R.id.tv_pressure)
    TextView mPressure;

    @Bind(R.id.rl_main_weather_container)
    RelativeLayout mMainContainer;

    @Bind(R.id.bounce_progress)
    BounceProgressBar mBounceProgressbar;


    public WeatherItem mCurrentWeatherItem;
    public static WeatherForecast mWeatherForecast;
    private static final String TAG = "CurrentWeatherFragment";

    public CurrentWeatherFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCurrentWeatherItem = (WeatherItem) getArguments().getSerializable(EXTRA_ITEM);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_current_weather, container, false);
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

        mMainContainer.setVisibility(View.INVISIBLE);


        colorAnimation.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimation.setRepeatCount(Animation.INFINITE);


        colorAnimation.setTarget(mLayout);
        colorAnimation.start();


        updateUI(mCurrentWeatherItem);


        return v;
    }

    private void chooseWeatherIcon(String description) {
        if (description.contains("sunny")) {
            mWeatherIcon.setBackgroundResource(R.drawable.ic_sunny);
        } else if (description.contains("rain")) {
            mWeatherIcon.setBackgroundResource(R.drawable.ic_rain);
        } else if (description.contains("snow")) {
            mWeatherIcon.setBackgroundResource(R.drawable.ic_snow);
        } else if (description.contains("cloud")) {
            mWeatherIcon.setBackgroundResource(R.drawable.ic_partly_cloudy);
        } else if (description.contains("clear")) {
            mWeatherIcon.setBackgroundResource(R.drawable.ic_clear);
        }
    }


    public void updateUI(WeatherItem item) {

        if (item != null) {
            mBounceProgressbar.setVisibility(View.INVISIBLE);
            mMainContainer.setVisibility(View.VISIBLE);

            mCityName.setText(item.getName());
            DateTimeUtil util = new DateTimeUtil();
            long dateLong = Long.valueOf(item.getLastUpdatedDate());
            mLastUpdatedAt.setText("Last updated at " + util.convertDate(dateLong));

            mComment.setText(item.getShortDescription());
            mDescription.setText(item.getDescription());

            chooseWeatherIcon(item.getDescription());

            mDegrees.setText("" + TemperatureUtil.fromKelvinToFahrenheit(item.getTemperature()) + (char) 0x00B0);

            mHumidityPercentage.setText(item.getHumidity() + "%");
            mWindspeed.setText(item.getWindspeed() + "mPh");
            mPressure.setText(item.getPressure() + "inHg");
        } else {
            Log.d("CurrentWeatherFragment", "Error displaying weather! WeatherItem is null");
        }


    }

    public static CurrentWeatherFragment newInstance(int position, WeatherItem item) {


        Bundle args = new Bundle();
        args.putInt(FRAGMENT_POSITION, position);
        args.putSerializable(EXTRA_ITEM, item);
        CurrentWeatherFragment fragment = new CurrentWeatherFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }
}
