package com.example.android.trackmysleepquality.clothesform

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.android.trackmysleepquality.database.Clothes
import com.example.android.trackmysleepquality.database.AppDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ClothesFormViewModel(
    private val dao: AppDatabaseDao,
    application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    var currentClothesItem = MutableLiveData<Clothes?>()
    fun getClothesById(id: Long){
        uiScope.launch {
            currentClothesItem.value = getById(id)
        }
    }
    private suspend fun getById(id: Long) : Clothes? {
        return withContext(Dispatchers.IO) {
            dao.getSingleClothes(id)
        }
    }

    fun updateCurrentClothes(clothes: Clothes){
        uiScope.launch {
            update(clothes)
        }
    }
    private suspend fun update(clothes: Clothes) {
        withContext(Dispatchers.IO) {
            dao.updateClothes(clothes)
            Log.i("sadsadsad", clothes.toString())
        }
    }

    fun insertNewClothes(clothes: Clothes){
        uiScope.launch {
            insert(clothes)
        }
    }
    private suspend fun insert(clothes: Clothes) {
        withContext(Dispatchers.IO) {
            dao.insertClothes(clothes)
        }
    }
}