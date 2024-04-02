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

package com.example.android.trackmysleepquality.planslist

import android.app.AlertDialog
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.Clothes
import com.example.android.trackmysleepquality.database.AppDatabase
import com.example.android.trackmysleepquality.database.Gift
import com.example.android.trackmysleepquality.database.Plan
import com.example.android.trackmysleepquality.database.PlanReceiverGifts
import com.example.android.trackmysleepquality.database.Receiver
import com.example.android.trackmysleepquality.databinding.FragmentClothesListBinding
import com.example.android.trackmysleepquality.receiverslist.ReceiverListAdapter
import java.time.Instant
import java.util.Date

/**
 * A fragment with buttons to record start and end times for sleep, which are saved in
 * a database. Cumulative data is displayed in a simple scrollable TextView.
 * (Because we have not learned about RecyclerView yet.)
 */
class ClothesListFragment : Fragment(), PlanListAdapter.ItemClickListener {

    private lateinit var viewModel: ClothesListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentClothesListBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_clothes_list, container, false)

        val application = requireNotNull(this.activity).application
        val dao = AppDatabase.getInstance(application).getAppDatabaseDao()
        val viewModelFactory = ClothesListViewModelFactory(dao, application)
        viewModel = ViewModelProvider(this, viewModelFactory)
                .get(ClothesListViewModel::class.java)

        val adapter = PlanListAdapter(this, application.resources)
        binding.plansList.adapter = adapter

        binding.clearButton.setOnClickListener {
            viewModel.onClear()
        }
        binding.createButton.setOnClickListener {
            val simpleDateFormat = SimpleDateFormat("ddMMyyyy")
            simpleDateFormat.format(Date.from(Instant.now())).toString().toLong()

            val plan: Plan = Plan(
                holiday = "Holiday",
                date = simpleDateFormat.format(Date.from(Instant.now())).toString().toLong()
            )
            val receiver: Receiver = Receiver(
                receiverName = "Leonid"
            )
            val gift: Gift = Gift(
                giftName = "gift",
                price = 200.0
            )
            viewModel.insertNewPlan(plan, receiver, gift)


            //binding.searchField.text?.clear()
            //this.findNavController().navigate(ClothesListFragmentDirections.actionSleepTrackerFragmentToClothesFormFragment(""))
        }

        binding.toReceiversButton.setOnClickListener {
            findNavController().navigate(ClothesListFragmentDirections.actionSleepTrackerFragmentToReceiverListFragment())
        }

        binding.toGiftsButton.setOnClickListener {
            findNavController().navigate(ClothesListFragmentDirections.actionSleepTrackerFragmentToGiftsListFragment())
        }

        viewModel.plans.observe(viewLifecycleOwner, Observer { plans ->
            if (plans != null)
                adapter.data = plans
        })

        viewModel.clearButtonVisible.observe(viewLifecycleOwner, Observer { visible ->
            binding.clearButton.isEnabled = visible
        })
        
        return binding.root
    }

    override fun onItemClick(plan: PlanReceiverGifts, item: PlanListViewHolder) {
        Toast.makeText(context, plan.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onLongClick(plan: PlanReceiverGifts) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.delete)
        builder.setMessage(R.string.deletePlanAlert)
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes") { _, _ ->
            viewModel.deletePlan(plan)
        }
        builder.setNegativeButton("No") { _, _ ->}

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(true)

        alertDialog.show()
    }

    /*private fun getViews(clothes: List<Clothes>, binding: FragmentClothesListBinding, label: String){
        val resultList: MutableList<View> = mutableListOf()
        clothes.map {cl ->
            val linearLayout = LinearLayout(context)
            linearLayout.orientation = LinearLayout.HORIZONTAL

            val item = TextView(context)
            item.text = viewModel.getClothesStringsForOneItem(cl)

            val buttonLinearLayout = LinearLayout(context)
            buttonLinearLayout.orientation = LinearLayout.VERTICAL

            val updateButton = Button(context)
            updateButton.text = resources.getString(R.string.update)
            updateButton.setBackgroundColor(resources.getColor(R.color.green_color))
            buttonLinearLayout.addView(updateButton)
            updateButton.setOnClickListener {
                this.findNavController().navigate(ClothesListFragmentDirections.actionSleepTrackerFragmentToClothesFormFragment(cl.id.toString()))
            }

            val deleteButton = Button(context)
            deleteButton.text = resources.getString(R.string.delete)
            deleteButton.setBackgroundColor(resources.getColor(R.color.red_color))
            buttonLinearLayout.addView(deleteButton)
            deleteButton.setOnClickListener {
                //viewModel.onDelete(cl.id)
                //binding.filterBar.isVisible = true
                binding.dateField.text?.clear()

                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }

            linearLayout.addView(buttonLinearLayout)
            linearLayout.addView(item)

            resultList.plusAssign(linearLayout)
        }
        val textView = TextView(context)
        textView.text = label + "\n"
        //binding.clothesList.addView(textView)

        *//*resultList.map {
            binding.clothesList.addView(it)
        }*//*
    }*/
}