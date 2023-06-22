package com.example.stocktracking.presentation.watchlist

import com.example.stocktracking.domain.model.Watchlist

data class WatchlistState(
    val watchlists: List<Watchlist> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false
)
