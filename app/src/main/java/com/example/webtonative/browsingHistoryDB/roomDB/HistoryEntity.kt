package com.example.webtonative.browsingHistoryDB.roomDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "url")
    val url: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "visit_count")
    val visitCount: Int,

    @ColumnInfo(name = "last_visited_time")
    val lastVisitedTime: Long
)