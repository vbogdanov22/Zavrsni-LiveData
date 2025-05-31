package hr.foi.zavrsniapp.data.repository

import hr.foi.zavrsniapp.data.api.RetrofitClient
import hr.foi.zavrsniapp.data.models.NbaGame
import hr.foi.zavrsniapp.BuildConfig

class SportsRepository {
    private val apiKey: String = BuildConfig.SPORTS_API_KEY

    suspend fun getGamesByDate(date: String): List<NbaGame> {
        return RetrofitClient.sportsApiService.getGamesByDate(date, apiKey)
    }
}