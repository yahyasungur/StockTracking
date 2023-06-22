package com.example.stocktracking.presentation.watchlist

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stocktracking.domain.model.Watchlist
import com.example.stocktracking.ui.theme.Green

@Composable
fun WatchlistItem(
    item: Watchlist,
    modifier: Modifier = Modifier,
) {
    val price = String.format("%.2f", item.endPrice.toDouble())
    val changeAmount =
        String.format("%.2f", (item.endPrice.toDouble() - item.startPrice.toDouble()))
    val changeRatio = String.format(
        "%.2f",
        ((item.endPrice.toDouble() - item.startPrice.toDouble()) / item.startPrice.toDouble()) * 100
    )
    var changeColor = Color.Red
    if (changeAmount.toDouble() > 0) {
        changeColor = Green
    } else if (changeAmount.toDouble() == 0.0) {
        changeColor = Color.Gray
    }

    Row(
        modifier = modifier, verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = item.symbol,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colors.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = price,
                    fontWeight = FontWeight.Bold,
                    fontSize = 23.sp,
                    color = MaterialTheme.colors.onBackground,
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = item.name,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colors.onBackground,
                    fontSize = 15.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "$changeAmount  (${changeRatio}%)",
                    fontWeight = FontWeight.SemiBold,
                    color = changeColor,
                    fontSize = 15.sp,
                    textAlign = TextAlign.End
                )

            }
        }
    }
}