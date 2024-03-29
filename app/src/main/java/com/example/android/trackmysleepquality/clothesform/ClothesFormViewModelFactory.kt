package com.example.android.trackmysleepquality.clothesform

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.trackmysleepquality.database.Clothes
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.sleeptracker.SleepTrackerViewModel

class ClothesFormViewModelFactory(
    private val dao: SleepDatabaseDao,
    private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClothesFormViewModel::class.java)) {
            return ClothesFormViewModel(dao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}