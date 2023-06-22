package com.example.stocktracking.presentation.watchlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stocktracking.domain.repository.StockRepository
import com.example.stocktracking.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val repository: StockRepository
) : ViewModel() {

    var state by mutableStateOf(WatchlistState())

    init {
        getWatchlist()
    }

    fun onEvent(event: WatchlistEvent) {
        when (event) {
            is WatchlistEvent.Refresh -> {
                getWatchlist(fetchFromRemote = true)
            }
        }
    }

    fun getWatchlist(
        fetchFromRemote: Boolean = false // Can be used in the future if the unlimited api acquired
    ) {
        viewModelScope.launch {
            repository.getWatchlist() // give a parameter like fetchFromRemote
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { items ->
                                state = state.copy(watchlists = items)
                            }
                        }
                        is Resource.Error -> Unit
                        is Resource.Loading -> {
                            state = state.copy(isLoading = result.isLoading)
                        }
                    }
                }
        }
    }

}