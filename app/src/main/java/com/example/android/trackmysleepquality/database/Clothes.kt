package com.example.android.trackmysleepquality.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.trackmysleepquality.enums.Season
import com.example.android.trackmysleepquality.enums.Type
import com.example.android.trackmysleepquality.enums.ClothesSize

@Entity(tableName = "clothes")
data class Clothes (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0L,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "season")
    var season: Season,

    @ColumnInfo(name = "type")
    var type: Type,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "clothesSize")
    var clothesSize: ClothesSize?,

    @ColumnInfo(name = "shoesSize")
    var shoesSize: Int?
)
