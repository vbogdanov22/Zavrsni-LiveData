package hr.foi.zavrsniapp.ui.sports

import android.util.Log
import androidx.lifecycle.*
import hr.foi.zavrsniapp.SportsUIState
import hr.foi.zavrsniapp.data.models.NbaGame
import hr.foi.zavrsniapp.data.repository.SportsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SportsViewModel(private val repository: SportsRepository = SportsRepository()) : ViewModel() {
    private val _game = MutableLiveData<NbaGame?>()
    val game: LiveData<NbaGame?> = _game

    val uiState: LiveData<SportsUIState> = game.map { game ->
        val timeAndQuarterHidden = when (game?.Status) {
            "Scheduled", "Final" -> true
            "InProgress" -> game.Quarter.isNullOrEmpty() || game.Quarter == "Half"
            else -> false
        }
        val lastPlayHidden = game?.Status == "Final"
        SportsUIState(timeAndQuarterHidden, lastPlayHidden)
    }

    private val _timeRemaining = MutableLiveData<Int?>()
    val formattedTimeRemaining: LiveData<String> = _timeRemaining.map { totalSeconds ->
        if (totalSeconds != null) {
            val min = totalSeconds / 60
            val sec = totalSeconds % 60
            String.format("%02dm %02ds", min, sec)
        } else {
            "N/A"
        }
    }

    val quarterString: LiveData<String> = game.map { game ->
        when (game?.Quarter?.toIntOrNull()) {
            1 -> "1st quarter"
            2 -> "2nd quarter"
            3 -> "3rd quarter"
            4 -> "4th quarter"
            else -> ""
        }
    }

    val teamsString: LiveData<String> = game.map { game ->
        if (game != null) "${game.AwayTeam} — ${game.HomeTeam}" else ""
    }

    val scoresString: LiveData<String> = game.map { game ->
        if (game != null) "${game.AwayTeamScore ?: "-"} - ${game.HomeTeamScore ?: "-"}" else ""
    }

    private fun getLogoUrl(teamName: String?): String =
        if (!teamName.isNullOrEmpty()) "https://a.espncdn.com/i/teamlogos/nba/500/${teamName}.png" else ""

    val awayLogoUrl: LiveData<String> = game.map { game ->
        getLogoUrl(game?.AwayTeam)
    }

    val homeLogoUrl: LiveData<String> = game.map { game ->
        getLogoUrl(game?.HomeTeam)
    }

    private var refreshJob: Job? = null
    private var timerJob: Job? = null

    fun startRefreshing(gameId: String) {
        if (refreshJob?.isActive == true) return

        refreshJob = viewModelScope.launch {
            while (true) {
                try {
                    val game = repository.getGameById(gameId)
                    _game.value = game.Game

                    val minutes = game.Game.TimeRemainingMinutes ?: 0
                    val seconds = game.Game.TimeRemainingSeconds ?: 0
                    _timeRemaining.value = minutes * 60 + seconds

                    Log.d("SVM", "Game updated: $game")
                } catch (_: Exception) {
                    _game.value = null
                }
                delay(65_000)
            }
        }

        if(timerJob?.isActive != true) {
            timerJob = viewModelScope.launch {
                while(true){
                    _timeRemaining.value?.let {
                        if (it > 0) {
                             _timeRemaining.postValue(it - 1)
                        }
                    }
                    delay(1000)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        refreshJob?.cancel()
        timerJob?.cancel()
    }
}