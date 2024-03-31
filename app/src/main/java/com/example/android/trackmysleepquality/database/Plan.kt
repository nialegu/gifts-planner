package com.example.android.trackmysleepquality.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "plan")
data class Plan(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0L,

    @ColumnInfo(name = "date")
    var date: LocalDateTime,

    @ColumnInfo(name = "holiday")
    var holiday: String,

    @ColumnInfo(name = "receiver")
    var receiver: Receiver,

    @ColumnInfo(name = "gift")
    var gift: Gift
)