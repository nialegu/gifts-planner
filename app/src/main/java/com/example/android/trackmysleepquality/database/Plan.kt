package com.example.android.trackmysleepquality.database

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.TypeConverters

@Entity(tableName = "plan")
data class Plan(
    @PrimaryKey(autoGenerate = true)
    var pId: Long = 0L,
    @ColumnInfo(name = "holiday")
    var holiday: String,

    @TypeConverters(DateConverter::class)
    @ColumnInfo(name = "date")
    var date: Long,
)


