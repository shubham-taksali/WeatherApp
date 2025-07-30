package com.mindtree.weatherapp.domain.mapper

import com.mindtree.weatherapp.data.remote.dto.*
import com.mindtree.weatherapp.domain.model.*

fun WeatherResponse.toWeather(): Weather {
    return Weather(
        id = "${this.latitude},${this.longitude}",
        latitude = this.latitude,
        longitude = this.longitude,
        timezone = this.timezone,
        current = this.currently.toCurrentWeather(),
        hourlyForecast = this.hourly.data.map { it.toHourlyWeather() },
        dailyForecast = this.daily.data.map { it.toDailyWeather() }
    )
}

fun Currently.toCurrentWeather(): CurrentWeather {
    return CurrentWeather(
        time = this.time,
        summary = this.summary,
        icon = this.icon,
        temperature = this.temperature,
        feelsLike = this.apparentTemperature,
        humidity = this.humidity,
        windSpeed = this.windSpeed,
        precipitationProbability = this.precipProbability,
        uvIndex = this.uvIndex
    )
}

fun HourlyData.toHourlyWeather(): HourlyWeather {
    return HourlyWeather(
        time = this.time,
        summary = this.summary,
        icon = this.icon,
        temperature = this.temperature,
        feelsLike = this.apparentTemperature,
        precipitationProbability = this.precipProbability,
        humidity = this.humidity,
        windSpeed = this.windSpeed
    )
}

fun DailyData.toDailyWeather(): DailyWeather {
    return DailyWeather(
        time = this.time,
        summary = this.summary,
        icon = this.icon,
        sunriseTime = this.sunriseTime,
        sunsetTime = this.sunsetTime,
        temperatureHigh = this.temperatureHigh,
        temperatureLow = this.temperatureLow,
        precipitationProbability = this.precipProbability,
        humidity = this.humidity,
        windSpeed = this.windSpeed,
        uvIndex = this.uvIndex
    )
}
