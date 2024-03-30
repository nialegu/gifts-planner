/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.android.trackmysleepquality.enums.Season
import com.example.android.trackmysleepquality.enums.Type

@Dao
interface SleepDatabaseDao {

    @Insert
    fun insert(night: SleepNight)

    @Update
    fun update(night: SleepNight)

    @Query("SELECT * FROM sleep_quality_table WHERE id = :key")
    fun get(key: Long): SleepNight?

    @Query("DELETE FROM sleep_quality_table")
    fun clear()

    @Query("SELECT * FROM sleep_quality_table ORDER BY id DESC")
    fun getAllNights(): LiveData<List<SleepNight>>

    @Query("SELECT * FROM sleep_quality_table ORDER BY id DESC LIMIT 1")
    fun getTonight(): SleepNight?

    @Insert
    fun insertClothes(clothes: Clothes)

    @Update
    fun updateClothes(clothes: Clothes)

    @Query("SELECT * FROM clothes WHERE id = :key")
    fun getSingleClothes(key: Long): Clothes?

    @Query("DELETE FROM clothes")
    fun clearClothes()

    @Query("DELETE FROM clothes WHERE id = :id")
    fun deleteClothesById(id: Long)

    @Query("SELECT * FROM clothes ORDER BY id DESC")
    fun getAllClothes(): LiveData<List<Clothes>>

    @Query("SELECT * FROM clothes ORDER BY id DESC")
    fun getAllClothesFuture(): List<Clothes>

    @Query("SELECT * FROM clothes c " +
            "WHERE c.name LIKE :pattern " +
            "OR c.description LIKE :pattern " +
            "ORDER BY id DESC")
    fun getClothesByName(pattern: String): List<Clothes>

    @Query("SELECT * FROM clothes c WHERE c.season == :season AND c.type == :type")
    fun getClothesByFilters(season: Season?, type: Type?): List<Clothes>
}
