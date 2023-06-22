package com.example.stocktracking.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CompanyListingEntity::class, CompanyInfoEntity::class, WatchlistEntity::class],
    version = 3
)
abstract class StockDatabase : RoomDatabase() {
    abstract val dao: StockDao
}