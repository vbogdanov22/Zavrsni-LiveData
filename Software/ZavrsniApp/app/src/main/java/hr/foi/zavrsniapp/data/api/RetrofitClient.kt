package hr.foi.zavrsniapp.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL_WEATHER = "https://api.weatherapi.com/v1/"
    private const val BASE_URL_SPORTS = "https://replay.sportsdata.io/api/v3/nba/pbp/json/playbyplay/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    val weatherApiService: WeatherAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_WEATHER)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(WeatherAPIService::class.java)
    }

    val sportsApiService: SportsAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_SPORTS)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(SportsAPIService::class.java)
    }
}