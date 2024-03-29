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

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.databinding.FragmentSleepTrackerBinding
import java.util.logging.Logger

/**
 * A fragment with buttons to record start and end times for sleep, which are saved in
 * a database. Cumulative data is displayed in a simple scrollable TextView.
 * (Because we have not learned about RecyclerView yet.)
 */
class SleepTrackerFragment : Fragment() {

    private lateinit var viewModel: SleepTrackerViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentSleepTrackerBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sleep_tracker, container, false)

        val application = requireNotNull(this.activity).application
        val dao = SleepDatabase.getInstance(application).getSleepDatabaseDao()
        val viewModelFactory = SleepTrackerViewModelFactory(dao, application)
        viewModel = ViewModelProvider(this, viewModelFactory)
                .get(SleepTrackerViewModel::class.java)

        /* binding.startButton.setOnClickListener {
            viewModel.onStartTracking()
        }
        binding.stopButton.setOnClickListener {
            viewModel.onStopTracking()
        }*/
        binding.clearButton.setOnClickListener {
            viewModel.onClear()
        }
        binding.createButton.setOnClickListener {
            this.findNavController().navigate(SleepTrackerFragmentDirections.actionSleepTrackerFragmentToClothesFormFragment())
        }

        binding.searchButton.setOnClickListener {
            val searchText = binding.searchField.text.toString()
            viewModel.searchClothes(searchText)
        }

        /*viewModel.nightsString.observe(viewLifecycleOwner, Observer { nightsString ->
            binding.textview.text = nightsString
        })*/
        viewModel.clothesString.observe(viewLifecycleOwner, Observer { clothesString ->
            binding.textview.text = clothesString
        })

        // !!!!!!!!!!!!!!!!!!!
        /*viewModel.clothes.observe(viewLifecycleOwner, Observer { clothes ->
            val resultList: MutableList<TextView> = mutableListOf()
            clothes.map {
                val textView = TextView(context)
                textView.text = it.description
                textView.setTextColor(Color.RED)
                resultList.plusAssign(textView)
            }
            resultList.map {
                binding.clothesList.addView(it)
            }
        })*/

        /* viewModel.navigateToSleepQuality.observe(viewLifecycleOwner, Observer { night ->
             if (night != null) {
                 this.findNavController().navigate(
                         SleepTrackerFragmentDirections
                                 .actionSleepTrackerFragmentToSleepQualityFragment(night.nightId))
                 viewModel.doneNavigating()
             }
         })
         viewModel.startButtonVisible.observe(viewLifecycleOwner, Observer { visible ->
             binding.startButton.isEnabled = visible
         })
         viewModel.stopButtonVisible.observe(viewLifecycleOwner, Observer { visible ->
             binding.stopButton.isEnabled = visible
         })*/
        viewModel.clearButtonVisible.observe(viewLifecycleOwner, Observer { visible ->
            binding.clearButton.isEnabled = visible
        })

        return binding.root
    }
}
