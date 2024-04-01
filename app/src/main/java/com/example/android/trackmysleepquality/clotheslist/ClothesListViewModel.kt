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

package com.example.android.trackmysleepquality.clotheslist

import android.app.Application
import android.icu.text.SimpleDateFormat
import android.text.Spanned
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.android.trackmysleepquality.database.Clothes
import com.example.android.trackmysleepquality.database.AppDatabaseDao
import com.example.android.trackmysleepquality.database.Gift
import com.example.android.trackmysleepquality.database.Plan
import com.example.android.trackmysleepquality.database.PlanReceiverGift
import com.example.android.trackmysleepquality.database.Receiver
import com.example.android.trackmysleepquality.enums.Season
import com.example.android.trackmysleepquality.enums.Type
import com.example.android.trackmysleepquality.formatClothesForOneItem
import kotlinx.coroutines.*
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

/**
 * ViewModel for SleepTrackerFragment.
 */
class ClothesListViewModel(
        private val dao: AppDatabaseDao,
        application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    val clothes = dao.getAllClothes()
    val plans = dao.getAllPlans()

    fun insertNewPlan(){
        val simpleDateFormat = SimpleDateFormat("ddMMyyyy")

        val plan: Plan = Plan(
            pId = 1L,
            holiday = "Holiday",
            date = simpleDateFormat.format(Date.from(Instant.now())).toString().toLong()
        )
        val receiver: Receiver = Receiver(
            rId = 2L,
            receiverName = "Leonid"
        )
        val gift: Gift = Gift(
            gId = 3L,
            giftName = "gift",
            price = 200.0
        )
        val prg: PlanReceiverGift = PlanReceiverGift(
            1L,
            2L,
            3L
        )
        uiScope.launch {
            insert(plan, receiver, gift, prg)
        }
    }
    private suspend fun insert(plan: Plan, receiver: Receiver, gift: Gift, prg: PlanReceiverGift, ) {
        withContext(Dispatchers.IO) {
            dao.insertPlanReceiverGift(prg)
            dao.insertPlan(plan)
            dao.insertReceiver(receiver)
            dao.insertGift(gift)
        }
    }

    val foundedAfterDateFilter = MutableLiveData<List<Plan>>()
    /*fun onDateFilter(date: LocalDateTime){
        uiScope.launch {
            foundedAfterDateFilter.value = getByDate(date)
        }
    }
    private suspend fun getByDate(date: LocalDateTime): List<Plan>{
        return withContext(Dispatchers.IO) {
            val foundedPlans = dao.getPlansByDate(date)
            foundedPlans
        }
    }*/

    val resources = application.resources

    val foundedAfterSearchClothes = MutableLiveData<List<Clothes>>()
    fun onSearch(text: String){
        uiScope.launch {
            foundedAfterSearchClothes.value = searchFromDb(text)
        }
    }
    private suspend fun searchFromDb(text: String): List<Clothes>{
        return withContext(Dispatchers.IO) {
            val foundedClothes = dao.getClothesByName(text)
            foundedClothes
        }
    }

    val foundedAfterFilterClothes = MutableLiveData<List<Clothes>>()
    fun onFilter(season: Season?, type: Type?){
        uiScope.launch {
            foundedAfterFilterClothes.value = filterFromDb(season, type)
        }
    }
    private suspend fun filterFromDb(season: Season?, type: Type?): List<Clothes>{
        return withContext(Dispatchers.IO) {
            val foundedClothes = dao.getClothesByFilters(season, type)
            foundedClothes
        }
    }

    fun getClothesStringsForOneItem(cl: Clothes): Spanned{
        return formatClothesForOneItem(cl, resources)
    }
   /* fun getPlansStringForOneItem(plan: PlanWithReceiver): Spanned{
        return formatPlansForOneItem(plan, resources)
    }*/

    val clearButtonVisible = clothes.map { clothes ->
        clothes.isNotEmpty()
    }

    fun onDelete(id: Long){
        uiScope.launch {
            deleteById(id)
        }
    }
    private suspend fun deleteById(id: Long){
        withContext(Dispatchers.IO){
            dao.deleteClothesById(id)
        }
    }

    fun onClear() {
        uiScope.launch {
            clear()
        }
    }
    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            dao.clearClothes()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
