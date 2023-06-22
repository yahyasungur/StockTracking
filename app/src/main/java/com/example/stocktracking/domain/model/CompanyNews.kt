package com.example.stocktracking.domain.model

data class CompanyNews(
    val title: String,
    val url: String,
    // val time_published : String,
    // val summary : String,
    val banner_image: String,
    val sentiment: String,
)