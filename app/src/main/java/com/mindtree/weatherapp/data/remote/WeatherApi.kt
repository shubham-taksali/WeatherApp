package com.mindtree.weatherapp.data.remote

import com.mindtree.weatherapp.data.remote.dto.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherApi {
    @GET("forecast/{apiKey}/{latitude},{longitude}")
    suspend fun getWeatherForecast(
        @Path("apiKey") apiKey: String,
        @Path("latitude") latitude: Double,
        @Path("longitude") longitude: Double
    ): WeatherResponse
}
