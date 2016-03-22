package com.permoveo.weworkweather.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.permoveo.weworkweather.R;
import com.permoveo.weworkweather.models.WeatherForecast;
import com.permoveo.weworkweather.models.WeatherItem;
import com.permoveo.weworkweather.util.DateTimeUtil;
import com.permoveo.weworkweather.util.TemperatureUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by byfieldj on 3/20/16.
 */
public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {


    private ArrayList<WeatherItem> mItems = new ArrayList<WeatherItem>();
    private Context context;


    public ForecastAdapter(Context context, WeatherForecast forecast) {

        this.context = context;
        mItems = forecast.getWeatherItems();


    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public void onBindViewHolder(ForecastAdapter.ViewHolder holder, int position) {

        Log.d("ForecastAdapter", "Date String is " + mItems.get(position).getLastUpdatedDate());

        String date = mItems.get(position).getLastUpdatedDate();
        long dv = Long.valueOf(date) * 1000;// its need to be in milisecond


        DateTimeUtil dateTimeUtil = new DateTimeUtil();
        String dateString = dateTimeUtil.convertDate(dv);

        Date df = new java.util.Date(dv);
        String vv = new SimpleDateFormat("MM dd, yyyy").format(df);


        holder.day.setText(vv);
        holder.desc.setText(mItems.get(position).getShortDescription());

        chooseWeatherIcon(mItems.get(position).getDescription(), holder.icon);


        holder.highDisplay.setText("" + TemperatureUtil.fromKelvinToFahrenheit(mItems.get(position).getTempMax()) + (char) 0x00B0);
        holder.lowDisplay.setText("" + TemperatureUtil.fromKelvinToFahrenheit(mItems.get(position).getTempMin()) + (char) 0x00B0);

    }

    private String convertDate(String date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
            Date d = format.parse(date);
            SimpleDateFormat serverFormat = new SimpleDateFormat("EEEE");
            return serverFormat.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private void chooseWeatherIcon(String description, ImageView icon) {
        if (description.contains("sunny")) {
            icon.setBackgroundResource(R.drawable.ic_sunny);
        } else if (description.contains("rain")) {
            icon.setBackgroundResource(R.drawable.ic_rain);
        } else if (description.contains("snow")) {
            icon.setBackgroundResource(R.drawable.ic_snow);
        } else if (description.contains("cloud")) {
            icon.setBackgroundResource(R.drawable.ic_partly_cloudy);
        } else if (description.contains("clear")) {
            icon.setBackgroundResource(R.drawable.ic_clear);
        }
    }

    @Override
    public ForecastAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forecast, parent, false);
        return new ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_forecast_day)
        TextView day;

        @Bind(R.id.tv_forecast_desc)
        TextView desc;

        @Bind(R.id.iv_weather_icon)
        ImageView icon;

        @Bind(R.id.tv_high_disp)
        TextView highDisplay;

        @Bind(R.id.tv_low_disp)
        TextView lowDisplay;

        @Bind(R.id.tv_high)
        TextView high;

        @Bind(R.id.tv_low)
        TextView low;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
