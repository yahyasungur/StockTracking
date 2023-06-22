package com.example.stocktracking.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CompanyInfoEntity(
    val symbol: String,
    val description: String,
    val name: String,
    val country: String,
    val industry: String,
    val sector: String,
    val analystTargetPrice: String,
    val weekHigh52: String,
    val weekLow52: String,
    val dayMovingAverage50: String,
    val dayMovingAverage200: String,
    @PrimaryKey val id: Int? = null
)