package com.example.stocktracking.presentation.watchlist

sealed class WatchlistEvent {
    object Refresh : WatchlistEvent()
}