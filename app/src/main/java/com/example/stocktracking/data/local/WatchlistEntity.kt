package com.example.stocktracking.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class WatchlistEntity(
    val symbol: String,
    val name: String,
    val startPrice: String,
    val endPrice: String,
    @PrimaryKey val id: Int? = null
)