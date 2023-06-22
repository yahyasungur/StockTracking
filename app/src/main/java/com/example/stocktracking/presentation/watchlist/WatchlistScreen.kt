package com.example.stocktracking.presentation.watchlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.stocktracking.presentation.destinations.CompanyInfoScreenDestination
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun WatchlistScreen(
    navigator: DestinationsNavigator, viewModel: WatchlistViewModel = hiltViewModel()
) {
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = viewModel.state.isRefreshing)
    val state = viewModel.state
    viewModel.getWatchlist()

    Column(modifier = Modifier.fillMaxSize()) {
        SwipeRefresh(state = swipeRefreshState, onRefresh = {
            viewModel.onEvent(WatchlistEvent.Refresh)
        }) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.watchlists.size) { i ->
                    val item = state.watchlists[i]
                    WatchlistItem(item = item, modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navigator.navigate(
                                CompanyInfoScreenDestination(item.symbol)
                            )
                        }
                        .padding(16.dp))
                    if (i < state.watchlists.size) {
                        Divider(
                            modifier = Modifier.padding(
                                horizontal = 16.dp
                            )
                        )
                    }
                }
            }
        }
    }
}