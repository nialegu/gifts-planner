package com.example.android.trackmysleepquality.database

import androidx.room.TypeConverter
import java.time.LocalDate
import java.util.Date

class DateConverter {
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.date?.toLong()
    }
}