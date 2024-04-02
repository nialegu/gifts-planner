package com.example.android.trackmysleepquality.giftslist

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.trackmysleepquality.database.AppDatabaseDao

class GiftListViewModelFactory(
            private val dao: AppDatabaseDao,
            private val application: Application
        ) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GiftListViewModel::class.java)) {
            return GiftListViewModel(dao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}