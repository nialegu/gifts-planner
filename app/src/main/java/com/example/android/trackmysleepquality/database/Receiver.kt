package com.example.android.trackmysleepquality.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("receiver")
data class Receiver(
    @PrimaryKey(autoGenerate = true)
    var rId: Long = 0L,

    @ColumnInfo(name = "name")
    var receiverName: String,
)
