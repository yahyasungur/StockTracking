package com.example.stocktracking.ui.composables

import androidx.compose.foundation.layout.height
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.stocktracking.presentation.NavGraphs
import com.example.stocktracking.presentation.destinations.CompanyListingsScreenDestination
import com.example.stocktracking.presentation.destinations.DirectionDestination
import com.example.stocktracking.presentation.destinations.WatchlistScreenDestination
import com.example.stocktracking.ui.theme.Complementary
import com.example.stocktracking.ui.theme.TextWhite
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popBackStack
import com.ramcosta.composedestinations.navigation.popUpTo
import com.ramcosta.composedestinations.utils.isRouteOnBackStack

@Composable
fun BottomBar(
    navController: NavHostController
) {
    BottomNavigation(
        modifier = Modifier.height(55.dp),
        backgroundColor = Complementary,
    ) {
        BottomBarItem.values().forEach { destination ->
            val isCurrentDestOnBackStack = navController.isRouteOnBackStack(destination.direction)
            BottomNavigationItem(
                selected = isCurrentDestOnBackStack,
                selectedContentColor = TextWhite,
                unselectedContentColor = TextWhite,
                onClick = {
                    if (isCurrentDestOnBackStack) {
                        // When we click again on a bottom bar item and it was already selected
                        // we want to pop the back stack until the initial destination of this bottom bar item
                        navController.popBackStack(destination.direction, false)
                        return@BottomNavigationItem
                    }

                    navController.navigate(destination.direction) {
                        // Pop up to the root of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(NavGraphs.root) {
                            saveState = true
                        }

                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        destination.icon, contentDescription = destination.label
                    )
                },
                label = { Text(destination.label) },
            )
        }
    }
}

enum class BottomBarItem(
    val direction: DirectionDestination, val icon: ImageVector, val label: String
) {
    CompanyListing(CompanyListingsScreenDestination, Icons.Default.List, "Stocks"), Watchlist(
        WatchlistScreenDestination,
        Icons.Default.Star,
        "Watch List"
    ),
}