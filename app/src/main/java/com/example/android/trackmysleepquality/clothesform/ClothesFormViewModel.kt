package com.example.android.trackmysleepquality.clothesform

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.android.trackmysleepquality.database.SleepDatabaseDao

class ClothesFormViewModel(
    private val dao: SleepDatabaseDao,
    application: Application) : AndroidViewModel(application) {

}