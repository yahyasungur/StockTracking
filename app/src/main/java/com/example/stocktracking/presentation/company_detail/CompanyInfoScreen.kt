package com.example.stocktracking.presentation.company_detail

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.stocktracking.domain.model.Watchlist
import com.example.stocktracking.ui.theme.Complementary
import com.example.stocktracking.ui.theme.Green
import com.example.stocktracking.ui.theme.TextWhite
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.util.*

@Composable
@Destination
fun CompanyInfoScreen(
    symbol: String,
    navigator: DestinationsNavigator,
    viewModel: CompanyInfoViewModel = hiltViewModel()
) {
    val state = viewModel.state

    var watchlistItem: Watchlist? = null

    if (state.stockInfos.isNotEmpty()) {
        watchlistItem = state.company?.let {
            Watchlist(
                symbol = it.symbol,
                name = state.company.name,
                startPrice = state.stockInfos.first().close.toString(),
                endPrice = state.stockInfos.last().close.toString()
            )
        }

        // if the stock already in watchlist update the info about stock in DB
        if (viewModel.isInWatchlist) {
            if (watchlistItem != null) {
                viewModel.addWatchlist(watchlistItem)
            }
        }
    }

    if (state.error == null) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                StaticSection(viewModel, watchlistItem)
            }
            item {
                SlidingSection(state)
            }
        }

    }
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Center
    ) {
        if (state.isLoading) {
            CircularProgressIndicator()
        } else if (state.error != null) {
            Text(
                text = state.error, color = MaterialTheme.colors.error
            )
        }
    }
}

