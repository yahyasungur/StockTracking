package com.example.stocktracking.presentation.company_detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.stocktracking.R
import com.example.stocktracking.domain.model.CompanyNews

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CompanyNewsItem(
    new: CompanyNews, modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var sentiment = "neutral"
    if (new.sentiment.lowercase().contains("bull")) {
        sentiment = "bullish"
    } else if (new.sentiment.lowercase().contains("bear")) {
        sentiment = "bearish"
    }

    Card(modifier = Modifier.fillMaxWidth(0.5f),
        shape = RoundedCornerShape(15.dp),
        elevation = 5.dp,
        onClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(new.url))
            startActivity(context, intent, null)
        }) {
        Box(
            modifier = Modifier
                .size(130.dp)
                .background(Color.DarkGray)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context).data(new.banner_image).crossfade(true)
                    .build(),
                contentDescription = null,
                placeholder = painterResource(id = R.drawable.placeholder),
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent, Color.Black
                            ), startY = 60f
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Text(
                    text = new.title, style = TextStyle(
                        color = Color.White,
                        fontSize = 12.sp,
                    ), maxLines = 4, overflow = TextOverflow.Ellipsis
                )
            }
            if (sentiment != "neutral") {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0x94000000), Color.Transparent
                                ), start = Offset(x = 0f, y = 0f), end = Offset(x = 70f, y = 70f)
                            )
                        ), contentAlignment = Alignment.TopStart
                ) {
                    if (sentiment == "bullish") {
                        Image(
                            painter = painterResource(id = R.drawable.bullish),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )

                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.bearish),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            }

        }
    }


}