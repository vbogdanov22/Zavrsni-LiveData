package hr.foi.zavrsniapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.bumptech.glide.Glide
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
            val unit = if (isCelsius) "°C" else "°F"

            val temp = if (isCelsius) weatherResponse.current.temp_c else weatherResponse.current.temp_f
            binding.tvTemperature.text = "$temp$unit"

            val tempFeel = if(isCelsius) weatherResponse.current.feelslike_c else weatherResponse.current.feelslike_f
            binding.tvFeelsLike.text = "$tempFeel$unit"

            binding.tvCondition.text = weatherResponse.current.condition.text

            val speedUnit = if (isCelsius) "km/h" else "mph"
            val speed = if(isCelsius) weatherResponse.current.wind_kph else weatherResponse.current.wind_mph
            binding.tvWindSpeed.text = "$speed$speedUnit"

            binding.tvLastUpdated.text = weatherResponse.current.last_updated

            val iconUrl = "https:${weatherResponse.current.condition.icon}"
            Glide.with(this)
                .load(iconUrl)
                .into(binding.ivConditionIcon)
        }
    }

    private fun setupListeners(){
        binding.btnFetchWeather.setOnClickListener{
            val location = binding.etLocationInput.text.toString()
            weatherViewModel.setLocationInput(location)
            binding.tvLocation.text = weatherViewModel.LocationInput.value
        }

        binding.btnToggleUnit.setOnClickListener {
            weatherViewModel.toggleTemperatureUnit()
            val unit = if (weatherViewModel.isCelsiusUnit.value == true) {
                "C"
            } else {
                "F"
            }
            Toast.makeText(this, "Temperature unit changed to $unit", Toast.LENGTH_SHORT).show()
        }
    }
}