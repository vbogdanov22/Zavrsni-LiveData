package hr.foi.zavrsniapp.ui.weather

import android.util.Log

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.ViewModel
import hr.foi.zavrsniapp.ToastMethod

import hr.foi.zavrsniapp.data.models.Condition
import hr.foi.zavrsniapp.data.models.Current
import hr.foi.zavrsniapp.data.models.Location
import hr.foi.zavrsniapp.data.models.WeatherDisplayData

import hr.foi.zavrsniapp.data.models.WeatherResponse
import hr.foi.zavrsniapp.data.repository.WeatherRepository
import hr.foi.zavrsniapp.utilities.ToastEventWrapper
import hr.foi.zavrsniapp.utilities.SingleLiveEvent

class WeatherViewModel(private val repository: WeatherRepository = WeatherRepository()) : ViewModel() {
    private val _locationInput: MutableLiveData<String> = MutableLiveData(null)
    fun setLocationInput(location: String) {
        _locationInput.value = location
    }

    private val _isMetricUnit: MutableLiveData<Boolean> = MutableLiveData(true)
    val isMetricUnit: LiveData<Boolean> = _isMetricUnit

    var toastMethod = ToastMethod.BROKEN
    val unitChangedMessage = MutableLiveData<String?>()
    val unitChangedSingleLiveEvent = SingleLiveEvent<String?>()
    val unitChangedToastEventWrapper = MutableLiveData<ToastEventWrapper<String>>()

    fun toggleUnitType() {
        _isMetricUnit.value = !(_isMetricUnit.value ?: true)
        val unit = if (_isMetricUnit.value == true) "metric" else "imperial"
        when (toastMethod) {
            ToastMethod.SINGLE_LIVE_EVENT -> {
                unitChangedSingleLiveEvent.value = "Units: $unit"
            }
            ToastMethod.EVENT_WRAPPER -> {
                unitChangedToastEventWrapper.value = ToastEventWrapper("Units: $unit")
            }
            else -> { }
        }
    }

    private val _weatherData: LiveData<WeatherResponse?> = _locationInput.switchMap { location ->
        androidx.lifecycle.liveData {
            if (location.isNullOrBlank()) {
                emit(loadMockWeather())
            } else {
                try {
                    Log.d("WVM", "Attempting fetch for $location")
                    val response = repository.getWeather(location)
                    Log.d("WVM", "$response")
                    emit(response)
                } catch (e: Exception) {
                    Log.e("WVM", "Error fetching data: ${e.message}")
                    emit(null)
                }
                Log.d("WVM", "Data fetched for $location")
            }
        }
    }

    val weatherData: LiveData<WeatherDisplayData?> = MediatorLiveData<WeatherDisplayData?>().apply {
        fun update() {
            val weather = _weatherData.value
            val isMetric = _isMetricUnit.value ?: true
            value = weather?.let {
                WeatherDisplayData(
                    location = "${it.location.name}, ${it.location.country}",
                    temperature = if (isMetric) "${it.current.temp_c} °C" else "${it.current.temp_f} °F",
                    feelsLike = if (isMetric) "${it.current.feelslike_c} °C" else "${it.current.feelslike_f} °F",
                    condition = it.current.condition.text,
                    windSpeed = if (isMetric) "${it.current.wind_kph} km/h" else "${it.current.wind_mph} mph",
                    lastUpdated = it.current.last_updated.let { dt ->
                        try {
                            val inputFormat = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault())
                            val date = inputFormat.parse(dt)
                            val outputFormat = java.text.SimpleDateFormat("HH:mm (dd.MM.)", java.util.Locale.getDefault())
                            outputFormat.format(date ?: dt)
                        } catch (e: Exception) {
                            dt
                        }
                    },
                    iconUrl = "https:${it.current.condition.icon}"
                )
            }
            Log.d("WVM", "WeatherDisplayData updated: $value")
        }
        addSource(_weatherData) { update() }
        addSource(_isMetricUnit) { update() }
    }

    private fun loadMockWeather(): WeatherResponse {
        return WeatherResponse(
            location = Location(
                name = "Zagreb",
                region = "Grad Zagreb",
                country = "Croatia",
                lat = 45.8,
                lon = 16.0,
                tz_id = "Europe/Zagreb",
                localtime_epoch = 1748369571,
                localtime = "2025-05-27 20:12"
            ),
            current = Current(
                last_updated_epoch = 1748368800,
                last_updated = "2025-05-27 20:00",
                temp_c = 21.2,
                temp_f = 70.2,
                is_day = 1,
                condition = Condition(
                    text = "Partly Cloudy",
                    icon = "//cdn.weatherapi.com/weather/64x64/day/116.png",
                    code = 1003
                ),
                wind_mph = 2.2,
                wind_kph = 3.6,
                wind_degree = 9,
                wind_dir = "N",
                pressure_mb = 1017.0,
                pressure_in = 30.03,
                precip_mm = 0.0,
                precip_in = 0.0,
                humidity = 46,
                cloud = 0,
                feelslike_c = 21.2,
                feelslike_f = 70.2,
                windchill_c = 16.4,
                windchill_f = 61.6,
                heatindex_c = 16.4,
                heatindex_f = 61.6,
                dewpoint_c = 10.1,
                dewpoint_f = 50.3,
                vis_km = 10.0,
                vis_miles = 6.0,
                uv = 0.0,
                gust_mph = 4.2,
                gust_kph = 6.8
            )
        )
    }
}