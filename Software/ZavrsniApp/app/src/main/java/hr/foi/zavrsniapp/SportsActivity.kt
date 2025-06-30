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

class SportsActivity : ComponentActivity() {
    private val viewModel: SportsViewModel by viewModels()

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sports_activity)

        val gameIdView = findViewById<TextView>(R.id.tvGameId)
        val timeRemainingLabel = findViewById<TextView>(R.id.tvTimeRemainingLabel)
        val timeRemainingView = findViewById<TextView>(R.id.tvTimeRemaining)
        val quarterLabel = findViewById<TextView>(R.id.tvQuarterLabel)
        val quarterView = findViewById<TextView>(R.id.tvQuarter)
        val seasonView = findViewById<TextView>(R.id.tvSeason)
        val statusView = findViewById<TextView>(R.id.tvStatus)
        val teamsView = findViewById<TextView>(R.id.tvTeams)
        val scoresView = findViewById<TextView>(R.id.tvScores)
        val awayLogoView = findViewById<ImageView>(R.id.ivAwayTeamLogo)
        val homeLogoView = findViewById<ImageView>(R.id.ivHomeTeamLogo)
        val lastPlayView = findViewById<TextView>(R.id.tvLastPlay)

        val gameId = "20913" //20884
        viewModel.startRefreshing(gameId)

        viewModel.formattedTimeRemaining.observe(this) { timeRemainingView.text = it }
        viewModel.quarterString.observe(this) { quarterView.text = it }

        viewModel.game.observe(this) { game ->
            if (game != null) {
                gameIdView.text = game.GameID.toString()
                seasonView.text = game.Season.toString()
                statusView.text = game.Status
                lastPlayView.text = game.LastPlay
            }
        }

        viewModel.teamsString.observe(this) { teamsView.text = it }
        viewModel.scoresString.observe(this) { scoresView.text = it }

        viewModel.awayLogoUrl.observe(this) { Glide.with(this).load(it).into(awayLogoView) }
        viewModel.homeLogoUrl.observe(this) { Glide.with(this).load(it).into(homeLogoView) }

        viewModel.timeAndQuarterVisibilityEvent.observe(this) { event ->
            event.getContentIfNotHandled()?.let { shouldHide ->
                timeRemainingLabel.visibility = if (shouldHide) View.GONE else View.VISIBLE
                timeRemainingView.visibility = if (shouldHide) View.GONE else View.VISIBLE
                quarterLabel.visibility = if (shouldHide) View.GONE else View.VISIBLE
                quarterView.visibility = if (shouldHide) View.GONE else View.VISIBLE
            }
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