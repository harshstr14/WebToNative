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
import java.util.Calendar
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
        val today = mutableListOf<HistoryUiItem>()
        val yesterday = mutableListOf<HistoryUiItem>()
        val earlier = mutableListOf<HistoryUiItem>()

        list.sortedByDescending { it.lastVisitedTime }
            .forEach { item ->
                when {
                    isToday(item.lastVisitedTime) ->
                        today.add(HistoryUiItem.Item(item))

                    isYesterday(item.lastVisitedTime) ->
                        yesterday.add(HistoryUiItem.Item(item))

                    else ->
                        earlier.add(HistoryUiItem.Item(item))
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

    fun isToday(timestamp: Long): Boolean {
        val today = Calendar.getInstance()

        val itemDate = Calendar.getInstance().apply {
            timeInMillis = timestamp
        }

        return today.get(Calendar.YEAR) == itemDate.get(Calendar.YEAR) &&
                today.get(Calendar.DAY_OF_YEAR) == itemDate.get(Calendar.DAY_OF_YEAR)
    }

    fun isYesterday(timestamp: Long): Boolean {
        val yesterday = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -1)
        }

        val itemDate = Calendar.getInstance().apply {
            timeInMillis = timestamp
        }

        return yesterday.get(Calendar.YEAR) == itemDate.get(Calendar.YEAR) &&
                yesterday.get(Calendar.DAY_OF_YEAR) == itemDate.get(Calendar.DAY_OF_YEAR)
    }
}