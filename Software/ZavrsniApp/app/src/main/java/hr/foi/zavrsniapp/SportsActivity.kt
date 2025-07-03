package hr.foi.zavrsniapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import hr.foi.zavrsniapp.ui.sports.SportsViewModel
import com.bumptech.glide.Glide
import hr.foi.zavrsniapp.databinding.SportsActivityBinding

class SportsActivity : ComponentActivity() {
    private val viewModel: SportsViewModel by viewModels()
    private lateinit var binding: SportsActivityBinding

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SportsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gameId = "20913"
        viewModel.startRefreshing(gameId)

        viewModel.formattedTimeRemaining.observe(this) { binding.tvTimeRemaining.text = it }
        viewModel.quarterString.observe(this) { binding.tvQuarter.text = it }

        viewModel.game.observe(this) { game ->
            if (game != null) {
                binding.tvGameId.text = game.GameID.toString()
                binding.tvSeason.text = game.Season.toString()
                binding.tvStatus.text = game.Status
                binding.tvLastPlay.text = game.LastPlay
            }
        }

        viewModel.teamsString.observe(this) { binding.tvTeams.text = it }
        viewModel.scoresString.observe(this) { binding.tvScores.text = it }

        viewModel.awayLogoUrl.observe(this) { Glide.with(this).load(it).into(binding.ivAwayTeamLogo) }
        viewModel.homeLogoUrl.observe(this) { Glide.with(this).load(it).into(binding.ivHomeTeamLogo) }

        viewModel.uiState.observe(this) { uiState ->
            val timeAndQuarter = if (uiState.timeAndQuarterHidden) android.view.View.GONE else android.view.View.VISIBLE
            binding.tvTimeRemainingLabel.visibility = timeAndQuarter
            binding.tvTimeRemaining.visibility = timeAndQuarter
            binding.tvQuarterLabel.visibility = timeAndQuarter
            binding.tvQuarter.visibility = timeAndQuarter

            val lastPlay = if (uiState.lastPlayHidden) android.view.View.GONE else android.view.View.VISIBLE
            binding.tvLastPlayLabel.visibility = lastPlay
            binding.tvLastPlay.visibility = lastPlay
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_weather -> {
                    startActivity(Intent(this, WeatherActivity::class.java))
                    true
                }
                R.id.nav_sports -> true
                else -> false
            }
        }
        bottomNav.selectedItemId = R.id.nav_sports
    }
}