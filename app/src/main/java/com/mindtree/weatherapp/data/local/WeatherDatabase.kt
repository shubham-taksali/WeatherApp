package com.mindtree.weatherapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mindtree.weatherapp.data.local.dao.WeatherDao
import com.mindtree.weatherapp.data.local.entity.WeatherEntity

@Database(
    entities = [WeatherEntity::class],
    version = 1
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao

    companion object {
        const val DATABASE_NAME = "weather_db"
    }
}
