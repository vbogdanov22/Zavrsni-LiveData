package hr.foi.zavrsniapp.ui.sports

import androidx.lifecycle.*
import hr.foi.zavrsniapp.data.models.NbaGame
import hr.foi.zavrsniapp.data.repository.SportsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SportsViewModel(
    private val repository: SportsRepository = SportsRepository()
) : ViewModel() {

    private val _game = MutableLiveData<NbaGame?>()
    val game: LiveData<NbaGame?> = _game

    private var refreshJob: Job? = null

    fun startRefreshing(gameId: String) {
        if (refreshJob?.isActive == true) return
        refreshJob = viewModelScope.launch {
            while (true) {
                try {
                    val games = repository.getGamesById(gameId)
                    _game.value = games.firstOrNull()
                } catch (_: Exception) {
                    _game.value = null
                }
                kotlinx.coroutines.delay(30_000)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        refreshJob?.cancel()
    }
}