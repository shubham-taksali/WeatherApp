package com.mindtree.weatherapp.data.repository

import com.mindtree.weatherapp.data.local.WeatherDatabase
import com.mindtree.weatherapp.data.local.entity.WeatherEntity
import com.mindtree.weatherapp.data.remote.WeatherApi
import com.mindtree.weatherapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val api: WeatherApi,
    private val db: WeatherDatabase
) {
    fun getWeatherData(latitude: Double, longitude: Double, apiKey: String) = flow {
        emit(Resource.Loading())

        val weatherDao = db.weatherDao()
        emit(Resource.Loading(data = weatherDao.getLatestWeather()))

        try {
            val remoteWeather = api.getWeatherForecast(apiKey, latitude, longitude)
            weatherDao.insertWeather(
                WeatherEntity(
                    id = "${latitude},${longitude}",
                    timestamp = System.currentTimeMillis(),
                    temperature = remoteWeather.currently.temperature,
                    summary = remoteWeather.currently.summary,
                    icon = remoteWeather.currently.icon,
                    precipitation = remoteWeather.currently.precipProbability,
                    humidity = remoteWeather.currently.humidity,
                    windSpeed = remoteWeather.currently.windSpeed,
                    latitude = latitude,
                    longitude = longitude
                )
            )
        } catch (e: HttpException) {
            emit(Resource.Error(
                message = "An error occurred",
                data = weatherDao.getLatestWeather()
            ))
        } catch (e: IOException) {
            emit(Resource.Error(
                message = "Couldn't reach server. Check your internet connection.",
                data = weatherDao.getLatestWeather()
            ))
        }

        emit(Resource.Success(data = weatherDao.getLatestWeather()))
    }
}
