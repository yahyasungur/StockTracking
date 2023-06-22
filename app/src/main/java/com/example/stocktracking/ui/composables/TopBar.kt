package com.example.stocktracking.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import com.example.stocktracking.R
import com.example.stocktracking.presentation.destinations.CompanyInfoScreenDestination
import com.example.stocktracking.presentation.destinations.CompanyListingsScreenDestination
import com.example.stocktracking.presentation.destinations.Destination
import com.example.stocktracking.presentation.destinations.WatchlistScreenDestination
import com.example.stocktracking.ui.theme.Complementary
import com.example.stocktracking.ui.theme.TextWhite

@Composable
fun TopBar(
    destination: Destination,
    navBackStackEntry: NavBackStackEntry?
) {
    TopAppBar(
        modifier = Modifier.height(40.dp),
        backgroundColor = Complementary,
        contentColor = TextWhite
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(8.dp))
            Text(
                text = destination.topBarTitle(navBackStackEntry),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = TextWhite
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon2),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(140.dp)
                        .alpha(0.6f),
                )
            }
        }

    }
}

@Composable
fun Destination.topBarTitle(navBackStackEntry: NavBackStackEntry?): String {
    return when (this) {
        CompanyListingsScreenDestination -> {
            "Stocks"
        }
        CompanyInfoScreenDestination -> {
            "Stock Detail"
        }
        WatchlistScreenDestination -> {
            "Watch List"
        }
    }
}