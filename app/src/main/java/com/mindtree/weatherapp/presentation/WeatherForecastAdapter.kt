package com.mindtree.weatherapp.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mindtree.weatherapp.data.local.entity.WeatherEntity
import com.mindtree.weatherapp.databinding.ItemForecastBinding
import java.text.SimpleDateFormat
import java.util.*

class WeatherForecastAdapter : ListAdapter<WeatherEntity, WeatherForecastAdapter.ForecastViewHolder>(
    object : DiffUtil.ItemCallback<WeatherEntity>() {
        override fun areItemsTheSame(oldItem: WeatherEntity, newItem: WeatherEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: WeatherEntity, newItem: WeatherEntity): Boolean {
            return oldItem == newItem
        }
    }
) {
    inner class ForecastViewHolder(
        private val binding: ItemForecastBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(weather: WeatherEntity) {
            binding.apply {
                val dateFormat = SimpleDateFormat("EEE, ha", Locale.getDefault())
                timeText.text = dateFormat.format(Date(weather.timestamp))
                temperatureText.text = "${weather.temperature.toInt()}°"
                descriptionText.text = weather.summary
                precipitationText.text = "${(weather.precipitation * 100).toInt()}%"
                
                // Update weather icon based on conditions
                when (weather.icon) {
                    "clear-day" -> weatherIcon.setImageResource(android.R.drawable.ic_menu_day)
                    "clear-night" -> weatherIcon.setImageResource(android.R.drawable.ic_menu_night)
                    "rain" -> weatherIcon.setImageResource(android.R.drawable.ic_menu_rain)
                    else -> weatherIcon.setImageResource(android.R.drawable.ic_menu_today)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        return ForecastViewHolder(
            ItemForecastBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
