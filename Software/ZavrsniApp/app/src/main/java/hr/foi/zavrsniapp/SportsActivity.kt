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

        val gameId = "20884"
        viewModel.startRefreshing(gameId)

        viewModel.timeRemaining.observe(this) { totalSeconds ->
            if (totalSeconds != null) {
                val min = totalSeconds / 60
                val sec = totalSeconds % 60
                timeRemainingView.text = String.format("%02dm %02ds", min, sec)
            } else {
                timeRemainingView.text = "N/A"
            }
        }

        viewModel.game.observe(this) { game ->
            if (game != null) {
                gameIdView.text = "${game.GameID}"
                //timeRemainingView.text = "${game.TimeRemainingMinutes ?: "-"}m ${game.TimeRemainingSeconds ?: "-"}s"
                seasonView.text = "${game.Season}"
                statusView.text = "${game.Status}"
                teamsView.text = "${game.AwayTeam} — ${game.HomeTeam}"
                scoresView.text = "${game.AwayTeamScore ?: "-"} - ${game.HomeTeamScore ?: "-"}"
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