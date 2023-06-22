package com.example.stocktracking.data.remote

import com.example.stocktracking.data.remote.dto.CompanyInfoDto
import com.example.stocktracking.data.remote.dto.JSONResponseDto
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface StockApi {

    @GET("query?function=LISTING_STATUS")
    suspend fun getListings(
        @Query("apikey") apiKey: String = API_KEY
    ): ResponseBody

    @GET("query?function=TIME_SERIES_INTRADAY&interval=60min&datatype=csv")
    suspend fun getIntradayInfo(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = API_KEY
    ): ResponseBody // CSV format

    @GET("query?function=OVERVIEW")
    suspend fun getCompanyInfo(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = API_KEY
    ): CompanyInfoDto // we do not return ResponseBody here, because it is a JSON format

    @GET("query?function=NEWS_SENTIMENT")
    suspend fun getCompanyNews(
        @Query("tickers") symbol: String,
        @Query("apikey") apiKey: String = API_KEY
    ): JSONResponseDto

    companion object {
        const val API_KEY = "N01CBJOQ7FY6GNDA"
        const val BASE_URL = "https://alphavantage.co"
    }
}