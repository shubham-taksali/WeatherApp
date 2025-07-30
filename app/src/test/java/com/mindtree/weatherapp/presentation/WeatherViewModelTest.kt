package com.mindtree.weatherapp.presentation

import app.cash.turbine.test
import com.mindtree.weatherapp.data.local.entity.WeatherEntity
import com.mindtree.weatherapp.data.repository.WeatherRepository
import com.mindtree.weatherapp.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {
    private lateinit var viewModel: WeatherViewModel
    private lateinit var repository: WeatherRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        viewModel = WeatherViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when getWeather is called, state is updated correctly for success`() = runTest {
        // Given
        val testWeather = WeatherEntity(
            id = "test",
            timestamp = 123L,
            temperature = 20.0,
            summary = "Clear",
            icon = "clear-day",
            precipitation = 0.0,
            humidity = 0.5,
            windSpeed = 5.0,
            latitude = 0.0,
            longitude = 0.0
        )

        coEvery { 
            repository.getWeatherData(any(), any(), any()) 
        } returns flowOf(Resource.Success(testWeather))

        // When & Then
        viewModel.state.test {
            assertEquals(WeatherState(), awaitItem()) // Initial state

            viewModel.getWeather(0.0, 0.0, "test-api-key")

            val finalState = awaitItem()
            assertEquals(testWeather, finalState.weather)
            assertNull(finalState.error)
            assertTrue(!finalState.isLoading)
        }
    }

    @Test
    fun `when getWeather fails, error state is emitted`() = runTest {
        // Given
        val errorMessage = "Network error"
        coEvery { 
            repository.getWeatherData(any(), any(), any()) 
        } returns flowOf(Resource.Error(errorMessage))

        // When & Then
        viewModel.state.test {
            assertEquals(WeatherState(), awaitItem()) // Initial state

            viewModel.getWeather(0.0, 0.0, "test-api-key")

            val finalState = awaitItem()
            assertEquals(errorMessage, finalState.error)
            assertTrue(!finalState.isLoading)
        }
    }
}
