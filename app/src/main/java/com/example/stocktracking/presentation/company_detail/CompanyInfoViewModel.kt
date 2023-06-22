package com.example.stocktracking.presentation.company_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stocktracking.domain.model.Watchlist
import com.example.stocktracking.domain.repository.StockRepository
import com.example.stocktracking.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle, private val repository: StockRepository
) : ViewModel() {

    var state by mutableStateOf(CompanyInfoState())
    var isInWatchlist = false
    var symbol: String = ""

    init {
        viewModelScope.launch {
            symbol = savedStateHandle.get<String>("symbol") ?: return@launch
            state = state.copy(isLoading = true)
            val companyInfoResult = async { repository.getCompanyInfo(symbol) }
            val intradayInfoResult = async { repository.getIntradayInfo(symbol) }
            val companyNewsResult = async { repository.getCompanyNews(symbol) }

            companyInfoResult.await().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data.let { info ->
                            state = state.copy(company = info)
                        }
                    }
                    is Resource.Error -> Unit
                    is Resource.Loading -> {
                        state = state.copy(isLoading = result.isLoading)
                    }
                }
            }
            when (val result = intradayInfoResult.await()) {
                is Resource.Success -> {
                    state = state.copy(
                        stockInfos = result.data ?: emptyList(), isLoading = false, error = null
                    )
                }
                is Resource.Error -> {
                    state = state.copy(
                        isLoading = false, error = result.message, company = null
                    )
                }
                else -> Unit
            }
            when (val result = companyNewsResult.await()) {
                is Resource.Success -> {
                    state = state.copy(
                        news = result.data ?: emptyList(), isLoading = false, error = null
                    )
                }
                is Resource.Error -> {
                    state = state.copy(
                        isLoading = false, error = result.message, company = null
                    )
                }
                else -> Unit
            }
        }
        viewModelScope.launch {
            isInWatchlist = repository.isInWatchlist(symbol)
        }
    }

    fun addWatchlist(
        item: Watchlist
    ) {
        deleteWatchlist(item.symbol)
        viewModelScope.launch {
            repository.addWatchlist(item)
        }
    }

    fun deleteWatchlist(
        symbol: String
    ) {
        viewModelScope.launch {
            repository.deleteWatchList(symbol)
        }
    }
}