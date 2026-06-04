package com.example.webtonative.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.MaterialTheme

object AppColors {

    /* ========== TEXT SYSTEM ========== */

    val primaryText: Color
        @Composable get() = MaterialTheme.colorScheme.onBackground

    val secondaryText: Color
        @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant

    val tertiaryText = Color(0xFF9CA3AF)

    val secondaryText2: Color
        @Composable get() = if (MaterialTheme.colorScheme.background == Color(0xFFF0F2F8))
            Color(0xFF6B7280)
        else
            Color(0xFF6B6B90)

    val darkText: Color
        @Composable get() = if (MaterialTheme.colorScheme.background == Color(0xFFF0F2F8))
            Color(0xFF374151)
        else
            Color(0xFF8888B0)

    val placeholderText: Color
        @Composable get() = if (MaterialTheme.colorScheme.background == Color(0xFFF0F2F8))
            Color(0xFFB0B7C3)
        else
            Color(0xFF3A3A55)

    /* ========== BACKGROUNDS (extra semantic) ========== */

    val offWhite = Color(0xFFFAF9F6)

    /* ========== ICONS ========== */

    val iconTint: Color
        @Composable get() = if (MaterialTheme.colorScheme.background == Color(0xFFF0F2F8))
            Color(0xFF555555)
        else
            Color(0xFF8888B0)

    /* ========== INDICATORS ========== */

    val indicatorInactive: Color
        @Composable get() = if (MaterialTheme.colorScheme.background == Color(0xFFF0F2F8))
            Color(0xFFD1D5DB)
        else
            Color(0xFF2A2A40)

    /* ========== SELECTION ========== */

    val selectionHandle: Color
        @Composable get() = if (MaterialTheme.colorScheme.background == Color(0xFFF0F2F8))
            Color(0xFF555555)
        else
            Color(0xFF555555)

    /* ========== BRAND ========== */

    val primary = Color(0xFF3B5FF1)

    val primaryAlpha = Color(0xFFF23B5FF1)

    /* ========== ERROR ========== */

    val error = Color(0xFFFF0000)

    /* ========== BORDERS ========== */

    val border: Color
        @Composable get() = MaterialTheme.colorScheme.outline

    /* ========== SURFACES ========== */

    val surfaceSecondary: Color
        @Composable get() = MaterialTheme.colorScheme.surfaceVariant
}