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
import java.time.LocalDate
import java.util.Date

@Dao
interface AppDatabaseDao {
    @Query("select * from `plan`")
    fun getAllPlansWithReceivers(): LiveData<List<PlanWithReceiver>>
    @Query("select * from `plan` where pId == :id")
    fun getPlanById(id: Long) : PlanReceiverGifts? ///////////////////
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
    fun insertPlanReceiver(pr: PlanReceiver)
    @Insert
    fun insertPlanGift(pg: PlanGifts)

    @Update
    fun updatePlanReceiver(pr: PlanReceiver)
    @Update
    fun updatePlanGifts(pr: PlanGifts)
    @Query("select * from planreceiver where receiverId == :id")
    fun getPlanReceiveByRecieveId(id: Long): List<PlanReceiver>

    @Query("delete from `plan` where pId == :id")
    fun deletePlanById(id: Long)

    /*@Query("delete from planreceiver where planId == :id")
    fun deleteRelationsPlanReceiver(id: Long)
    @Query("delete from plangifts where planId == :id")
    fun deleteRelationsPlanGifts(id: Long)*/
}
