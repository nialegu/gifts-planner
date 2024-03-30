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

package com.example.android.trackmysleepquality.sleeptracker

import android.app.Application
import android.text.Spanned
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.android.trackmysleepquality.database.Clothes
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.enums.Season
import com.example.android.trackmysleepquality.enums.Type
import com.example.android.trackmysleepquality.formatClothes
import com.example.android.trackmysleepquality.formatClothesForOneItem
import com.example.android.trackmysleepquality.formatNights
import kotlinx.coroutines.*

/**
 * ViewModel for SleepTrackerFragment.
 */
class SleepTrackerViewModel(
        private val dao: SleepDatabaseDao,
        application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    private var tonight = MutableLiveData<SleepNight?>()

    val clothes = dao.getAllClothes()
    val clothesItemsForView = clothes.map {
        it.toList()
    }

    private val _navigateToSleepQuality = MutableLiveData<SleepNight?>()

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

    fun getStringsForOneItem(cl: Clothes): Spanned{
        return formatClothesForOneItem(cl, resources)
    }

    val clearButtonVisible = clothes.map { clothes ->
        clothes.isNotEmpty()
    }

    init {
        initializeTonight()
    }

    private fun initializeTonight() {
        uiScope.launch {
            tonight.value = getTonightFromDatabase()
        }
    }

    fun doneNavigating() {
        _navigateToSleepQuality.value = null
    }

    private suspend fun getTonightFromDatabase(): SleepNight? {
        return withContext(Dispatchers.IO) {
            var night = dao.getTonight()
            if (night?.endTimeMillis != night?.startTimeMillis) {
                night = null
            }
            night
        }
    }

    fun onStartTracking() {
        uiScope.launch {
            val newNight = SleepNight()
            insert(newNight)
            tonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun insert(night: SleepNight) {
        withContext(Dispatchers.IO) {
            dao.insert(night)
        }
    }

    fun onStopTracking() {
        uiScope.launch {
            val oldNight = tonight.value ?: return@launch
            oldNight.endTimeMillis = System.currentTimeMillis()
            update(oldNight)
            _navigateToSleepQuality.value = oldNight
        }
    }

    private suspend fun update(night: SleepNight) {
        withContext(Dispatchers.IO) {
            dao.update(night)
        }
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
            tonight.value = null
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
