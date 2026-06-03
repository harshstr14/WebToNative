package com.example.webtonative

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.webtonative.browsingHistoryDB.roomDB.HistoryEntity
import com.example.webtonative.browsingHistoryDB.viewModel.HistoryUiItem
import com.example.webtonative.browsingHistoryDB.viewModel.HistoryViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.abs

@Composable
fun HistoryScreen(
    navController: NavController,
    historyViewModel: HistoryViewModel = hiltViewModel()
) {
    val items by historyViewModel.groupedHistory.collectAsStateWithLifecycle(initialValue = emptyList())

    val (backInteraction, backScale) = pressScale()
    val (deleteInteraction, deleteScale) = pressScale()

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_color))
    ) {
        val (topBar, column, text) = createRefs()

        Row(
            modifier = Modifier
                .constrainAs(topBar) {
                    top.linkTo(parent.top, margin = 15.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 15.dp)
                .background(
                    color = Color(0xFFF6F7FB),
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = 1.1.dp,
                    color = Color(0xFFE5E7EB),
                    shape = RoundedCornerShape(16.dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .size(46.dp)
                    .background(
                        color = Color(0xFFF4F4F8),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border(
                        width = 1.1.dp,
                        color = Color(0xFFE5E7EB),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable(
                        interactionSource = backInteraction,
                        indication = null
                    ) {
                        navController.popBackStack()
                    }
                    .graphicsLayer(
                        scaleX = backScale,
                        scaleY = backScale
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_icon),
                    contentDescription = null,
                    tint = Color(0xFF555555),
                    modifier = Modifier.size(22.dp)
                )
            }

            Text(
                text = "History",
                fontSize = 18.sp, fontFamily = fonts,
                fontWeight = FontWeight.SemiBold, fontStyle = FontStyle.Normal,
                color = colorResource(R.color.primary_text_color),
                textAlign = TextAlign.Center, lineHeight = 20.sp,
                letterSpacing = 0.3.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .size(46.dp)
                    .background(
                        color = Color(0xFFF4F4F8),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border(
                        width = 1.1.dp,
                        color = Color(0xFFE5E7EB),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable(
                        interactionSource = deleteInteraction,
                        indication = null
                    ) {
                        historyViewModel.deleteAllHistory()
                    }
                    .graphicsLayer(
                        scaleX = deleteScale,
                        scaleY = deleteScale
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.delete_icon),
                    contentDescription = null,
                    tint = Color(0xFF555555),
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        if (items.isEmpty()) {
            Box(
                modifier = Modifier
                    .constrainAs(text) {
                        top.linkTo(topBar.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)

                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                    .padding(15.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No browsing history",
                    fontSize = 14.sp, fontFamily = fonts,
                    fontWeight = FontWeight.SemiBold, fontStyle = FontStyle.Normal,
                    color = Color(0xFF6B7280), lineHeight = 18.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .constrainAs(column) {
                        top.linkTo(topBar.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)

                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                    .padding(15.dp)
            ) {
                items(items) { item ->
                    when (item) {
                        is HistoryUiItem.Header -> {
                            Text(
                                text = item.title,
                                fontSize = 14.sp, fontFamily = fonts,
                                fontWeight = FontWeight.SemiBold, fontStyle = FontStyle.Normal,
                                color = Color(0xFF6B7280), lineHeight = 18.sp,
                                modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                            )
                        }

                        is HistoryUiItem.Item -> {
                            HistoryRow(item.data)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryRow(data: HistoryEntity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .height(68.dp)
            .background(
                color = Color(0xFFF6F7FB),
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.1.dp,
                color = Color(0xFFE5E7EB),
                shape = RoundedCornerShape(16.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val letter = data.title.firstOrNull()?.uppercase() ?: "W"
        val gradient = generateSoftGradient(data.url)

        Box(
            modifier = Modifier
                .padding(10.dp)
                .size(50.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = gradient
                    ),
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = letter,
                color = colorResource(R.color.off_white),
                fontWeight = FontWeight.Bold, fontSize = 18.sp, fontFamily = fonts,
                fontStyle = FontStyle.Normal, lineHeight = 20.sp
            )
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = data.title,
                fontSize = 14.5.sp, fontFamily = fonts,
                fontWeight = FontWeight.Bold, fontStyle = FontStyle.Normal,
                color = colorResource(R.color.primary_text_color),
                lineHeight = 16.sp, maxLines = 1
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = data.url,
                fontSize = 12.5.sp, fontFamily = fonts,
                fontWeight = FontWeight.Medium, fontStyle = FontStyle.Normal,
                color = colorResource(R.color.secondary_text_color),
                lineHeight = 16.sp, maxLines = 1
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = formatHistoryTime(data.lastVisitedTime),
            fontSize = 10.5.sp, fontFamily = fonts,
            fontWeight = FontWeight.SemiBold, fontStyle = FontStyle.Normal,
            color = Color(0xFF6B7280), lineHeight = 14.sp
        )

        Spacer(modifier = Modifier.width(10.dp))
    }
}

fun formatHistoryTime(timestamp: Long): String {
    val now = System.currentTimeMillis()

    val diff = now - timestamp

    val minute = 60 * 1000L
    val hour = 60 * minute
    val day = 24 * hour

    return when {
        diff < hour -> {
            "${diff / minute}m ago"
        }

        diff < day && !isYesterday(timestamp) -> {
            "${diff / hour}h ago"
        }

        isYesterday(timestamp) -> {
            val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
            "Yesterday ${timeFormat.format(Date(timestamp))}"
        }

        else -> {
            val dateFormat = SimpleDateFormat("MMM d", Locale.getDefault())
            dateFormat.format(Date(timestamp))
        }
    }
}

fun isYesterday(timestamp: Long): Boolean {
    val today = Calendar.getInstance()

    val yesterday = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_YEAR, -1)
    }

    val itemDate = Calendar.getInstance().apply {
        timeInMillis = timestamp
    }

    return itemDate.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) &&
            itemDate.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)
}

fun generateSoftGradient(seed: String): List<Color> {
    val hash = abs(seed.hashCode())

    val hue1 = hash % 360
    val hue2 = (hue1 + 25) % 360

    return listOf(
        Color.hsv(
            hue = hue1.toFloat(),
            saturation = 0.35f,
            value = 0.95f
        ),
        Color.hsv(
            hue = hue2.toFloat(),
            saturation = 0.45f,
            value = 0.85f
        )
    )
}