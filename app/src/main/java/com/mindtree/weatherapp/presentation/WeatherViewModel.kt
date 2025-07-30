package com.mindtree.weatherapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindtree.weatherapp.data.local.entity.WeatherEntity
import com.mindtree.weatherapp.data.repository.WeatherRepository
import com.mindtree.weatherapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _state = MutableStateFlow(WeatherState())
    val state = _state.asStateFlow()

    fun getWeather(latitude: Double, longitude: Double, apiKey: String) {
        viewModelScope.launch {
            repository
                .getWeatherData(latitude, longitude, apiKey)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                weather = result.data,
                                isLoading = false,
                                error = null
                            )
                        }
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                weather = result.data,
                                isLoading = false,
                                error = result.message
                            )
                        }
                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                weather = result.data,
                                isLoading = true,
                                error = null
                            )
                        }
                    }
                }
        }
    }
}

data class WeatherState(
    val weather: WeatherEntity? = null,
    val forecast: List<WeatherEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
