package hr.foi.zavrsniapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
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

        weatherViewModel.weatherData.observe(this) {
            updateUI()
        }

        weatherViewModel.isMetricUnit.observe(this) {
            updateUI()
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_weather -> {
                    true
                }
                R.id.nav_sports -> {
                    startActivity(Intent(this, SportsActivity::class.java))
                    true
                }
                else -> false
            }
        }
        bottomNav.selectedItemId = R.id.nav_weather
    }

    private fun updateUI(){
        val weatherResponse = weatherViewModel.weatherData.value
        if (weatherResponse != null) {
            binding.tvLocation.text = weatherResponse.location
            binding.tvTemperature.text = weatherResponse.temperature
            binding.tvFeelsLike.text = weatherResponse.feelsLike
            binding.tvCondition.text = weatherResponse.condition
            binding.tvLastUpdated.text = weatherResponse.lastUpdated
            binding.tvWindSpeed.text = weatherResponse.windSpeed

            @Suppress("DEPRECATION")
            Glide.with(this)
                .load(weatherResponse.iconUrl)
                .into(binding.ivConditionIcon)
        }
    }

    private fun setupListeners(){
        binding.btnFetchWeather.setOnClickListener{
            val location = binding.etLocationInput.text.toString()
            weatherViewModel.setLocationInput(location)
        }

        binding.btnToggleUnit.setOnClickListener {
            weatherViewModel.toggleUnitType()
            val unit = if (weatherViewModel.isMetricUnit.value == true) {
                "metric"
            } else {
                "imperial"
            }
            Toast.makeText(this, "Units: $unit", Toast.LENGTH_SHORT).show()
        }
    }
}