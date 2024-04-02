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
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.TypeConverters
import androidx.room.Update
import com.example.android.trackmysleepquality.enums.Season
import com.example.android.trackmysleepquality.enums.Type
import java.time.LocalDate
import java.util.Date

@Dao
interface AppDatabaseDao {
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
    @Query("SELECT * FROM clothes c " +
            "WHERE (c.season == :season OR :season is null) " +
            "AND (c.type == :type OR :type is null)")
    fun getClothesByFilters(season: Season?, type: Type?): List<Clothes>

    @Query("select * from `plan`")
    fun getAllPlansWithReceivers(): LiveData<List<PlanWithReceiver>>
    @Query("select * from `plan`")
    fun getAllPlansWithGifts(): LiveData<List<PlanWithGifts>>
    @Query("select * from `plan`")
    fun getAllPlans(): LiveData<List<PlanReceiverGifts>>
    @Insert
    fun insertPlan(plan: Plan): Long
    @Delete
    fun deletePlan(plan: Plan)
    @Update
    fun updatePlan(plan: Plan)
    @Query("select * from `plan` where date == :date")
    fun getPlansByDate(@TypeConverters(DateConverter::class) date: Date): List<PlanReceiverGifts>
    @Query("delete from `plan`")
    fun clearPlans()

    @Query("select * from receiver")
    fun getAllReceivers(): LiveData<List<Receiver>>
    @Insert
    fun insertReceiver(receiver: Receiver): Long
    @Delete
    fun deleteReceiver(receiver: Receiver)

    @Query("select * from gift")
    fun getAllGifts(): LiveData<List<Gift>>
    @Insert
    fun insertGift(gift: Gift): Long
    @Delete
    fun deleteGift(gift: Gift)
    @Update
    fun updateGift(gift: Gift)

    @Insert
    fun insertPlanReceiver(prg: PlanReceiver)
    @Insert
    fun insertPlanGift(prg: PlanGifts)

    /*@Query("delete from planreceiver where planId == :id")
    fun deleteRelationsPlanReceiver(id: Long)
    @Query("delete from plangifts where planId == :id")
    fun deleteRelationsPlanGifts(id: Long)*/
}
