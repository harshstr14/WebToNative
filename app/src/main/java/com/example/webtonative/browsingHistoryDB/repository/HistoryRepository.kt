package com.example.webtonative.browsingHistoryDB.repository

import com.example.webtonative.browsingHistoryDB.roomDB.HistoryDao
import com.example.webtonative.browsingHistoryDB.roomDB.HistoryEntity
import javax.inject.Inject

class HistoryRepository @Inject constructor(
    private val dao: HistoryDao
) {
    suspend fun saveVisit(
        url: String,
        title: String
    ) {
        val existing = dao.getHistoryByUrl(url)

        if (existing == null) {
            dao.insertHistory(
                HistoryEntity(
                    url = url,
                    title = title,
                    visitCount = 1,
                    lastVisitedTime = System.currentTimeMillis()
                )
            )
        } else {
            dao.updateHistory(
                existing.copy(
                    title = title,
                    visitCount = existing.visitCount + 1,
                    lastVisitedTime = System.currentTimeMillis()
                )
            )
        }
    }

    fun getHistory() = dao.getAllHistory()

    suspend fun deleteHistory(history: HistoryEntity) {
        dao.deleteHistory(history)
    }

    suspend fun deleteAllHistory() {
        dao.clearHistory()
    }
}