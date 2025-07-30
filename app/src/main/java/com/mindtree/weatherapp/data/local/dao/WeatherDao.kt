package com.mindtree.weatherapp.data.local.dao

import androidx.room.*
import com.mindtree.weatherapp.data.local.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather ORDER BY timestamp DESC LIMIT 1")
    fun getLatestWeather(): Flow<WeatherEntity?>

    @Query("SELECT * FROM weather WHERE timestamp BETWEEN :startTime AND :endTime ORDER BY timestamp ASC")
    fun getHourlyWeather(startTime: Long, endTime: Long): Flow<List<WeatherEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)

    @Query("DELETE FROM weather WHERE timestamp < :timestamp")
    suspend fun deleteOldWeather(timestamp: Long)
}
