package hr.foi.zavrsniapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import hr.foi.zavrsniapp.databinding.WeatherActivityBinding
import hr.foi.zavrsniapp.ui.weather.WeatherViewModel

class WeatherActivity : ComponentActivity() {
    private lateinit var binding: WeatherActivityBinding
    private val weatherViewModel : WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = WeatherActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListeners()

        weatherViewModel.rawWeatherData.observe(this) {
            updateUI()
        }

        weatherViewModel.isCelsiusUnit.observe(this) {
            updateUI()
        }
    }

    private fun updateUI(){
        val weatherResponse = weatherViewModel.rawWeatherData.value
        if (weatherResponse != null) {
            binding.tvLocation.text = "${weatherResponse.location.name}, ${weatherResponse.location.country}"
            val isCelsius = weatherViewModel.isCelsiusUnit.value == true
            val temp = if (isCelsius) weatherResponse.current.temp_c else weatherResponse.current.temp_f
            val unit = if (isCelsius) "°C" else "°F"
            binding.tvTemperature.text = "$temp$unit"
            binding.tvCondition.text = weatherResponse.current.condition.text
        }
    }

    private fun setupListeners(){
        binding.btnFetchWeather.setOnClickListener{
            val location = binding.etCityInput.text.toString()
            weatherViewModel.setLocationInput(location)
            binding.tvLocation.text = weatherViewModel.LocationInput.value
        }

        binding.btnToggleUnit.setOnClickListener {
            weatherViewModel.toggleTemperatureUnit()
            val unit = if (weatherViewModel.isCelsiusUnit.value == true) {
                "Celsius"
            } else {
                "Fahrenheit"
            }
            Toast.makeText(this, "Temperature unit changed to $unit", Toast.LENGTH_SHORT).show()
        }
    }
}