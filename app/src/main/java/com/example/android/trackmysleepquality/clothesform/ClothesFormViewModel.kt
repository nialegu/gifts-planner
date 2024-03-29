package com.example.android.trackmysleepquality.clothesform

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.android.trackmysleepquality.database.Clothes
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ClothesFormViewModel(
    private val dao: SleepDatabaseDao,
    application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)


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