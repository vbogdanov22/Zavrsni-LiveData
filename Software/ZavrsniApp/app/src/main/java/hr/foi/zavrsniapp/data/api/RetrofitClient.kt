package hr.foi.zavrsniapp.data.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL_WEATHER = "https://api.weatherapi.com/v1/"

    val client = OkHttpClient.Builder()
        .build()

    val apiService: WeatherAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_WEATHER)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(WeatherAPIService::class.java)
    }
}