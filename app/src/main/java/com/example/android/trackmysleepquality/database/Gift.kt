package com.example.android.trackmysleepquality.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity("gift")
data class Gift(
    @PrimaryKey(autoGenerate = true)
    var gId: Long = 0L,

    @ColumnInfo(name = "name")
    var giftName: String,

    @ColumnInfo(name = "price")
    var price: Double,
)
