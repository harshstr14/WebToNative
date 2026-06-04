package com.example.webtonative

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.webtonative.browsingHistoryDB.viewModel.HistoryViewModel
import com.example.webtonative.ui.theme.themeColors.AppThemeColors
import kotlinx.coroutines.launch
import java.nio.file.WatchEvent

data class FeatureItem(
    val image: Int,
    val title: String,
    val heading: String,
    val description: String
)

val features = listOf(
    FeatureItem(
        R.drawable.card_webview,
        "WebView Browsing",
        "Full web, inside your app",
        "Render any URL natively with controls, zoom, and back navigation."
    ),
    FeatureItem(
        R.drawable.card_notifications,
        "Notification Handling",
        "Stay in the loop, always",
        "Push alerts, deep links, and badge counts handled out of the box."
    ),
    FeatureItem(
        R.drawable.card_history,
        "Local History",
        "Every visit, remembered",
        "Browse your offline-ready history log with search and quick revisit."
    )
)

@Composable
fun HomeScreen(
    navController: NavController,
    onGoogleSignOut: () -> Unit,
    historyViewModel: HistoryViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val activity = context as? Activity
    var url by rememberSaveable { mutableStateOf("") }
    var urlError by rememberSaveable { mutableStateOf(false) }
    var urlFocused by rememberSaveable { mutableStateOf(false) }
    val clipboard = LocalClipboard.current
    val pagerState = rememberPagerState(
        pageCount = { features.size }
    )
    val scrollState = rememberScrollState()

    val configuration = LocalConfiguration.current

    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val (pasteInteraction, pasteScale) = pressScale()
    val (logoutInteraction, logoutScale) = pressScale()
    val (historyInteraction, historyScale) = pressScale()

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(AppThemeColors.colors.background_color)
    ) {
        val (headerCard, column) = createRefs()

        Box(
            modifier = Modifier
                .constrainAs(headerCard) {
                    top.linkTo(parent.top, margin = 15.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 15.dp)
                .background(
                    color = AppThemeColors.colors.surface_color,
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = 1.1.dp,
                    color = AppThemeColors.colors.border_color,
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "WebToNative",
                        fontSize = 17.sp, fontFamily = fonts,
                        fontWeight = FontWeight.Bold, fontStyle = FontStyle.Normal,
                        color = AppThemeColors.colors.primary_text_color,
                        textAlign = TextAlign.Center, lineHeight = 20.sp,
                        letterSpacing = 0.3.sp
                    )

                    Text(
                        text = "by Orufy",
                        fontSize = 12.sp, fontFamily = fonts,
                        fontWeight = FontWeight.Medium, fontStyle = FontStyle.Normal,
                        color = AppThemeColors.colors.secondary_text_color,
                        textAlign = TextAlign.Center, lineHeight = 16.sp
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .background(
                            color = AppThemeColors.colors.secondary_surface_color,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .border(
                            width = 1.1.dp,
                            color = AppThemeColors.colors.border_color,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable(
                            interactionSource = historyInteraction,
                            indication = null
                        ) {
                            if (navController.currentDestination?.route
                                != "history") {
                                navController.navigate("history")
                            }
                        }
                        .graphicsLayer(
                            scaleX = historyScale,
                            scaleY = historyScale
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.recent_history),
                        contentDescription = null,
                        tint = AppThemeColors.colors.icon_tint_color,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .background(
                            color = AppThemeColors.colors.secondary_surface_color,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .border(
                            width = 1.1.dp,
                            color = AppThemeColors.colors.border_color,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable(
                            interactionSource = logoutInteraction,
                            indication = null
                        ) {
                            onGoogleSignOut()
                            historyViewModel.deleteAllHistory()

                            val intent = Intent(context, SignInScreen::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            context.startActivity(intent)
                            activity?.finish()
                        }
                        .graphicsLayer(
                            scaleY = logoutScale,
                            scaleX = logoutScale
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.logout_icon),
                        contentDescription = null,
                        tint = AppThemeColors.colors.icon_tint_color,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .constrainAs(column) {
                    top.linkTo(headerCard.bottom, margin = 20.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)

                    height = Dimension.fillToConstraints
                }
                .verticalScroll(scrollState)
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 55.dp)
            ) {
                val (
                    featureCard, websiteUrlLabel, urlInputContainer,
                    urlInfoSection, openAppButton
                ) = createRefs()

                Column(
                    modifier = Modifier
                        .constrainAs(featureCard) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxWidth()
                    ) { page ->
                        val feature = features[page]

                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .then(
                                        if (isLandscape) {
                                            Modifier.width(400.dp)
                                        } else {
                                            Modifier.fillMaxWidth()
                                        }
                                    )
                                    .height(260.dp)
                                    .padding(horizontal = 25.dp)
                                    .background(
                                        color = AppThemeColors.colors.background_color,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .border(
                                        width = 1.1.dp,
                                        color = AppThemeColors.colors.border_color,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                            ) {
                                Image(
                                    painter = painterResource(feature.image),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(16.dp)),
                                    contentScale = ContentScale.FillBounds
                                )

                                Row(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .fillMaxWidth()
                                        .height(120.dp)
                                        .background(
                                            color = AppThemeColors.colors.surface_color,
                                            shape = RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp)
                                        ),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(end = 15.dp)
                                            .padding(horizontal = 18.dp),
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = feature.title.uppercase(),
                                            fontSize = 12.sp, fontFamily = fonts,
                                            fontWeight = FontWeight.SemiBold, fontStyle = FontStyle.Normal,
                                            color = AppThemeColors.colors.tertiary_text_color, lineHeight = 18.sp
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))

                                        Text(
                                            text = feature.heading,
                                            fontSize = 15.5.sp, fontFamily = fonts,
                                            fontWeight = FontWeight.Bold, fontStyle = FontStyle.Normal,
                                            color = AppThemeColors.colors.primary_text_color,
                                            lineHeight = 16.sp
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))

                                        Text(
                                            text = feature.description,
                                            fontSize = 12.5.sp, fontFamily = fonts,
                                            fontWeight = FontWeight.Medium, fontStyle = FontStyle.Normal,
                                            color = AppThemeColors.colors.secondary_text_color2, lineHeight = 18.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        repeat(features.size) { index ->
                            val isSelected = pagerState.currentPage == index

                            val indicatorColor by animateColorAsState(
                                targetValue = if (isSelected) {
                                    AppThemeColors.colors.primary_color
                                } else {
                                    AppThemeColors.colors.indicator_inactive_color
                                },
                                label = "indicator_color"
                            )

                            val width by animateDpAsState(
                                targetValue = if (isSelected) 24.dp else 8.dp,
                                label = "indicator_width"
                            )

                            Box(
                                modifier = Modifier
                                    .height(8.dp)
                                    .width(width)
                                    .clip(CircleShape)
                                    .background(indicatorColor)
                            )
                        }
                    }
                }

                Text(
                    modifier = Modifier
                        .constrainAs(websiteUrlLabel) {
                            top.linkTo(featureCard.bottom, margin = 25.dp)
                            start.linkTo(urlInfoSection.start)
                        }
                        .then(
                            if (isLandscape) {
                                Modifier.padding(start = 78.dp)
                            } else {
                                Modifier.padding(start = 18.dp)
                            }
                        ),
                    text = "Website URL",
                    fontSize = 15.5.sp, fontFamily = fonts,
                    fontWeight = FontWeight.Bold, fontStyle = FontStyle.Normal,
                    color = AppThemeColors.colors.primary_text_color,
                    lineHeight = 18.sp, letterSpacing = 0.2.sp
                )

                Box(
                    modifier = Modifier
                        .constrainAs(urlInputContainer) {
                            top.linkTo(websiteUrlLabel.bottom, margin = 10.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .fillMaxWidth()
                        .height(64.dp)
                        .then(
                            if (isLandscape) {
                                Modifier.padding(horizontal = 75.dp)
                            } else {
                                Modifier.padding(horizontal = 15.dp)
                            }
                        )
                        .background(
                            color = AppThemeColors.colors.surface_color,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .border(
                            width = 1.1.dp,
                            color = when {
                                urlError -> Color.Red
                                else -> AppThemeColors.colors.border_color
                            },
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(2.dp))

                        Icon(
                            painter = painterResource(R.drawable.internet_icon),
                            contentDescription = null,
                            tint = AppThemeColors.colors.icon_tint_color,
                            modifier = Modifier.size(22.dp)
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        val selectionColors = TextSelectionColors(
                            handleColor = colorResource(R.color.selection_handle_color),
                            backgroundColor = AppThemeColors.colors.secondary_text_color.copy(alpha = 0.10f)
                        )

                        CompositionLocalProvider(LocalTextSelectionColors provides selectionColors) {
                            BasicTextField(
                                value = url,
                                onValueChange = {
                                    url = it

                                    if (it.isNotBlank()) {
                                        urlError = false
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .onFocusChanged {
                                        urlFocused = it.isFocused
                                    },
                                textStyle = TextStyle(
                                    fontFamily = fonts,
                                    fontWeight = FontWeight.Medium,
                                    fontStyle = FontStyle.Normal,
                                    fontSize = 15.sp, lineHeight = 18.sp,
                                    color = AppThemeColors.colors.placeholder_text_color
                                ),
                                singleLine = true,
                                cursorBrush = SolidColor(AppThemeColors.colors.primary_text_color.copy(alpha = 0.88f)),
                                decorationBox = { innerTextField ->
                                    Box {
                                        if (url.isEmpty()) {
                                            Text(
                                                text = "https://yourwebsite.com",
                                                fontFamily = fonts,
                                                fontWeight = FontWeight.Medium,
                                                fontStyle = FontStyle.Normal,
                                                fontSize = 15.sp, lineHeight = 18.sp,
                                                color = AppThemeColors.colors.placeholder_text_color
                                            )
                                        }
                                        innerTextField()
                                    }
                                }
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Row(
                            modifier = Modifier
                                .size(width = 80.dp, height = 40.dp)
                                .background(
                                    color = AppThemeColors.colors.secondary_surface_color,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .border(
                                    width = 1.1.dp,
                                    color = AppThemeColors.colors.border_color,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable(
                                    interactionSource = pasteInteraction,
                                    indication = null
                                ) {
                                    scope.launch {
                                        val clipEntry = clipboard.getClipEntry()
                                        val clipboardText = clipEntry?.clipData
                                            ?.getItemAt(0)
                                            ?.text
                                            ?.toString()

                                        if (!clipboardText.isNullOrBlank()) {
                                            url = clipboardText
                                            urlError = false
                                        }
                                    }
                                }
                                .graphicsLayer(
                                    scaleX = pasteScale,
                                    scaleY = pasteScale
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.paste_icon),
                                contentDescription = null,
                                tint = AppThemeColors.colors.icon_tint_color,
                                modifier = Modifier.size(18.dp)
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Text(
                                text = "Paste",
                                fontSize = 12.5.sp, fontFamily = fonts,
                                fontWeight = FontWeight.Medium, fontStyle = FontStyle.Normal,
                                color = AppThemeColors.colors.dark_text_color,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .constrainAs(urlInfoSection) {
                            top.linkTo(urlInputContainer.bottom, margin = 5.dp)
                            start.linkTo(parent.start)
                        }
                        .then(
                            if (isLandscape) {
                                Modifier.padding(start = 78.dp)
                            } else {
                                Modifier.padding(start = 18.dp)
                            }
                        )
                ) {
                    AnimatedVisibility(urlError) {
                        Text(
                            text = "Url cannot be empty",
                            color = Color.Red,
                            fontSize = 12.sp,
                            lineHeight = 16.sp, fontFamily = fonts,
                            fontWeight = FontWeight.Medium, fontStyle = FontStyle.Normal
                        )
                    }

                    Spacer(modifier = Modifier.height(if (urlError) 5.dp else 5.dp))

                    Text(
                        text = "We'll wrap it into a native Android & IOS app.",
                        fontSize = 12.5.sp, fontFamily = fonts,
                        fontWeight = FontWeight.Medium, fontStyle = FontStyle.Normal,
                        color = AppThemeColors.colors.tertiary_text_color, lineHeight = 16.sp
                    )
                }

                Button(
                    modifier = Modifier
                        .constrainAs(openAppButton) {
                            top.linkTo(urlInfoSection.bottom, margin = 20.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .fillMaxWidth()
                        .height(64.dp)
                        .then(
                            if (isLandscape) {
                                Modifier.padding(horizontal = 75.dp)
                            } else {
                                Modifier.padding(horizontal = 15.dp)
                            }
                        )
                        .background(
                            color = AppThemeColors.colors.surface_color,
                            shape = RoundedCornerShape(16.dp)
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppThemeColors.colors.primary_color,
                        contentColor = AppThemeColors.colors.off_white
                    ),
                    shape = RoundedCornerShape(14.dp),
                    onClick = {
                        urlError = url.isBlank()

                        if (!urlError) {
                            navController.navigate(
                                "webview/${Uri.encode(url)}"
                            )
                        }
                    }
                ) {
                    Text(
                        text = "Open App",
                        fontSize = 16.sp, fontFamily = fonts,
                        fontWeight = FontWeight.Bold, fontStyle = FontStyle.Normal,
                        color = AppThemeColors.colors.off_white,
                        lineHeight = 18.sp, letterSpacing = 0.2.sp
                    )

                    Icon(
                        painter = painterResource(R.drawable.arrow_right),
                        contentDescription = null,
                        tint = AppThemeColors.colors.off_white,
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .size(22.dp)
                    )
                }
            }
        }
    }
}