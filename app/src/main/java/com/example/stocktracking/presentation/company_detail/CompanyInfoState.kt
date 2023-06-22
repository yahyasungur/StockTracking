package com.example.stocktracking.presentation.company_detail

import com.example.stocktracking.domain.model.CompanyInfo
import com.example.stocktracking.domain.model.CompanyNews
import com.example.stocktracking.domain.model.IntradayInfo

data class CompanyInfoState(
    val stockInfos: List<IntradayInfo> = emptyList(),
    val news: List<CompanyNews> = emptyList(),
    val company: CompanyInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
