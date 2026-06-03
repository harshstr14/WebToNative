package com.example.webtonative.uiState

data class WebViewUiState(
    val currentUrl: String = "",
    val lastOpenedUrl: String = "",
    val progress: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)