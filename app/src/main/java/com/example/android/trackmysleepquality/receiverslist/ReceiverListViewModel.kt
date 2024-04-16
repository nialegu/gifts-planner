package com.example.android.trackmysleepquality.receiverslist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.android.trackmysleepquality.database.AppDatabaseDao
import com.example.android.trackmysleepquality.database.Receiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReceiverListViewModel(
            private val dao: AppDatabaseDao,
            application: Application
        ) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    val receivers = dao.getAllReceivers()

    fun insertReceiver(name: String){
        uiScope.launch {
            insert(name)
        }
    }
    private suspend fun insert(name: String){
        withContext(Dispatchers.IO){
            val receiver = Receiver(receiverName = name)
            dao.insertReceiver(receiver)
        }
    }

    fun deleteReceive(receiver: Receiver){
        uiScope.launch {
            delete(receiver)
        }
    }
    private suspend fun delete(receiver: Receiver){
        withContext(Dispatchers.IO){
            val list = dao.getPlanReceiveByRecieveId(receiver.rId)
            list.map {
                dao.deletePlanById(it.planId)
            }
            dao.deleteReceiver(receiver)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}