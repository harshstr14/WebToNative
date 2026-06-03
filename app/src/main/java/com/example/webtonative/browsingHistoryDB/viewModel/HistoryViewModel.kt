package com.example.webtonative.browsingHistoryDB.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.webtonative.browsingHistoryDB.repository.HistoryRepository
import com.example.webtonative.browsingHistoryDB.roomDB.HistoryEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class HistoryUiItem {
    data class Header(val title: String) : HistoryUiItem()
    data class Item(val data: HistoryEntity) : HistoryUiItem()
}

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: HistoryRepository
) : ViewModel() {

    val history = repository.getHistory()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun saveVisit(
        url: String,
        title: String
    ) {
        viewModelScope.launch {
            repository.saveVisit(url, title)
        }
    }

    val groupedHistory = history.map { list ->
        val now = System.currentTimeMillis()

        val today = mutableListOf<HistoryUiItem>()
        val yesterday = mutableListOf<HistoryUiItem>()
        val earlier = mutableListOf<HistoryUiItem>()

        list.sortedByDescending { it.lastVisitedTime }.forEach { item ->

            val diff = now - item.lastVisitedTime
            val oneDay = 24 * 60 * 60 * 1000L

            when {
                diff < oneDay -> today.add(HistoryUiItem.Item(item))
                diff < oneDay * 2 -> yesterday.add(HistoryUiItem.Item(item))
                else -> earlier.add(HistoryUiItem.Item(item))
            }
        }

        buildList {
            if (today.isNotEmpty()) {
                add(HistoryUiItem.Header("Today"))
                addAll(today)
            }

            if (yesterday.isNotEmpty()) {
                add(HistoryUiItem.Header("Yesterday"))
                addAll(yesterday)
            }

            if (earlier.isNotEmpty()) {
                add(HistoryUiItem.Header("Earlier"))
                addAll(earlier)
            }
        }
    }

    fun deleteHistory(history: HistoryEntity) {
        viewModelScope.launch {
            repository.deleteHistory(history)
        }
    }

    fun deleteAllHistory() {
        viewModelScope.launch {
            repository.deleteAllHistory()
        }
    }
}