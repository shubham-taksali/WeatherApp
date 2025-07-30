package com.mindtree.weatherapp.domain.model

data class Weather(
    val id: String,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val current: CurrentWeather,
    val hourlyForecast: List<HourlyWeather>,
    val dailyForecast: List<DailyWeather>
)

data class CurrentWeather(
    val time: Long,
    val summary: String,
    val icon: String,
    val temperature: Double,
    val feelsLike: Double,
    val humidity: Double,
    val windSpeed: Double,
    val precipitationProbability: Double,
    val uvIndex: Int
)

data class HourlyWeather(
    val time: Long,
    val summary: String,
    val icon: String,
    val temperature: Double,
    val feelsLike: Double,
    val precipitationProbability: Double,
    val humidity: Double,
    val windSpeed: Double
)

data class DailyWeather(
    val time: Long,
    val summary: String,
    val icon: String,
    val sunriseTime: Long,
    val sunsetTime: Long,
    val temperatureHigh: Double,
    val temperatureLow: Double,
    val precipitationProbability: Double,
    val humidity: Double,
    val windSpeed: Double,
    val uvIndex: Int
)
