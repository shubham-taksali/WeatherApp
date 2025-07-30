package com.mindtree.weatherapp.di

import android.app.Application
import androidx.room.Room
import com.mindtree.weatherapp.data.local.WeatherDatabase
import com.mindtree.weatherapp.data.remote.WeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideWeatherDatabase(app: Application): WeatherDatabase {
        return Room.databaseBuilder(
            app,
            WeatherDatabase::class.java,
            WeatherDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideWeatherApi(): WeatherApi {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://api.forecast.io/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }
}
