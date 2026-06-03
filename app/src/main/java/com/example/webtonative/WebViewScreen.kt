package com.example.webtonative

import android.graphics.Bitmap
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.webtonative.browsingHistoryDB.viewModel.HistoryViewModel
import com.example.webtonative.viewModel.WebViewViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewScreen(
    initialUrl: String,
    navController: NavController,
    webViewViewModel: WebViewViewModel = hiltViewModel(),
    historyViewModel: HistoryViewModel = hiltViewModel()
) {
    val uiState by webViewViewModel.uiState.collectAsStateWithLifecycle()

    var webView by remember {
        mutableStateOf<WebView?>(null)
    }
    val scrollState = rememberScrollState()

    val (backInteraction, backScale) = pressScale()
    val (closeInteraction, closeScale) = pressScale()

    BackHandler {
        if (webView?.canGoBack() == true) {
            webView?.goBack()
        } else {
            navController.popBackStack()
        }
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_color))
    ) {
        val (topBar, loadingIndicator, errorContainer, webViewContainer) = createRefs()

        Row (
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
                )
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
                        when {
                            webView?.canGoBack() == true -> {
                                webView?.goBack()
                            }

                            else -> {
                                navController.popBackStack()
                            }
                        }
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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                    .height(46.dp)
                    .weight(1f)
                    .background(
                        color = Color(0xFFF4F4F8),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border(
                        width = 1.1.dp,
                        color = Color(0xFFE5E7EB),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.lock_icon),
                        contentDescription = null,
                        tint = Color(0xFF555555),
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Text(
                        text = uiState.currentUrl,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Visible,
                        fontFamily = fonts,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        lineHeight = 18.sp,
                        color = Color(0xFF9CA3AF),
                        modifier = Modifier.horizontalScroll(scrollState)
                    )
                }
            }

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
                        interactionSource = closeInteraction,
                        indication = null
                    ) {
                        webViewViewModel.clearUrl()

                        navController.navigate("home") {
                            popUpTo("home") {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    }
                    .graphicsLayer(
                        scaleX = closeScale,
                        scaleY = closeScale
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.cancel_icon),
                    contentDescription = null,
                    tint = Color(0xFF555555),
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        if (uiState.progress in 1..99) {
            LinearProgressIndicator(
                progress = {
                    uiState.progress / 100f
                },
                color = Color(0xF23B5FF1),
                trackColor = Color(0xFFF6F7FB),
                modifier = Modifier
                    .constrainAs(loadingIndicator) {
                        top.linkTo(topBar.bottom, margin = 5.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)
            )
        }

        if (uiState.errorMessage != null) {
            Box(
                modifier = Modifier
                    .constrainAs(errorContainer) {
                        top.linkTo(webViewContainer.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)

                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                    .padding(15.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF6F7FB)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.errorMessage ?: "",
                    fontSize = 16.sp, fontFamily = fonts,
                    fontWeight = FontWeight.SemiBold, fontStyle = FontStyle.Normal,
                    color = colorResource(R.color.secondary_text_color),
                    textAlign = TextAlign.Center, lineHeight = 24.sp
                )
            }
        } else {
            AndroidView(
                modifier = Modifier
                    .constrainAs(webViewContainer) {
                        top.linkTo(topBar.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)

                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                    .padding(15.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF6F7FB)),
                factory = { context ->
                    WebView(context).apply {

                        webView = this

                        settings.apply {
                            javaScriptEnabled = true

                            domStorageEnabled = true

                            loadWithOverviewMode = true

                            useWideViewPort = true

                            builtInZoomControls = false

                            displayZoomControls = false
                        }

                        webChromeClient =
                            object : WebChromeClient() {
                                override fun onProgressChanged(
                                    view: WebView?,
                                    newProgress: Int
                                ) {
                                    webViewViewModel.updateProgress(
                                        newProgress
                                    )
                                }
                            }

                        webViewClient =
                            object : WebViewClient() {
                                override fun doUpdateVisitedHistory(
                                    view: WebView?,
                                    url: String?,
                                    isReload: Boolean
                                ) {
                                    super.doUpdateVisitedHistory(
                                        view,
                                        url,
                                        isReload
                                    )

                                    url?.let {
                                        webViewViewModel.updateUrl(it)
                                    }
                                }

                                override fun onPageFinished(
                                    view: WebView?,
                                    url: String?
                                ) {
                                    super.onPageFinished(
                                        view,
                                        url
                                    )

                                    webViewViewModel.clearError()

                                    url?.let {

                                        historyViewModel.saveVisit(
                                            url = it,
                                            title = view?.title ?: ""
                                        )
                                    }
                                }

                                override fun onReceivedError(
                                    view: WebView?,
                                    request: WebResourceRequest?,
                                    error: WebResourceError?
                                ) {
                                    if (request?.isForMainFrame == true) {

                                        webViewViewModel.showError(
                                            "No Internet Connection"
                                        )
                                    }
                                }

                                override fun onPageStarted(
                                    view: WebView?,
                                    url: String?,
                                    favicon: Bitmap?
                                ) {
                                    webViewViewModel.clearError()
                                }
                            }

                        loadUrl(initialUrl)
                    }
                }
            )
        }
    }
}