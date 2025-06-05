package hr.foi.zavrsniapp.data.repository

import hr.foi.zavrsniapp.data.api.RetrofitClient
import hr.foi.zavrsniapp.data.models.NbaGame
import hr.foi.zavrsniapp.BuildConfig
import hr.foi.zavrsniapp.data.models.NbaGameResponse

class SportsRepository {
    private val apiKey: String = BuildConfig.SPORTS_API_KEY

    suspend fun getGamesById(gameId: String): NbaGameResponse {
        return RetrofitClient.sportsApiService.getGameById(gameId, apiKey)
    }
}