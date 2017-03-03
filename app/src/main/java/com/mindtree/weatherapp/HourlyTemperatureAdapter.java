package com.mindtree.weatherapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Shubham on 03/03/2017.
 */

public class HourlyTemperatureAdapter extends RecyclerView.Adapter<HourlyTemperatureAdapter.MyViewHolder> {

    private List<ForecastValues> moviesList;

    public HourlyTemperatureAdapter(List<ForecastValues> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        ForecastValues forecastValues = moviesList.get(position);

        char degree = 0x00B0;
        String temperature = Math.round(forecastValues.getForecastTemperature()) + "";

        holder.dateTime.setText(getDate(forecastValues.getTime()));
        holder.summary.setText(forecastValues.getForecastSummary());
        holder.forecastTemperature.setText(temperature + degree + " C");
        holder.forecastWindspeed.setText(forecastValues.getForecastWindspeed() + " KM/H");
        holder.forecastHumidity.setText(Math.round(forecastValues.getForecastHumidity()*100) + "%");
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTime, summary, forecastTemperature, forecastWindspeed, forecastHumidity;

        public MyViewHolder(View view) {
            super(view);
            dateTime = (TextView) view.findViewById(R.id.date_time);
            summary = (TextView) view.findViewById(R.id.summary);
            forecastTemperature = (TextView) view.findViewById(R.id.temperature_value);
            forecastWindspeed = (TextView) view.findViewById(R.id.windspeed_value);
            forecastHumidity = (TextView) view.findViewById(R.id.humidity_value);
        }
    }

    public static String getDate(long milliSeconds)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM, hh a");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds*1000);
        return formatter.format(calendar.getTime());
    }
}
