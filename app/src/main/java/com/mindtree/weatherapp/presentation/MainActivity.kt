package com.mindtree.weatherapp.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mindtree.weatherapp.BuildConfig
import com.mindtree.weatherapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        
        setupObservers()
        checkLocationPermission()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when {
                        state.isLoading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        state.error != null -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this@MainActivity, state.error, Toast.LENGTH_LONG).show()
                        }
                        state.weather != null -> {
                            binding.progressBar.visibility = View.GONE
                            updateUI(state)
                        }
                    }
                }
            }
        }
    }

    private fun updateUI(state: WeatherState) {
        state.weather?.let { weather ->
            binding.apply {
                temperatureText.text = "${weather.temperature}°"
                summaryText.text = weather.summary
                humidityText.text = "${(weather.humidity * 100).toInt()}%"
                windSpeedText.text = "${weather.windSpeed} mph"
                
                // Update weather icon based on the icon string
                when (weather.icon) {
                    "clear-day" -> sunView.visibility = View.VISIBLE
                    "clear-night" -> moonView.visibility = View.VISIBLE
                    "rain" -> rainView.visibility = View.VISIBLE
                    "snow" -> snowView.visibility = View.VISIBLE
                    "wind" -> windView.visibility = View.VISIBLE
                    "fog" -> fogView.visibility = View.VISIBLE
                    "cloudy" -> cloudView.visibility = View.VISIBLE
                    "partly-cloudy-day" -> cloudSunView.visibility = View.VISIBLE
                    "partly-cloudy-night" -> cloudMoonView.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        getLocation()
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    viewModel.getWeather(
                        latitude = it.latitude,
                        longitude = it.longitude,
                        apiKey = BuildConfig.WEATHER_API_KEY
                    )
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            getLocation()
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}
