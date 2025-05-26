package hr.foi.zavrsniapp.data.repository

import hr.foi.zavrsniapp.data.api.RetrofitClient
import hr.foi.zavrsniapp.data.models.WeatherResponse

class WeatherRepository {
    private val API_KEY = "bla_bla_bla"

    suspend fun getWeather(location: String): WeatherResponse {
        return RetrofitClient.apiService.getCurrentWeather(API_KEY, location)
    }
}