package com.example.stocktracking.domain.repository

import com.example.stocktracking.domain.model.*
import com.example.stocktracking.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {

    suspend fun getCompanyListing(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>>

    suspend fun getIntradayInfo(
        symbol: String
    ): Resource<List<IntradayInfo>>

    suspend fun getCompanyInfo(
        symbol: String
    ): Flow<Resource<CompanyInfo>>

    suspend fun getWatchlist(): Flow<Resource<List<Watchlist>>>

    suspend fun addWatchlist(
        watchlist: Watchlist
    )

    suspend fun deleteWatchList(
        symbol: String
    )

    suspend fun isInWatchlist(
        symbol: String
    ): Boolean

/*    suspend fun updateWatchlist(
        watchlist: Watchlist
    )*/

    suspend fun getCompanyNews(
        symbol: String
    ): Resource<List<CompanyNews>>
}