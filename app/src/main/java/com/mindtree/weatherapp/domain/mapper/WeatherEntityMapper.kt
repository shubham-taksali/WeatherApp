package com.mindtree.weatherapp.domain.mapper

import com.mindtree.weatherapp.data.local.entity.WeatherEntity
import com.mindtree.weatherapp.domain.model.CurrentWeather
import com.mindtree.weatherapp.domain.model.Weather

fun Weather.toEntity(): List<WeatherEntity> {
    val entities = mutableListOf<WeatherEntity>()
    
    // Current weather
    entities.add(
        WeatherEntity(
            id = "${this.latitude},${this.longitude}_current",
            timestamp = current.time,
            temperature = current.temperature,
            summary = current.summary,
            icon = current.icon,
            precipitation = current.precipitationProbability,
            humidity = current.humidity,
            windSpeed = current.windSpeed,
            latitude = this.latitude,
            longitude = this.longitude
        )
    )
    
    // Hourly forecast
    entities.addAll(
        hourlyForecast.map { hourly ->
            WeatherEntity(
                id = "${this.latitude},${this.longitude}_hourly_${hourly.time}",
                timestamp = hourly.time,
                temperature = hourly.temperature,
                summary = hourly.summary,
                icon = hourly.icon,
                precipitation = hourly.precipitationProbability,
                humidity = hourly.humidity,
                windSpeed = hourly.windSpeed,
                latitude = this.latitude,
                longitude = this.longitude
            )
        }
    )
    
    // Daily forecast
    entities.addAll(
        dailyForecast.map { daily ->
            WeatherEntity(
                id = "${this.latitude},${this.longitude}_daily_${daily.time}",
                timestamp = daily.time,
                temperature = (daily.temperatureHigh + daily.temperatureLow) / 2,
                summary = daily.summary,
                icon = daily.icon,
                precipitation = daily.precipitationProbability,
                humidity = daily.humidity,
                windSpeed = daily.windSpeed,
                latitude = this.latitude,
                longitude = this.longitude
            )
        }
    )
    
    return entities
}

fun List<WeatherEntity>.toWeather(): Weather? {
    if (this.isEmpty()) return null
    
    val current = this.find { it.id.endsWith("_current") }
    val hourly = this.filter { it.id.contains("_hourly_") }
    val daily = this.filter { it.id.contains("_daily_") }
    
    if (current == null) return null
    
    return Weather(
        id = current.id.substringBefore("_current"),
        latitude = current.latitude,
        longitude = current.longitude,
        timezone = "", // Timezone is not stored locally
        current = CurrentWeather(
            time = current.timestamp,
            summary = current.summary,
            icon = current.icon,
            temperature = current.temperature,
            feelsLike = current.temperature, // Simplified for local storage
            humidity = current.humidity,
            windSpeed = current.windSpeed,
            precipitationProbability = current.precipitation,
            uvIndex = 0 // Simplified for local storage
        ),
        hourlyForecast = hourly.map {
            HourlyWeather(
                time = it.timestamp,
                summary = it.summary,
                icon = it.icon,
                temperature = it.temperature,
                feelsLike = it.temperature, // Simplified for local storage
                precipitationProbability = it.precipitation,
                humidity = it.humidity,
                windSpeed = it.windSpeed
            )
        }.sortedBy { it.time },
        dailyForecast = daily.map {
            DailyWeather(
                time = it.timestamp,
                summary = it.summary,
                icon = it.icon,
                sunriseTime = 0, // Not stored locally
                sunsetTime = 0, // Not stored locally
                temperatureHigh = it.temperature + 5, // Approximated
                temperatureLow = it.temperature - 5, // Approximated
                precipitationProbability = it.precipitation,
                humidity = it.humidity,
                windSpeed = it.windSpeed,
                uvIndex = 0 // Not stored locally
            )
        }.sortedBy { it.time }
    )
}
