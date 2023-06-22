package com.example.stocktracking.di

import com.example.stocktracking.data.csv.CSVParser
import com.example.stocktracking.data.csv.CompanyListingParser
import com.example.stocktracking.data.csv.IntradayInfoParser
import com.example.stocktracking.data.repository.StockRepositoryImpl
import com.example.stocktracking.domain.model.CompanyListing
import com.example.stocktracking.domain.model.IntradayInfo
import com.example.stocktracking.domain.repository.StockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCompanyListingsParser(
        companyListingsParser: CompanyListingParser
    ): CSVParser<CompanyListing>

    @Binds
    @Singleton
    abstract fun bindIntradayInfoParser(
        intradayInfoParser: IntradayInfoParser
    ): CSVParser<IntradayInfo>

    @Binds
    @Singleton
    abstract fun bindStockRepository(
        stockRepositoryImpl: StockRepositoryImpl
    ): StockRepository
}