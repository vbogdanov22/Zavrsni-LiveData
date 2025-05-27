package hr.foi.zavrsniapp.ui.weather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.ViewModel

import hr.foi.zavrsniapp.data.models.WeatherResponse
import hr.foi.zavrsniapp.data.repository.WeatherRepository

class WeatherViewModel(private val repository: WeatherRepository = WeatherRepository()) : ViewModel() {

    // LOCATION INPUT
    private val _locationInput: MutableLiveData<String> = MutableLiveData("Zagreb")
    val LocationInput: LiveData<String> = _locationInput

    fun setLocationInput(location: String) {
        _locationInput.value = location
    }

    val rawWeatherData: LiveData<WeatherResponse?> = _locationInput.switchMap { location ->
        androidx.lifecycle.liveData {
            emit(null)
            try {
                val response = repository.getWeather(location)
                emit(response)
            } catch (e: Exception) {
                Log.e("WVM", "Error fetching weather data: ${e.message}")
                emit(null)
            }
        }
    }

    // TEMP UNIT TOGGLE
    private val _isCelsiusUnit: MutableLiveData<Boolean> = MutableLiveData(true)
    val isCelsiusUnit: LiveData<Boolean> = _isCelsiusUnit

    fun toggleTemperatureUnit() {
        _isCelsiusUnit.value = !(_isCelsiusUnit.value ?: true)
    }
}