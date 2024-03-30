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

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
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
import com.example.android.trackmysleepquality.enums.Season
import com.example.android.trackmysleepquality.enums.Type

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

        var seasonResult: Season? = null
        var typeResult: Type? = null

        val seasonOnCheckedChangeListener = RadioGroup.OnCheckedChangeListener { group, checkedId ->
            seasonResult = Season.valueOf(group.findViewById<RadioButton>(checkedId).text.toString())
        }
        val typeOnCheckedChangeListener = RadioGroup.OnCheckedChangeListener { group, checkedId ->
            typeResult = Type.valueOf(group.findViewById<RadioButton>(checkedId).text.toString())
        }
        binding.season.setOnCheckedChangeListener(seasonOnCheckedChangeListener)
        binding.type.setOnCheckedChangeListener(typeOnCheckedChangeListener)

        val seasonRadioButtons: MutableList<RadioButton> = mutableListOf()
        Season.entries.map {
            val rb = RadioButton(context)
            rb.text = it.toString()
            seasonRadioButtons.plusAssign(rb)
        }
        seasonRadioButtons.map {
            binding.season.addView(it)
        }

        val typeRadioButtons: MutableList<RadioButton> = mutableListOf()
        Type.entries.map {
            val rb = RadioButton(context)
            rb.text = it.toString()
            typeRadioButtons.plusAssign(rb)
        }
        typeRadioButtons.map {
            binding.type.addView(it)
        }

        binding.clearButton.setOnClickListener {
            binding.clothesList.removeAllViews()

            val textView = TextView(context)
            textView.text = resources.getString(R.string.hereIsYourClothes)
            binding.clothesList.addView(textView)

            viewModel.onClear()
        }
        binding.createButton.setOnClickListener {
            binding.searchField.text?.clear()
            this.findNavController().navigate(SleepTrackerFragmentDirections.actionSleepTrackerFragmentToClothesFormFragment())
        }


        var clothesList: List<Clothes> = emptyList()
        binding.searchButton.setOnClickListener {
            val searchText = binding.searchField.text.toString()

            if (searchText == "") {
                binding.clothesList.removeAllViews()
                getViews(clothesList, binding, resources.getString(R.string.hereIsYourClothes))

                binding.filterBar.isVisible = true
            }
            else{
                viewModel.onSearch(searchText)
                binding.clothesList.removeAllViews()

                binding.filterBar.isVisible = false
                binding.season.setOnCheckedChangeListener(null)
                binding.type.setOnCheckedChangeListener(null)
                binding.season.clearCheck()
                binding.type.clearCheck()
                binding.season.setOnCheckedChangeListener(seasonOnCheckedChangeListener)
                binding.type.setOnCheckedChangeListener(typeOnCheckedChangeListener)
            }
        }
        viewModel.foundedAfterSearchClothes.observe(viewLifecycleOwner, Observer { clothes ->
            if (clothes.isEmpty()){
                val nothingText = TextView(context)
                nothingText.text = resources.getString(R.string.nothingWasFound)
                binding.clothesList.addView(nothingText)
            }
            else {
                getViews(clothes, binding, resources.getString(R.string.foundedItems))
            }
        })

        binding.filterButton.setOnClickListener {
            binding.clothesList.removeAllViews()
            viewModel.onFilter(seasonResult, typeResult)
        }
        viewModel.foundedAfterFilterClothes.observe(viewLifecycleOwner, Observer { clothes ->
            if (clothes.isEmpty()){
                val nothingText = TextView(context)
                nothingText.text = resources.getString(R.string.nothingWasFound)
                binding.clothesList.addView(nothingText)
            }
            else {
                getViews(clothes, binding, resources.getString(R.string.foundedItems))
            }
        })
        binding.clearFilterButton.setOnClickListener {
            binding.season.setOnCheckedChangeListener(null)
            binding.type.setOnCheckedChangeListener(null)
            binding.season.clearCheck()
            binding.type.clearCheck()
            binding.season.setOnCheckedChangeListener(seasonOnCheckedChangeListener)
            binding.type.setOnCheckedChangeListener(typeOnCheckedChangeListener)

            binding.clothesList.removeAllViews()
            getViews(clothesList, binding, resources.getString(R.string.hereIsYourClothes))
        }

        viewModel.clothes.observe(viewLifecycleOwner, Observer { clothes ->
            clothesList = clothes
            binding.clothesList.removeAllViews()
            getViews(clothes, binding, resources.getString(R.string.hereIsYourClothes))
        })

        viewModel.clearButtonVisible.observe(viewLifecycleOwner, Observer { visible ->
            binding.clearButton.isEnabled = visible
        })
        
        return binding.root
    }

    private fun getViews(clothes: List<Clothes>, binding: FragmentSleepTrackerBinding, label: String){
        val resultList: MutableList<View> = mutableListOf()
        clothes.map {cl ->
            val linearLayout = LinearLayout(context)
            linearLayout.orientation = LinearLayout.HORIZONTAL

            val item = TextView(context)
            item.text = viewModel.getStringsForOneItem(cl)
            linearLayout.addView(item)

            val deleteButton = Button(context)
            deleteButton.text = resources.getString(R.string.delete)
            linearLayout.addView(deleteButton)
            deleteButton.setOnClickListener {
                viewModel.onDelete(cl.id)
                binding.filterBar.isVisible = true
                binding.searchField.text?.clear()

                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }

            val updateButton = Button(context)
            updateButton.text = resources.getString(R.string.update)
            linearLayout.addView(updateButton)
            updateButton.setOnClickListener {
                this.findNavController().navigate(SleepTrackerFragmentDirections.actionSleepTrackerFragmentToClothesFormFragment())
            }

            resultList.plusAssign(linearLayout)
        }
        val textView = TextView(context)
        textView.text = label
        binding.clothesList.addView(textView)

        resultList.map {
            binding.clothesList.addView(it)
        }
    }
}
