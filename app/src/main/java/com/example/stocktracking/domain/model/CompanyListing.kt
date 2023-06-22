package com.example.stocktracking.domain.model

data class CompanyListing(
    val name: String,
    val symbol: String,
    val exchange: String,
    val assetType: String,
    val ipoDate: String,
    val status: String
)
