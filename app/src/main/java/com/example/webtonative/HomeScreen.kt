package com.example.webtonative

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.webtonative.googleAuthentication.GoogleSignInManager
import com.example.webtonative.ui.theme.WebToNativeTheme
import kotlinx.coroutines.launch

class HomeScreen : ComponentActivity() {
    private lateinit var googleSignInManager: GoogleSignInManager

    override fun onCreate(savedInstanceState: Bundle?) {
        googleSignInManager = GoogleSignInManager(this)

        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                scrim = 0xFFF0F2F8.toInt()
            ),
            navigationBarStyle = SystemBarStyle.dark(
                scrim = 0xFFF0F2F8.toInt()
            )
        )

        setContent {
            WebToNativeTheme {
                Home_Screen(
                    onGoogleSignOut = {
                        googleSignInManager.signOut {
                            Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun Home_Screen(
    onGoogleSignOut: () -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val activity = context as? Activity
    var url by remember { mutableStateOf("") }
    var urlError by remember { mutableStateOf(false) }
    var urlFocused by remember { mutableStateOf(false) }
    val clipboard = LocalClipboard.current

    val (pasteInteraction, pasteScale) = pressScale()
    val (logoutInteraction, logoutScale) = pressScale()
    val (historyInteraction, historyScale) = pressScale()

    Scaffold(
        modifier = Modifier.background(colorResource(R.color.background_color)),
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 15.dp)
            ) { data ->
                Snackbar(
                    modifier = Modifier.fillMaxWidth()
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(10.dp),
                            ambientColor = Color(0xFF2C2C2C),
                            spotColor = Color(0xFF2C2C2C)
                        ),
                    containerColor = Color(0xFF2C2C2C),
                    shape = RoundedCornerShape(9.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(when {

                                else -> {
                                    R.drawable.alert_icon
                                }
                            } ), contentDescription = "Icons",
                            tint = colorResource(R.color.white), modifier = Modifier.size(24.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = data.visuals.message,
                            fontFamily = fonts,
                            fontWeight = FontWeight.SemiBold,
                            fontStyle = FontStyle.Normal,
                            fontSize = 13.sp,
                            color = colorResource(R.color.white)
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.background_color))
                .padding(paddingValues)
        ) {
            val (box, card, text1, box2, infoColumn, text2, button) = createRefs()

            Box(
                modifier = Modifier
                    .constrainAs(box) {
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
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .background(
                                color = colorResource(R.color.primary_text_color).copy(alpha = 0.20f),
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {

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
                            color = colorResource(R.color.primary_text_color),
                            textAlign = TextAlign.Center, lineHeight = 20.sp,
                            letterSpacing = 0.3.sp
                        )

                        Text(
                            text = "by Orufy",
                            fontSize = 12.sp, fontFamily = fonts,
                            fontWeight = FontWeight.Medium, fontStyle = FontStyle.Normal,
                            color = colorResource(R.color.secondary_text_color),
                            textAlign = TextAlign.Center, lineHeight = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Box(
                        modifier = Modifier
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
                                interactionSource = historyInteraction,
                                indication = null
                            ) {

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
                            tint = Color(0xFF555555),
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Box(
                        modifier = Modifier
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
                                interactionSource = logoutInteraction,
                                indication = null
                            ) {
                                onGoogleSignOut()

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
                            tint = Color(0xFF555555),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .constrainAs(card) {
                        top.linkTo(box.bottom, margin = 20.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth()
                    .height(260.dp)
                    .padding(horizontal = 25.dp)
                    .background(
                        color = colorResource(R.color.secondary_text_color).copy(alpha = 0.15f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .border(
                        width = 1.1.dp,
                        color = Color(0xFFE5E7EB),
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(
                            color = Color(0xFFF6F7FB),
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
                            text = "NATIVE UI BUILDER",
                            fontSize = 12.sp, fontFamily = fonts,
                            fontWeight = FontWeight.SemiBold, fontStyle = FontStyle.Normal,
                            color = Color(0xFF9CA3AF), lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Design native screens, no code",
                            fontSize = 15.sp, fontFamily = fonts,
                            fontWeight = FontWeight.Bold, fontStyle = FontStyle.Normal,
                            color = colorResource(R.color.primary_text_color),
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Compose splash, tabs and menus with a visual",
                            fontSize = 12.sp, fontFamily = fonts,
                            fontWeight = FontWeight.Medium, fontStyle = FontStyle.Normal,
                            color = Color(0xFF6B7280), lineHeight = 18.sp
                        )
                    }
                }
            }

            Text(
                modifier = Modifier
                    .constrainAs(text1) {
                        top.linkTo(card.bottom, margin = 25.dp)
                        start.linkTo(parent.start, margin = 25.dp)
                    },
                text = "Website URL",
                fontSize = 15.sp, fontFamily = fonts,
                fontWeight = FontWeight.Bold, fontStyle = FontStyle.Normal,
                color = colorResource(R.color.primary_text_color),
                lineHeight = 18.sp, letterSpacing = 0.2.sp
            )

            Box(
                modifier = Modifier
                    .constrainAs(box2) {
                        top.linkTo(text1.bottom, margin = 10.dp)
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
                        color = when {
                            urlError -> Color.Red
                            else -> Color(0xFFE5E7EB)
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
                        tint = Color(0xFF555555),
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    val selectionColors = TextSelectionColors(
                        handleColor = colorResource(R.color.primary_text_color).copy(alpha = 0.88f),
                        backgroundColor = colorResource(R.color.primary_text_color).copy(alpha = 0.3f)
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
                                .onFocusChanged {
                                    urlFocused = it.isFocused
                                },
                            textStyle = TextStyle(
                                fontFamily = fonts,
                                fontWeight = FontWeight.Medium,
                                fontStyle = FontStyle.Normal,
                                fontSize = 15.sp, lineHeight = 18.sp,
                                color = Color(0xFFB0B7C3)
                            ),
                            singleLine = true,
                            cursorBrush = SolidColor(colorResource(R.color.primary_text_color).copy(alpha = 0.88f)),
                            decorationBox = { innerTextField ->
                                Box {
                                    if (url.isEmpty()) {
                                        Text(
                                            text = "https://yourwebsite.com",
                                            fontFamily = fonts,
                                            fontWeight = FontWeight.Medium,
                                            fontStyle = FontStyle.Normal,
                                            fontSize = 15.sp, lineHeight = 18.sp,
                                            color = Color(0xFFB0B7C3)
                                        )
                                    }
                                    innerTextField()
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Row(
                        modifier = Modifier
                            .size(width = 90.dp, height = 40.dp)
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
                            tint = Color(0xFF555555),
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = "Paste",
                            fontSize = 12.5.sp, fontFamily = fonts,
                            fontWeight = FontWeight.Medium, fontStyle = FontStyle.Normal,
                            color = Color(0xFF374151),
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.constrainAs(infoColumn) {
                    top.linkTo(box2.bottom, margin = 10.dp)
                    start.linkTo(parent.start, margin = 25.dp)
                }
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

                Spacer(modifier = Modifier.height(if (urlError) 5.dp else 0.dp))

                Text(
                    text = "We'll wrap it into a native Android & IOS app.",
                    fontSize = 12.sp, fontFamily = fonts,
                    fontWeight = FontWeight.Medium, fontStyle = FontStyle.Normal,
                    color = Color(0xFF9CA3AF), lineHeight = 16.sp
                )
            }

            Button(
                modifier = Modifier
                    .constrainAs(button) {
                        top.linkTo(infoColumn.bottom, margin = 20.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 15.dp)
                    .background(
                        color = Color(0xFFF6F7FB),
                        shape = RoundedCornerShape(14.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xF23B5FF1),
                    contentColor = colorResource(R.color.background_color)
                ),
                shape = RoundedCornerShape(14.dp),
                onClick = {

                }
            ) {
                Text(
                    text = "Open App",
                    fontSize = 15.sp, fontFamily = fonts,
                    fontWeight = FontWeight.Bold, fontStyle = FontStyle.Normal,
                    color = colorResource(R.color.background_color),
                    lineHeight = 18.sp, letterSpacing = 0.2.sp
                )

                Icon(
                    painter = painterResource(R.drawable.arrow_right),
                    contentDescription = null,
                    tint = Color(0xFFFFFFFF),
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(22.dp)
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    WebToNativeTheme {
        Home_Screen(
            onGoogleSignOut = {}
        )
    }
}