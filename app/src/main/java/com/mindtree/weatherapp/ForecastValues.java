package com.mindtree.weatherapp;

/**
 * Created by Shubham on 03/03/2017.
 */

public class ForecastValues {

    private long time;
    private String forecastSummary;
    private double forecastTemperature;
    private double forecastWindspeed;
    private double forecastHumidity;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getForecastSummary() {
        return forecastSummary;
    }

    public void setForecastSummary(String forecastSummary) {
        this.forecastSummary = forecastSummary;
    }

    public double getForecastTemperature() {
        return forecastTemperature;
    }

    public void setForecastTemperature(double forecastTemperature) {
        this.forecastTemperature = forecastTemperature;
    }

    public double getForecastWindspeed() {
        return forecastWindspeed;
    }

    public void setForecastWindspeed(double forecastWindspeed) {
        this.forecastWindspeed = forecastWindspeed;
    }

    public double getForecastHumidity() {
        return forecastHumidity;
    }

    public void setForecastHumidity(double forecastHumidity) {
        this.forecastHumidity = forecastHumidity;
    }
}
