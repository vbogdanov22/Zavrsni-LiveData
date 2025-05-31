package hr.foi.zavrsniapp.data.api

import hr.foi.zavrsniapp.data.models.NbaGame
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SportsAPIService {
    @GET("scores/json/GamesByDate/{date}")
    suspend fun getGamesByDate(
        @Path("date") date: String,
        @Query("key") apiKey: String
    ): List<NbaGame>
}