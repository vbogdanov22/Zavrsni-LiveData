package hr.foi.zavrsniapp.data.repository

import hr.foi.zavrsniapp.data.api.RetrofitClient
import hr.foi.zavrsniapp.data.models.WeatherResponse
import hr.foi.zavrsniapp.BuildConfig

class WeatherRepository {
    private val API_KEY: String = BuildConfig.WEATHER_API_KEY

    suspend fun getWeather(location: String): WeatherResponse {
        return RetrofitClient.apiService.getCurrentWeather(API_KEY, location)
    }
}