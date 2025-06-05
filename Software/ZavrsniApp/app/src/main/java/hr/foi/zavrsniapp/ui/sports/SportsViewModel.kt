package hr.foi.zavrsniapp.ui.sports

import android.util.Log
import androidx.lifecycle.*
import hr.foi.zavrsniapp.data.models.NbaGame
import hr.foi.zavrsniapp.data.repository.SportsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SportsViewModel(
    private val repository: SportsRepository = SportsRepository()
) : ViewModel() {

    private val _game = MutableLiveData<NbaGame?>()
    val game: LiveData<NbaGame?> = _game

    private val _timeRemaining = MutableLiveData<Int?>()
    val timeRemaining: LiveData<Int?> = _timeRemaining

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
                kotlinx.coroutines.delay(30_000)
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