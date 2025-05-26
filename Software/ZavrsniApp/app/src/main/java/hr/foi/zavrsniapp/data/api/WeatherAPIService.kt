package hr.foi.zavrsniapp.data.api

import hr.foi.zavrsniapp.data.models.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPIService {
    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("key") apiKey: String,
        @Query("q") location: String,
    ): WeatherResponse
}