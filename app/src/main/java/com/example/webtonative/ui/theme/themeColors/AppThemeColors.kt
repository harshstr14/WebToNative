package com.example.webtonative.ui.theme.themeColors

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.activity.enableEdgeToEdge
import androidx.activity.SystemBarStyle

data class ThemeColors(
    val background_color: Color,
    val surface_color: Color,
    val secondary_surface_color: Color,

    val border_color: Color,

    val primary_text_color: Color,
    val secondary_text_color: Color,
    val tertiary_text_color: Color,
    val placeholder_text_color: Color,
    val secondary_text_color2: Color,
    val dark_text_color: Color,

    val icon_tint_color: Color,

    val primary_color: Color,
    val primary_color_alpha: Color,

    val indicator_inactive_color: Color,

    val selection_handle_color: Color,

    val error_color: Color,

    val off_white: Color
)

object AppThemeColors {

    var colors by mutableStateOf(LightColors)
        private set

    var isDark by mutableStateOf(false)
        private set

    fun update(dark: Boolean) {
        isDark = dark
        colors = if (dark) DarkColors else LightColors
    }
}

object SystemBarController {

    fun getBg(isDark: Boolean): Color {
        return if (isDark)
            Color(0xFF0D0D14)
        else
            Color(0xFFF0F2F8)
    }
}