@Composable
fun StaticSection(
    viewModel: CompanyInfoViewModel, watchlistItem: Watchlist?
) {
    val state = viewModel.state
    var colour = Color.Red
    var changeAmount = "0.0"
    var changeRatio = "0.0"
    val context = LocalContext.current
    var isFavorite by remember { mutableStateOf(false) }
    if (viewModel.isInWatchlist) {
        isFavorite = true
    }

    if (state.stockInfos.isNotEmpty()) {
        changeAmount =
            String.format("%.2f", (state.stockInfos.last().close - state.stockInfos.first().close))
        changeRatio = String.format(
            "%.2f",
            ((state.stockInfos.last().close - state.stockInfos.first().close) / state.stockInfos.first().close) * 100
        )
        if (state.stockInfos.first().close < state.stockInfos.last().close) {
            colour = Green
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFF3B3B3B))
            .padding(16.dp)
    ) {
        state.company?.let { company ->
            Box(
                modifier = Modifier
                    .clip(
                        shape = RoundedCornerShape(
                            topStart = 5.dp, topEnd = 20.dp, bottomStart = 30.dp, bottomEnd = 20.dp
                        )
                    )
                    .background(color = Complementary)
                    .padding(15.dp)
                    .fillMaxWidth()
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = company.symbol,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = company.name,
                            fontStyle = FontStyle.Italic,
                            fontSize = 14.sp,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        if (state.stockInfos.isNotEmpty()) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                ConstraintLayout {
                                    val (textPrice, textRatio) = createRefs()
                                    Text(text = "${state.stockInfos.last().close}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 23.sp,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.constrainAs(textPrice) {
                                            start.linkTo(parent.start)
                                        })
                                    Text(text = "$changeAmount (${changeRatio}%)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        overflow = TextOverflow.Ellipsis,
                                        color = colour,
                                        textAlign = TextAlign.Left,
                                        modifier = Modifier.constrainAs(textRatio) {
                                            start.linkTo(textPrice.end, margin = 8.dp)
                                            bottom.linkTo(textPrice.bottom)
                                        })
                                }
                            }
                        }
                    }
                    Column(
                        modifier = Modifier.weight(0.2F)
                    ) {
                        if (state.stockInfos.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(90.dp)
                            ) {
                                Spacer(modifier = Modifier.width(20.dp))
                                IconToggleButton(checked = isFavorite, onCheckedChange = {
                                    viewModel.isInWatchlist = !viewModel.isInWatchlist
                                    isFavorite = !isFavorite
                                    if (viewModel.isInWatchlist) {
                                        if (watchlistItem != null) {
                                            viewModel.addWatchlist(watchlistItem)
                                        }
                                        Toast.makeText(
                                            context,
                                            "${company.symbol} added to Watch List!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        viewModel.deleteWatchlist(company.symbol)
                                        Toast.makeText(
                                            context,
                                            "${company.symbol} removed from Watch List!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }) {
                                    Icon(
                                        tint = Color(0xFFE7AF09),
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(8.dp)
                                            .graphicsLayer {
                                                scaleX = 2.3f
                                                scaleY = 2.3f
                                            },
                                        imageVector = if (isFavorite) {
                                            Icons.Filled.Star
                                        } else {
                                            Icons.Default.StarBorder
                                        },
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SlidingSection(
    state: CompanyInfoState
) {
    Column(
        modifier = Modifier
            .height(510.dp)
            .background(color = Color(0xFF3B3B3B))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())

    ) {
        state.company?.let { company ->
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.width(100.dp)) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = " Sector",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        modifier = Modifier.fillMaxWidth(),
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = " Industry",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.fillMaxWidth(),
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = " Country",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.fillMaxWidth(),
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Column(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = capitalize(company.sector.lowercase()),
                        fontSize = 14.sp,
                        modifier = Modifier.fillMaxWidth(),
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = capitalize(company.industry.lowercase()),
                        fontSize = 14.sp,
                        modifier = Modifier.fillMaxWidth(),
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = company.country,
                        fontSize = 14.sp,
                        modifier = Modifier.fillMaxWidth(),
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            ExpandableText(text = company.description, minimizedMaxLines = 5)
            Spacer(modifier = Modifier.height(13.dp))
            if (state.stockInfos.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Spacer(modifier = Modifier.height(25.dp))
                StockChart(
                    infos = state.stockInfos,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .align(CenterHorizontally)
                )
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.width(200.dp)) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "   52 Weeks High",
                        fontSize = 12.sp,
                        color = TextWhite,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.fillMaxWidth(),
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Divider(modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "   52 Weeks Low",
                        fontSize = 12.sp,
                        color = TextWhite,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.fillMaxWidth(),
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Divider(modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "   50 Days Moving Average",
                        fontSize = 12.sp,
                        color = TextWhite,
                        fontWeight = FontWeight.Medium,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Divider(modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "   200 Days Moving Average",
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxWidth(),
                        color = TextWhite,
                        fontWeight = FontWeight.Medium,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Divider(modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "   Analyst Target Price",
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxWidth(),
                        color = TextWhite,
                        fontWeight = FontWeight.Medium,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Column(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = company.weekHigh52,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Light,
                        color = TextWhite,
                        modifier = Modifier.fillMaxWidth(),
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Divider(modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = company.weekLow52,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Light,
                        color = TextWhite,
                        modifier = Modifier.fillMaxWidth(),
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Divider(modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = company.dayMovingAverage50,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Light,
                        color = TextWhite,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Divider(modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = company.dayMovingAverage200,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Light,
                        modifier = Modifier.fillMaxWidth(),
                        color = TextWhite,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Divider(modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = company.analystTargetPrice,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Light,
                        modifier = Modifier.fillMaxWidth(),
                        color = TextWhite,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        NewsSection(state = state)
    }
}

@Composable
fun NewsSection(
    state: CompanyInfoState
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(state.news.size) { i ->
            val new = state.news[i]
            CompanyNewsItem(
                new = new, modifier = Modifier.padding(10.dp)
            )
        }
    }
}

@Composable
fun ExpandableText(
    text: String,
    modifier: Modifier = Modifier,
    minimizedMaxLines: Int = 1,
) {
    var cutText by remember(text) { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }
    val textLayoutResultState = remember { mutableStateOf<TextLayoutResult?>(null) }
    val seeMoreSizeState = remember { mutableStateOf<IntSize?>(null) }
    val seeMoreOffsetState = remember { mutableStateOf<Offset?>(null) }

    // getting raw values for smart cast
    val textLayoutResult = textLayoutResultState.value
    val seeMoreSize = seeMoreSizeState.value
    val seeMoreOffset = seeMoreOffsetState.value

    LaunchedEffect(text, expanded, textLayoutResult, seeMoreSize) {
        val lastLineIndex = minimizedMaxLines - 1
        if (!expanded && textLayoutResult != null && seeMoreSize != null && lastLineIndex + 1 == textLayoutResult.lineCount && textLayoutResult.isLineEllipsized(
                lastLineIndex
            )
        ) {
            var lastCharIndex = textLayoutResult.getLineEnd(lastLineIndex, visibleEnd = true) + 1
            var charRect: Rect
            do {
                lastCharIndex -= 1
                charRect = textLayoutResult.getCursorRect(lastCharIndex)
            } while (charRect.left > textLayoutResult.size.width - seeMoreSize.width)
            seeMoreOffsetState.value = Offset(charRect.left, charRect.bottom - seeMoreSize.height)
            cutText = text.substring(startIndex = 0, endIndex = lastCharIndex)
        }
    }

    Box(modifier) {
        Text(
            text = cutText ?: text,
            maxLines = if (expanded) Int.MAX_VALUE else minimizedMaxLines,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { textLayoutResultState.value = it },
            fontSize = 12.sp,
            modifier = Modifier.fillMaxWidth()
        )
        if (!expanded) {
            val density = LocalDensity.current
            Text("... See more",
                onTextLayout = { seeMoreSizeState.value = it.size },
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .then(
                        if (seeMoreOffset != null) Modifier.offset(
                            x = with(density) { seeMoreOffset.x.toDp() },
                            y = with(density) { seeMoreOffset.y.toDp() },
                        )
                        else Modifier
                    )
                    .clickable {
                        expanded = true
                        cutText = null
                    }
                    .alpha(if (seeMoreOffset != null) 1f else 0f))
        }
    }
}


fun capitalize(str: String): String {
    return str.trim().split("\\s+".toRegex())
        .joinToString(" ") { it -> it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() } }
}
