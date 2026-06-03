package com.example.webtonative.browsingHistoryDB.roomDB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Query("""
        SELECT *
        FROM history
        ORDER BY last_visited_time DESC
    """)
    fun getAllHistory(): Flow<List<HistoryEntity>>

    @Query("""
        SELECT *
        FROM history
        WHERE url = :url
        LIMIT 1
    """)
    suspend fun getHistoryByUrl(
        url: String
    ): HistoryEntity?

    @Insert
    suspend fun insertHistory(
        history: HistoryEntity
    )

    @Update
    suspend fun updateHistory(
        history: HistoryEntity
    )

    @Delete
    suspend fun deleteHistory(
        history: HistoryEntity
    )

    @Query("DELETE FROM history")
    suspend fun clearHistory()
}