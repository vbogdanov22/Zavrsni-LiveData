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
        val seasonView = findViewById<TextView>(R.id.tvSeason)
        val statusView = findViewById<TextView>(R.id.tvStatus)
        val teamsView = findViewById<TextView>(R.id.tvTeams)
        val scoresView = findViewById<TextView>(R.id.tvScores)

        val date = "2023-FEB-01"
        viewModel.startRefreshing(date)

        viewModel.game.observe(this) { game ->
            if (game != null) {
                gameIdView.text = "Game ID: ${game.GameID}"
                timeRemainingView.text = "Time Remaining: ${game.TimeRemainingMinutes ?: "-"}m ${game.TimeRemainingSeconds ?: "-"}s"
                seasonView.text = "Season: ${game.Season}"
                statusView.text = "Status: ${game.Status}"
                teamsView.text = "Teams: ${game.AwayTeam} @ ${game.HomeTeam}"
                scoresView.text = "Scores: ${game.AwayTeamScore ?: "-"} - ${game.HomeTeamScore ?: "-"}"
            } else {
                gameIdView.text = "Game ID: -"
                timeRemainingView.text = "Time Remaining: -"
                seasonView.text = "Season: -"
                statusView.text = "Status: -"
                teamsView.text = "Teams: -"
                scoresView.text = "Scores: -"
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