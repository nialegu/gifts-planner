package com.example.android.trackmysleepquality.clothesform

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.databinding.FragmentClothesFormBinding
import com.example.android.trackmysleepquality.databinding.FragmentSleepTrackerBinding
import com.example.android.trackmysleepquality.sleeptracker.SleepTrackerViewModel
import com.example.android.trackmysleepquality.sleeptracker.SleepTrackerViewModelFactory

class ClothesFormFragment : Fragment() {

    private lateinit var viewModel: ClothesFormViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentClothesFormBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_clothes_form, container, false)//

        val application = requireNotNull(this.activity).application
        val dao = SleepDatabase.getInstance(application).getSleepDatabaseDao()
        val viewModelFactory = ClothesFormViewModelFactory(dao, application)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(ClothesFormViewModel::class.java)

        return binding.root
    }
}