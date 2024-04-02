package com.example.android.trackmysleepquality.receiverslist

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.trackmysleepquality.database.AppDatabaseDao

class ReceiverListViewModelFactory(
            private val dao: AppDatabaseDao,
            private val application: Application
        ) : ViewModelProvider.Factory  {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReceiverListViewModel::class.java)) {
            return ReceiverListViewModel(dao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}