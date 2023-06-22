package com.example.stocktracking.data.remote.dto

import com.squareup.moshi.Json

data class CompanyNewsDto(
    val title: String?,
    val url: String?,
    //val time_published : String?,
    //val summary : String?,
    val banner_image: String?,
    @field:Json(name = "overall_sentiment_label") val sentiment: String?,
)
