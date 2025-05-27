package hr.foi.zavrsniapp.ui.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.ViewModel

import hr.foi.zavrsniapp.data.models.WeatherResponse
import hr.foi.zavrsniapp.data.repository.WeatherRepository

class WeatherViewModel(private val repository: WeatherRepository = WeatherRepository()) : ViewModel() {
    private val _locationInput: MutableLiveData<String> = MutableLiveData("Zagreb")
    val LocationInput: LiveData<String> = _locationInput

    private val _isCelsiusUnit: MutableLiveData<Boolean> = MutableLiveData(true)
    val isCelsiusUnit: LiveData<Boolean> = _isCelsiusUnit

    val rawWeatherData: LiveData<WeatherResponse?> = _locationInput.switchMap { location ->
        androidx.lifecycle.liveData {
            emit(null)
            try {
                val response = repository.getWeather(location)
                emit(response)
            } catch (e: Exception) {
                println("Error fetching weather data: ${e.message}")
                emit(null)
            }
        }
    }
}