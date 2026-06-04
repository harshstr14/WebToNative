package com.example.webtonative

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.webtonative.googleAuthentication.GoogleSignInManager
import com.example.webtonative.notifications.WelcomeNotificationWorker
import com.example.webtonative.ui.theme.WebToNativeTheme
import com.example.webtonative.ui.theme.themeColors.AppThemeColors
import com.google.firebase.auth.FirebaseAuth

class SignInScreen : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    private lateinit var googleSignInManager: GoogleSignInManager

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            googleSignInManager.handleSignInResult(
                it,
                onSuccess = { auth ->
                    WelcomeNotificationWorker.schedule(this@SignInScreen)

                    startActivity(
                        Intent(this@SignInScreen, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                    )
                },
                onError = { message ->
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            )
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        googleSignInManager = GoogleSignInManager(this)
        auth = FirebaseAuth.getInstance()

        super.onCreate(savedInstanceState)

        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser != null) {
            startActivity(
                Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            )
            finish()
            return
        }

        setContent {
            val isDark = isSystemInDarkTheme()

            LaunchedEffect(isDark) {
                AppThemeColors.update(isDark)
            }

            UpdateSystemBars()

            WebToNativeTheme {
                SignIn_Screen(
                    onGoogleSignIn = {
                        googleSignInManager.signIn(launcher)
                    }
                )
            }
        }
    }
}

val fonts = FontFamily(
    Font(R.font.poppins_bold, FontWeight.Bold),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium)
)

@Composable
fun UpdateSystemBars() {
    val activity = LocalContext.current as ComponentActivity

    val bg = AppThemeColors.colors.background_color.toArgb()
    val isDark = AppThemeColors.isDark

    SideEffect {
        if (isDark) {
            activity.enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.dark(bg),
                navigationBarStyle = SystemBarStyle.dark(bg)
            )
        } else {
            activity.enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.light(
                    scrim = bg,
                    darkScrim = bg
                ),
                navigationBarStyle = SystemBarStyle.light(
                    scrim = bg,
                    darkScrim = bg
                )
            )
        }
    }
}

