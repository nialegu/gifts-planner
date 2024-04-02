package com.example.android.trackmysleepquality.giftslist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.android.trackmysleepquality.database.AppDatabaseDao
import com.example.android.trackmysleepquality.database.Gift
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GiftListViewModel(
            private val dao: AppDatabaseDao,
            application: Application
        ) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val gifts = dao.getAllGifts()

    fun insertGift(giftName: String, price: Double){
        uiScope.launch {
            insert(giftName, price)
        }
    }
    private suspend fun insert(giftName: String, price: Double){
        withContext(Dispatchers.IO){
            val gift = Gift(giftName = giftName, price = price)
            dao.insertGift(gift)
        }
    }

    fun deleteGift(gift: Gift){
        uiScope.launch {
            delete(gift)
        }
    }
    private suspend fun delete(gift: Gift){
        withContext(Dispatchers.IO){
            dao.deleteGift(gift)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}