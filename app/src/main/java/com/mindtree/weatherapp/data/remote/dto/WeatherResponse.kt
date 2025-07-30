package com.mindtree.weatherapp.data.remote.dto

data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val currently: Currently,
    val hourly: Hourly,
    val daily: Daily,
    val offset: Int
)

data class Currently(
    val time: Long,
    val summary: String,
    val icon: String,
    val nearestStormDistance: Int?,
    val nearestStormBearing: Int?,
    val precipIntensity: Double,
    val precipProbability: Double,
    val temperature: Double,
    val apparentTemperature: Double,
    val dewPoint: Double,
    val humidity: Double,
    val pressure: Double,
    val windSpeed: Double,
    val windGust: Double,
    val windBearing: Int,
    val cloudCover: Double,
    val uvIndex: Int,
    val visibility: Double,
    val ozone: Double
)

data class Hourly(
    val summary: String,
    val icon: String,
    val data: List<HourlyData>
)

data class Daily(
    val summary: String,
    val icon: String,
    val data: List<DailyData>
)

data class HourlyData(
    val time: Long,
    val summary: String,
    val icon: String,
    val precipIntensity: Double,
    val precipProbability: Double,
    val temperature: Double,
    val apparentTemperature: Double,
    val dewPoint: Double,
    val humidity: Double,
    val pressure: Double,
    val windSpeed: Double,
    val windGust: Double,
    val windBearing: Int,
    val cloudCover: Double,
    val uvIndex: Int,
    val visibility: Double,
    val ozone: Double
)

data class DailyData(
    val time: Long,
    val summary: String,
    val icon: String,
    val sunriseTime: Long,
    val sunsetTime: Long,
    val moonPhase: Double,
    val precipIntensity: Double,
    val precipIntensityMax: Double,
    val precipIntensityMaxTime: Long,
    val precipProbability: Double,
    val precipType: String?,
    val temperatureHigh: Double,
    val temperatureHighTime: Long,
    val temperatureLow: Double,
    val temperatureLowTime: Long,
    val apparentTemperatureHigh: Double,
    val apparentTemperatureHighTime: Long,
    val apparentTemperatureLow: Double,
    val apparentTemperatureLowTime: Long,
    val dewPoint: Double,
    val humidity: Double,
    val pressure: Double,
    val windSpeed: Double,
    val windGust: Double,
    val windGustTime: Long,
    val windBearing: Int,
    val cloudCover: Double,
    val uvIndex: Int,
    val uvIndexTime: Long,
    val visibility: Double,
    val ozone: Double,
    val temperatureMin: Double,
    val temperatureMinTime: Long,
    val temperatureMax: Double,
    val temperatureMaxTime: Long,
    val apparentTemperatureMin: Double,
    val apparentTemperatureMinTime: Long,
    val apparentTemperatureMax: Double,
    val apparentTemperatureMaxTime: Long
)
