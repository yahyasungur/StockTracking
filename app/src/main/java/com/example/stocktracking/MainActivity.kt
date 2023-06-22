package com.example.stocktracking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.stocktracking.presentation.NavGraphs
import com.example.stocktracking.presentation.destinations.Destination
import com.example.stocktracking.ui.composables.BottomBar
import com.example.stocktracking.ui.composables.SampleScaffold
import com.example.stocktracking.ui.composables.TopBar
import com.example.stocktracking.ui.theme.StockTrackingAppTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StockTrackingAppTheme {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    SampleScaffold(
                        navController = navController,
                        startRoute = NavGraphs.root.startRoute,
                        topBar = { dest: Destination, backStackEntry ->
                            TopBar(dest, backStackEntry)
                        },
                        bottomBar = {
                            BottomBar(navController)
                        }
                    ) {
                        DestinationsNavHost(
                            navController = navController,
                            navGraph = NavGraphs.root,
                            modifier = Modifier.padding(it),
                            startRoute = NavGraphs.root.startRoute
                        )
                    }
                }
            }
        }
    }
}
