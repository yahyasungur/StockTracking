package com.example.stocktracking.data.repository

import com.example.stocktracking.data.csv.CSVParser
import com.example.stocktracking.data.local.StockDatabase
import com.example.stocktracking.data.mapper.*
import com.example.stocktracking.data.remote.StockApi
import com.example.stocktracking.domain.model.*
import com.example.stocktracking.domain.repository.StockRepository
import com.example.stocktracking.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    private val api: StockApi,
    private val db: StockDatabase,
    private val companyListingParser: CSVParser<CompanyListing>,
    private val intradayInfoParser: CSVParser<IntradayInfo>
) : StockRepository {

    private val dao = db.dao

    override suspend fun getCompanyListing(
        fetchFromRemote: Boolean, query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {
            emit(Resource.Loading(true))
            val localListings = dao.searchCompanyListing(query)
            emit(Resource.Success(data = localListings.map { it.toCompanyListing() }))

            // if user does not type anything and listing is empty
            // then we can say that db is empty
            val isDbEmpty = localListings.isEmpty() && query.isBlank()
            val shouldLoadFromCache = !isDbEmpty && !fetchFromRemote
            if (shouldLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }

            val remoteListings = try {
                val response = api.getListings()
                // Because of single responsibility principle we ignore the following line
                // val csvReader = CSVReader(InputStreamReader(response.byteStream()))
                companyListingParser.parse(response.byteStream())
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load stocks"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't fetch stocks"))
                null
            }

            // fetch data from api and write it to the DB
            // and when using the data fetch data from DB
            remoteListings?.let { listings ->
                dao.clearCompanyListings()
                dao.insertCompanyListings(listings.map { it.toCompanyListingEntity() })
                emit(
                    Resource.Success(
                        data = dao.searchCompanyListing("").map { it.toCompanyListing() })
                )
                emit(Resource.Loading(false))
            }
        }
    }

    override suspend fun getCompanyInfo(symbol: String): Flow<Resource<CompanyInfo>> {
        return flow {
            emit(Resource.Loading(true))
            val localInfo = dao.searchCompanyInfo(symbol)
            val companyNotInDb = localInfo.isEmpty()

            // if it is in db fetch from db
            if (!companyNotInDb) {
                emit(
                    Resource.Success(
                        data = localInfo.first().toCompanyInfo()
                    )
                )
                emit(Resource.Loading(false))
                return@flow
            }


            // if company not in db fetch from remote and write it
            val remoteInfo = try {
                val response = api.getCompanyInfo(symbol)
                response
            } catch (e: IOException) {
                e.printStackTrace()
                emit(
                    Resource.Error(
                        message = "Couldn't load company info"
                    )
                )
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(
                    Resource.Error(
                        message = "Couldn't load company info"
                    )
                )
                null
            }

            remoteInfo?.let { info ->
                dao.insertCompanyInfo(info.toCompanyInfo().toCompanyInfoEntity())
                val localInfo = dao.searchCompanyInfo(symbol)
                if (localInfo.isEmpty()){
                    emit(
                        Resource.Error(
                            message = "Couldn't load company info, try again later."
                        )
                    )
                    emit(Resource.Loading(false))
                }
                else{
                    emit(
                        Resource.Success(
                            data = localInfo.first().toCompanyInfo()
                        )
                    )
                    emit(Resource.Loading(false))
                }
            }

        }
    }

    override suspend fun getCompanyNews(symbol: String): Resource<List<CompanyNews>> {
        return try {
            val response = api.getCompanyNews(symbol)
            Resource.Success(data = response.jsonResponse?.map { it.toCompanyNews() })
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load news"
            )
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load news"
            )
        }
    }

    override suspend fun getIntradayInfo(symbol: String): Resource<List<IntradayInfo>> {
        return try {
            val response = api.getIntradayInfo(symbol)
            val results = intradayInfoParser.parse(response.byteStream())
            Resource.Success(results)
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load intraday info"
            )
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load intraday info"
            )
        }
    }

    override suspend fun getWatchlist(): Flow<Resource<List<Watchlist>>> {
        return flow {
            emit(Resource.Loading(true))
            val watchlist = dao.getWatchlistItems()
            emit(Resource.Success(data = watchlist.map { it.toWatchlist() }))

            emit(Resource.Loading(false))
            return@flow
        }
    }

    override suspend fun deleteWatchList(symbol: String) {
        dao.deleteWatchlistItem(symbol)
    }

    override suspend fun isInWatchlist(symbol: String): Boolean {
        val watchlistItems = dao.getWatchlistItems()
        for (item in watchlistItems) {
            if (item.symbol == symbol) {
                return true
            }
        }
        return false
    }


    override suspend fun addWatchlist(watchlist: Watchlist) {
        watchlist.let { item ->
            dao.insertWatchlistItem(item.toWatchlistEntity())
        }
    }


}