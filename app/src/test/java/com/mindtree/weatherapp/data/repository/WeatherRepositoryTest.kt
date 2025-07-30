package com.mindtree.weatherapp.data.repository

import app.cash.turbine.test
import com.mindtree.weatherapp.data.local.WeatherDatabase
import com.mindtree.weatherapp.data.local.dao.WeatherDao
import com.mindtree.weatherapp.data.remote.WeatherApi
import com.mindtree.weatherapp.data.remote.dto.Currently
import com.mindtree.weatherapp.data.remote.dto.WeatherResponse
import com.mindtree.weatherapp.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class WeatherRepositoryTest {
    private lateinit var repository: WeatherRepository
    private lateinit var api: WeatherApi
    private lateinit var db: WeatherDatabase
    private lateinit var dao: WeatherDao

    @Before
    fun setup() {
        api = mockk()
        db = mockk()
        dao = mockk()
        coEvery { db.weatherDao() } returns dao
        repository = WeatherRepository(api, db)
    }

    @Test
    fun `getWeatherData emits loading then success on successful fetch`() = runTest {
        // Given
        val weatherResponse = WeatherResponse(
            currently = Currently(
                temperature = 20.0,
                summary = "Clear",
                icon = "clear-day",
                precipProbability = 0.0,
                humidity = 0.5,
                windSpeed = 5.0
            )
        )

        coEvery { 
            api.getWeatherForecast(any(), any(), any()) 
        } returns weatherResponse

        coEvery { 
            dao.getLatestWeather() 
        } returns null

        coEvery { 
            dao.insertWeather(any()) 
        } returns Unit

        // When & Then
        repository.getWeatherData(0.0, 0.0, "test-key").test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Loading)
            val success = awaitItem() as Resource.Success
            assertEquals(null, success.data)
            awaitComplete()
        }
    }

    @Test
    fun `getWeatherData emits error on network failure`() = runTest {
        // Given
        coEvery { 
            api.getWeatherForecast(any(), any(), any()) 
        } throws IOException()

        coEvery { 
            dao.getLatestWeather() 
        } returns null

        // When & Then
        repository.getWeatherData(0.0, 0.0, "test-key").test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Loading)
            val error = awaitItem() as Resource.Error
            assertEquals(
                "Couldn't reach server. Check your internet connection.",
                error.message
            )
            awaitComplete()
        }
    }
}
