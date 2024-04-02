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

package com.example.android.trackmysleepquality.planslist

import android.app.Application
import android.text.Spanned
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.android.trackmysleepquality.database.Clothes
import com.example.android.trackmysleepquality.database.AppDatabaseDao
import com.example.android.trackmysleepquality.database.Gift
import com.example.android.trackmysleepquality.database.Plan
import com.example.android.trackmysleepquality.database.PlanGifts
import com.example.android.trackmysleepquality.database.PlanReceiver
import com.example.android.trackmysleepquality.database.PlanReceiverGifts
import com.example.android.trackmysleepquality.database.Receiver
import kotlinx.coroutines.*
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

    val foundedAfterDateFilter = MutableLiveData<List<PlanReceiverGifts>>()
    fun onDateFilter(date: Date){
        uiScope.launch {
            foundedAfterDateFilter.value = getByDate(date)
        }
    }
    private suspend fun getByDate(date: Date): List<PlanReceiverGifts>{
        return withContext(Dispatchers.IO) {
            val foundedPlans = dao.getPlansByDate(date)
            foundedPlans
        }
    }

    val clearButtonVisible = plans.map { plans ->
        plans.isNotEmpty()
    }

    fun deletePlan(plan: PlanReceiverGifts){
        uiScope.launch {
            delete(plan)
        }
    }
    private suspend fun delete(plan: PlanReceiverGifts){
        withContext(Dispatchers.IO){
            //dao.deleteRelationsPlanReceiver(plan.plan.pId)
            //dao.deleteRelationsPlanGifts(plan.plan.pId)
            dao.deletePlan(plan.plan)
        }
    }

    fun onClear() {
        uiScope.launch {
            clear()
        }
    }
    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            dao.clearPlans()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
