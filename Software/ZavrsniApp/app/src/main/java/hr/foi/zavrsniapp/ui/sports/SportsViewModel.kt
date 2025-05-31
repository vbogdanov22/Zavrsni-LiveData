package hr.foi.zavrsniapp.ui.sports

import androidx.lifecycle.*
import hr.foi.zavrsniapp.data.repository.SportsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SportsViewModel(
    private val repository: SportsRepository = SportsRepository()
) : ViewModel() {

    private val _gameInfo = MutableLiveData<Pair<Int, Pair<Int?, Int?>>>()
    val gameInfo: LiveData<Pair<Int, Pair<Int?, Int?>>> = _gameInfo

    private var refreshJob: Job? = null

    fun startRefreshing(date: String) {
        if (refreshJob?.isActive == true) return
        refreshJob = viewModelScope.launch {
            while (true) {
                try {
                    val games = repository.getGamesByDate(date)
                    val firstGame = games.firstOrNull()
                    if (firstGame != null) {
                        _gameInfo.value = Pair(
                            firstGame.GameID,
                            Pair(firstGame.TimeRemainingMinutes, firstGame.TimeRemainingSeconds)
                        )
                    }
                } catch (_: Exception) { }
                delay(30_000)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        refreshJob?.cancel()
    }
}