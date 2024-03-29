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
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.Clothes
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.databinding.FragmentSleepTrackerBinding

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

        binding.clearButton.setOnClickListener {
            viewModel.onClear()
        }
        binding.createButton.setOnClickListener {
            binding.searchField.text?.clear()
            this.findNavController().navigate(SleepTrackerFragmentDirections.actionSleepTrackerFragmentToClothesFormFragment())
        }

        binding.searchButton.setOnClickListener {
            val searchText = binding.searchField.text.toString()

            binding.searchClothesList.removeAllViews()
            if (searchText == "") {
                binding.clothesList.isVisible = true
            }
            else{
                viewModel.onSearch(searchText)
                binding.clothesList.isVisible = false
            }
        }

        viewModel.foundedAfterSearchClothes.observe(viewLifecycleOwner, Observer { clothes ->
            if (clothes.isEmpty()){
                val nothingText = TextView(context)
                nothingText.text = resources.getString(R.string.nothingWasFound)
                binding.searchClothesList.addView(nothingText)
            }
            else {
                getViews(clothes, binding.searchClothesList)
            }
        })

        viewModel.clothes.observe(viewLifecycleOwner, Observer { clothes ->
            getViews(clothes, binding.clothesList)
        })

        viewModel.clearButtonVisible.observe(viewLifecycleOwner, Observer { visible ->
            binding.clearButton.isEnabled = visible
        })

        return binding.root
    }

    private fun getViews(clothes: List<Clothes>, layout: LinearLayout){
        val resultList: MutableList<View> = mutableListOf()
        clothes.map {cl ->
            val linearLayout = LinearLayout(context)
            linearLayout.orientation = LinearLayout.HORIZONTAL

            val item = TextView(context)
            item.text = viewModel.getStringsForOneItem(cl)
            linearLayout.addView(item)

            val button = Button(context)
            button.text = resources.getString(R.string.delete)
            button.setOnClickListener {
                viewModel.onDelete(cl.id)
            }
            linearLayout.addView(button)

            resultList.plusAssign(linearLayout)
        }
        //layout.removeAllViews()
        resultList.map {
            layout.addView(it)
        }
    }
}
