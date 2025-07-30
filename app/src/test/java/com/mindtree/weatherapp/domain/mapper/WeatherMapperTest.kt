package com.mindtree.weatherapp.domain.mapper

import com.mindtree.weatherapp.data.remote.dto.*
import org.junit.Test
import kotlin.test.assertEquals

class WeatherMapperTest {

    @Test
    fun `test WeatherResponse to Weather mapping`() {
        // Given
        val response = WeatherResponse(
            latitude = 40.7128,
            longitude = -74.0060,
            timezone = "America/New_York",
            currently = Currently(
                time = 1627646400,
                summary = "Clear",
                icon = "clear-day",
                nearestStormDistance = null,
                nearestStormBearing = null,
                precipIntensity = 0.0,
                precipProbability = 0.0,
                temperature = 75.0,
                apparentTemperature = 76.0,
                dewPoint = 65.0,
                humidity = 0.65,
                pressure = 1013.0,
                windSpeed = 5.0,
                windGust = 8.0,
                windBearing = 180,
                cloudCover = 0.1,
                uvIndex = 5,
                visibility = 10.0,
                ozone = 300.0
            ),
            hourly = Hourly(
                summary = "Clear throughout the day",
                icon = "clear-day",
                data = listOf(
                    HourlyData(
                        time = 1627646400,
                        summary = "Clear",
                        icon = "clear-day",
                        precipIntensity = 0.0,
                        precipProbability = 0.0,
                        temperature = 75.0,
                        apparentTemperature = 76.0,
                        dewPoint = 65.0,
                        humidity = 0.65,
                        pressure = 1013.0,
                        windSpeed = 5.0,
                        windGust = 8.0,
                        windBearing = 180,
                        cloudCover = 0.1,
                        uvIndex = 5,
                        visibility = 10.0,
                        ozone = 300.0
                    )
                )
            ),
            daily = Daily(
                summary = "Clear throughout the week",
                icon = "clear-day",
                data = listOf(
                    DailyData(
                        time = 1627646400,
                        summary = "Clear",
                        icon = "clear-day",
                        sunriseTime = 1627624800,
                        sunsetTime = 1627677600,
                        moonPhase = 0.5,
                        precipIntensity = 0.0,
                        precipIntensityMax = 0.0,
                        precipIntensityMaxTime = 0,
                        precipProbability = 0.0,
                        precipType = null,
                        temperatureHigh = 80.0,
                        temperatureHighTime = 1627660800,
                        temperatureLow = 65.0,
                        temperatureLowTime = 1627704000,
                        apparentTemperatureHigh = 82.0,
                        apparentTemperatureHighTime = 1627660800,
                        apparentTemperatureLow = 64.0,
                        apparentTemperatureLowTime = 1627704000,
                        dewPoint = 65.0,
                        humidity = 0.65,
                        pressure = 1013.0,
                        windSpeed = 5.0,
                        windGust = 8.0,
                        windGustTime = 1627660800,
                        windBearing = 180,
                        cloudCover = 0.1,
                        uvIndex = 5,
                        uvIndexTime = 1627660800,
                        visibility = 10.0,
                        ozone = 300.0,
                        temperatureMin = 65.0,
                        temperatureMinTime = 1627624800,
                        temperatureMax = 80.0,
                        temperatureMaxTime = 1627660800,
                        apparentTemperatureMin = 64.0,
                        apparentTemperatureMinTime = 1627624800,
                        apparentTemperatureMax = 82.0,
                        apparentTemperatureMaxTime = 1627660800
                    )
                )
            ),
            offset = -4
        )

        // When
        val weather = response.toWeather()

        // Then
        assertEquals(40.7128, weather.latitude)
        assertEquals(-74.0060, weather.longitude)
        assertEquals("America/New_York", weather.timezone)
        
        // Check current weather
        assertEquals("Clear", weather.current.summary)
        assertEquals(75.0, weather.current.temperature)
        assertEquals(76.0, weather.current.feelsLike)
        assertEquals(0.65, weather.current.humidity)
        assertEquals(5.0, weather.current.windSpeed)
        
        // Check hourly forecast
        assertEquals(1, weather.hourlyForecast.size)
        with(weather.hourlyForecast[0]) {
            assertEquals(1627646400, time)
            assertEquals("Clear", summary)
            assertEquals(75.0, temperature)
            assertEquals(76.0, feelsLike)
        }
        
        // Check daily forecast
        assertEquals(1, weather.dailyForecast.size)
        with(weather.dailyForecast[0]) {
            assertEquals(1627646400, time)
            assertEquals("Clear", summary)
            assertEquals(80.0, temperatureHigh)
            assertEquals(65.0, temperatureLow)
        }
    }
}
