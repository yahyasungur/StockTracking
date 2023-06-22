package com.example.stocktracking.domain.model

data class CompanyInfo(
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
)
