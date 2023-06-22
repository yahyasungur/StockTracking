package com.example.stocktracking.data.remote.dto

import com.squareup.moshi.Json

data class JSONResponseDto(
    @field:Json(name = "feed") val jsonResponse: List<CompanyNewsDto>?,
)