package hr.foi.zavrsniapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import hr.foi.zavrsniapp.ui.sports.SportsViewModel

class SportsActivity : ComponentActivity() {
    private val viewModel: SportsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sports_activity)

        val gameIdView = findViewById<TextView>(R.id.tvGameId)
        val timeRemainingView = findViewById<TextView>(R.id.tvTimeRemaining)

        val date = "2023-FEB-01"
        viewModel.startRefreshing(date)

        viewModel.gameInfo.observe(this) { info ->
            gameIdView.text = "Game ID: ${info.first}"
            val (minutes, seconds) = info.second
            timeRemainingView.text = "Time Remaining: ${minutes ?: "-"}m ${seconds ?: "-"}s"
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_weather -> {
                    startActivity(Intent(this, WeatherActivity::class.java))
                    true
                }
                R.id.nav_sports -> {
                    true
                }
                else -> false
            }
        }
        bottomNav.selectedItemId = R.id.nav_sports
    }
}