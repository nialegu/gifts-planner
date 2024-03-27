package com.example.android.trackmysleepquality.database

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

interface ClothesDatabaseDao {

    @Insert
    fun insertClothes(night: Clothes)

    @Update
    fun updateClothes(night: Clothes)

    @Query("SELECT * FROM clothes WHERE id = :key")
    fun getSingleClothes(key: Long): Clothes?

    @Query("DELETE FROM clothes")
    fun clearClothes()

    @Query("SELECT * FROM clothes ORDER BY id DESC")
    fun getAllClothes(): LiveData<List<Clothes>>

    @Query("SELECT * FROM clothes c " +
            "WHERE c.name LIKE :pattern " +
            "OR c.description LIKE :pattern " +
            "ORDER BY id DESC LIMIT 1")
    fun getTonight(pattern: String): Clothes?
}