package com.example.stocktracking.data.local

import androidx.room.*

@Dao
interface StockDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompanyListings(
        companyListingEntities: List<CompanyListingEntity>
    )

    @Query("DELETE FROM companylistingentity")
    suspend fun clearCompanyListings()

    // search for company name or exact symbol
    @Query(
        """
        SELECT *
        FROM companylistingentity
        WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' OR
            UPPER(:query) == symbol
    """
    )
    suspend fun searchCompanyListing(query: String): List<CompanyListingEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompanyInfo(
        companyInfoEntity: CompanyInfoEntity
    )

    @Query("DELETE FROM companyinfoentity")
    suspend fun clearCompanyInfo()

    // search for company name or exact symbol
    @Query(
        """
        SELECT *
        FROM companyinfoentity
        WHERE symbol == :symbol
    """
    )
    suspend fun searchCompanyInfo(symbol: String): List<CompanyInfoEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWatchlistItem(
        watchlistEntity: WatchlistEntity
    )

    @Query("DELETE FROM watchlistentity WHERE symbol=:symbol")
    suspend fun deleteWatchlistItem(
        symbol: String
    )

    @Query("SELECT * FROM watchlistentity ORDER BY symbol")
    suspend fun getWatchlistItems(): List<WatchlistEntity>

    @Update
    suspend fun updateWatchlistItem(
        watchlistEntity: WatchlistEntity
    )
}