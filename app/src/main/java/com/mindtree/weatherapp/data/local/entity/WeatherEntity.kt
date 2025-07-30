package com.mindtree.weatherapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey
    val id: String,
    val timestamp: Long,
    val temperature: Double,
    val summary: String,
    val icon: String,
    val precipitation: Double,
    val humidity: Double,
    val windSpeed: Double,
    val latitude: Double,
    val longitude: Double
)
