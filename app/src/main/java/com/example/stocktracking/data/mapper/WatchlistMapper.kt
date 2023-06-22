package com.example.stocktracking.data.mapper

import com.example.stocktracking.data.local.WatchlistEntity
import com.example.stocktracking.domain.model.Watchlist

fun WatchlistEntity.toWatchlist(): Watchlist {
    return Watchlist(
        name = name,
        symbol = symbol,
        startPrice = startPrice,
        endPrice = endPrice
    )
}

fun Watchlist.toWatchlistEntity(): WatchlistEntity {
    return WatchlistEntity(
        name = name,
        symbol = symbol,
        startPrice = startPrice,
        endPrice = endPrice
    )
}