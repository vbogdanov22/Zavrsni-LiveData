package hr.foi.zavrsniapp.data.api

import hr.foi.zavrsniapp.data.models.NbaGame
import hr.foi.zavrsniapp.data.models.NbaGameResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SportsAPIService {
    @GET("{gameId}")
    suspend fun getGameById(
        @Path("gameId") gameId: String,
        @Query("key") apiKey: String
    ): NbaGameResponse
}