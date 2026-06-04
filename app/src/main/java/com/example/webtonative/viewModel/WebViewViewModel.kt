package com.example.webtonative.viewModel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.example.webtonative.uiState.WebViewUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class WebViewViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(WebViewUiState())
    val uiState = _uiState.asStateFlow()

    var webViewState: Bundle? = null

    fun updateUrl(url: String) {
        _uiState.update {
            it.copy(
                currentUrl = url,
                lastOpenedUrl = url
            )
        }
    }

    fun updateProgress(progress: Int) {
        _uiState.update {
            it.copy(
                progress = progress,
                isLoading = progress < 100
            )
        }
    }

    fun showError(message: String) {
        _uiState.update {
            it.copy(errorMessage = message)
        }
    }

    fun clearError() {
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }

    fun clearUrl() {
        _uiState.update {
            it.copy(
                currentUrl = "",
                lastOpenedUrl = ""
            )
        }
    }
}