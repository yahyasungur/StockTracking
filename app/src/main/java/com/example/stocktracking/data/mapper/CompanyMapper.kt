package com.example.stocktracking.data.mapper

import com.example.stocktracking.data.local.CompanyInfoEntity
import com.example.stocktracking.data.local.CompanyListingEntity
import com.example.stocktracking.data.remote.dto.CompanyInfoDto
import com.example.stocktracking.data.remote.dto.CompanyNewsDto
import com.example.stocktracking.domain.model.CompanyInfo
import com.example.stocktracking.domain.model.CompanyListing
import com.example.stocktracking.domain.model.CompanyNews

fun CompanyListingEntity.toCompanyListing(): CompanyListing {
    return CompanyListing(
        name = name,
        symbol = symbol,
        exchange = exchange,
        assetType = assetType,
        ipoDate = ipoDate,
        status = status
    )
}

fun CompanyListing.toCompanyListingEntity(): CompanyListingEntity {
    return CompanyListingEntity(
        name = name,
        symbol = symbol,
        exchange = exchange,
        assetType = assetType,
        ipoDate = ipoDate,
        status = status
    )
}

fun CompanyInfoDto.toCompanyInfo(): CompanyInfo {
    return CompanyInfo(
        symbol = symbol ?: "",
        description = description ?: "",
        name = name ?: "",
        country = country ?: "",
        industry = industry ?: "",
        sector = sector ?: "",
        analystTargetPrice = analystTargetPrice ?: "0.0",
        weekHigh52 = weekHigh52 ?: "0.0",
        weekLow52 = weekLow52 ?: "0.0",
        dayMovingAverage50 = dayMovingAverage50 ?: "0.0",
        dayMovingAverage200 = dayMovingAverage200 ?: "0.0"
    )
}

fun CompanyInfo.toCompanyInfoEntity(): CompanyInfoEntity {
    return CompanyInfoEntity(
        symbol = symbol,
        description = description,
        name = name,
        country = country,
        industry = industry,
        sector = sector,
        analystTargetPrice = analystTargetPrice,
        weekHigh52 = weekHigh52,
        weekLow52 = weekLow52,
        dayMovingAverage50 = dayMovingAverage50,
        dayMovingAverage200 = dayMovingAverage200
    )
}

fun CompanyInfoEntity.toCompanyInfo(): CompanyInfo {
    return CompanyInfo(
        symbol = symbol,
        description = description,
        name = name,
        country = country,
        industry = industry,
        sector = sector,
        analystTargetPrice = analystTargetPrice,
        weekHigh52 = weekHigh52,
        weekLow52 = weekLow52,
        dayMovingAverage50 = dayMovingAverage50,
        dayMovingAverage200 = dayMovingAverage200
    )
}

fun CompanyNewsDto.toCompanyNews(): CompanyNews {
    return CompanyNews(
        title = title ?: "",
        url = url ?: "",
        // time_published = time_published ?: "",
        // summary = summary ?: "",
        banner_image = banner_image ?: "",
        sentiment = sentiment ?: ""
    )
}