@Composable
fun SignIn_Screen(
    onGoogleSignIn: () -> Unit
) {
    val configuration = LocalConfiguration.current

    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val snackBarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier.background(AppThemeColors.colors.background_color),
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
                .background(AppThemeColors.colors.background_color)
                .padding(paddingValues)
        ) {
            val (
                termsAndPrivacyText, googleSignInButton, leftDivider,
                rightDivider, signInWithText, appLogo
            ) = createRefs()

            Column(
                modifier = Modifier
                    .constrainAs(appLogo) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(signInWithText.top)
                    }
                    .then(
                        if (isLandscape) {
                            Modifier.padding(top = 0.dp)
                        } else {
                            Modifier.padding(top = 65.dp)
                        }
                    )
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.image),
                    contentDescription = null,
                    modifier = Modifier
                        .size(150.dp)
                )

                Box(
                    modifier = Modifier
                        .size(84.dp)
                        .clip(RoundedCornerShape(20.dp))
                ) {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "WebToNative",
                    modifier = Modifier
                        .padding(horizontal = 45.dp),
                    fontSize = 26.sp, fontFamily = fonts,
                    fontWeight = FontWeight.Bold, fontStyle = FontStyle.Normal,
                    color = AppThemeColors.colors.primary_text_color,
                    textAlign = TextAlign.Center, lineHeight = 28.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Turn your web app into a native experience",
                    modifier = Modifier
                        .padding(horizontal = 45.dp),
                    fontSize = 16.sp, fontFamily = fonts,
                    fontWeight = FontWeight.SemiBold, fontStyle = FontStyle.Normal,
                    color = AppThemeColors.colors.secondary_text_color,
                    textAlign = TextAlign.Center, lineHeight = 24.sp
                )
            }

            Box(
                modifier = Modifier
                    .constrainAs(leftDivider) {
                        top.linkTo(signInWithText.top)
                        bottom.linkTo(signInWithText.bottom)
                        end.linkTo(signInWithText.start, margin = 12.dp)
                    }
                    .width(90.dp).height(1.2.dp)
                    .drawWithCache {
                        onDrawBehind {
                            drawLine(
                                color = Color(0xFF797979).copy(alpha = 0.40f),
                                start = Offset(size.width / 2, 0f),
                                end = Offset(size.width / 2, size.height),
                                strokeWidth = size.width
                            )
                        }
                    }
            )

            Text(
                text = "SIGN IN WITH",
                modifier = Modifier
                    .constrainAs(signInWithText) {
                        bottom.linkTo(googleSignInButton.top, margin = 18.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                fontSize = 15.sp, fontFamily = fonts,
                fontWeight = FontWeight.Medium, fontStyle = FontStyle.Normal,
                color = AppThemeColors.colors.secondary_text_color2,
                textAlign = TextAlign.Center, lineHeight = 18.sp
            )

            Box(
                modifier = Modifier
                    .constrainAs(rightDivider) {
                        top.linkTo(signInWithText.top)
                        bottom.linkTo(signInWithText.bottom)
                        start.linkTo(signInWithText.end, margin = 12.dp)
                    }
                    .width(90.dp).height(1.2.dp)
                    .drawWithCache {
                        onDrawBehind {
                            drawLine(
                                color = Color(0xFF797979).copy(alpha = 0.4f),
                                start = Offset(size.width / 2, 0f),
                                end = Offset(size.width / 2, size.height),
                                strokeWidth = size.width
                            )
                        }
                    }
            )

            Box(
                modifier = Modifier
                    .constrainAs(googleSignInButton) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(termsAndPrivacyText.top, margin = 25.dp)
                    }
                    .then(
                        if (isLandscape) {
                            Modifier.padding(horizontal = 115.dp)
                        } else {
                            Modifier.padding(horizontal = 30.dp)
                        }
                    )
                    .height(68.dp)
                    .fillMaxWidth()
                    .clickable {
                        onGoogleSignIn()
                    }
                    .background(
                        color = AppThemeColors.colors.primary_text_color.copy(alpha = 0.95f),
                        shape = RoundedCornerShape(26.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.google_icon),
                        contentDescription = "Google Icon",
                        tint = Color.Unspecified
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "Continue with Google", fontSize = 17.sp,
                        fontFamily = fonts, fontWeight = FontWeight.SemiBold,
                        fontStyle = FontStyle.Normal, lineHeight = 20.sp,
                        color = AppThemeColors.colors.background_color
                    )
                }
            }

            Row (
                modifier = Modifier
                    .constrainAs(termsAndPrivacyText) {
                        bottom.linkTo(parent.bottom, margin = 25.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(horizontal = 30.dp)
            ) {
                val annotatedText = buildAnnotatedString {
                    append("By continuing, you agree to our ")

                    pushStringAnnotation(tag = "TERMS", annotation = "terms")
                    withStyle(
                        SpanStyle(
                            color = AppThemeColors.colors.primary_text_color,
                            fontWeight = FontWeight.SemiBold
                        )
                    ) {
                        append("Terms of Service")
                    }
                    pop()

                    append(" and ")

                    pushStringAnnotation(tag = "PRIVACY", annotation = "privacy")
                    withStyle(
                        SpanStyle(
                            color = AppThemeColors.colors.primary_text_color,
                            fontWeight = FontWeight.SemiBold
                        )
                    ) {
                        append("Privacy Policy")
                    }
                    pop()
                }

                ClickableText(
                    text = annotatedText,
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 18.sp,
                        fontFamily = fonts,
                        fontWeight = FontWeight.Medium,
                        color = AppThemeColors.colors.secondary_text_color2,
                        textAlign = TextAlign.Center
                    )
                ) { offset ->
                    annotatedText.getStringAnnotations(
                        start = offset,
                        end = offset
                    ).firstOrNull()?.let { annotation ->
                        when (annotation.tag) {
                            "TERMS" -> {
                                // Open Terms
                            }
                            "PRIVACY" -> {
                                // Open Privacy Policy
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun pressScale(
    pressedScale: Float = 1.15f
): Pair<MutableInteractionSource, Float> {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) pressedScale else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "PressScale"
    )

    return interactionSource to scale
}

@Preview(showSystemUi = true)
@Composable
private fun SignInScreenPreview() {
    WebToNativeTheme {
        SignIn_Screen(
            onGoogleSignIn = {}
        )
    }
}