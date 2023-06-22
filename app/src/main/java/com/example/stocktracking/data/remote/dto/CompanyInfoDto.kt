package com.example.stocktracking.data.remote.dto

import com.squareup.moshi.Json

data class CompanyInfoDto(
    @field:Json(name = "Symbol") val symbol: String?,
    @field:Json(name = "Description") val description: String?,
    @field:Json(name = "Name") val name: String?,
    @field:Json(name = "Country") val country: String?,
    @field:Json(name = "Industry") val industry: String?,
    @field:Json(name = "Sector") val sector: String?,
    @field:Json(name = "AnalystTargetPrice") val analystTargetPrice: String?,
    @field:Json(name = "52WeekHigh") val weekHigh52: String?,
    @field:Json(name = "52WeekLow") val weekLow52: String?,
    @field:Json(name = "50DayMovingAverage") val dayMovingAverage50: String?,
    @field:Json(name = "200DayMovingAverage") val dayMovingAverage200: String?,
)
