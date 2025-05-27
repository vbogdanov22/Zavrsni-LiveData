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
    }

    private fun setupListeners(){
//        binding.btnFetchWeather.setOnClickListener{
//            val location = binding.etCityInput.text.toString()
//            weatherViewModel.LocationInput.value = location
//        }

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