package com.example.android.trackmysleepquality.planform

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.AppDatabase
import com.example.android.trackmysleepquality.database.Gift
import com.example.android.trackmysleepquality.database.Plan
import com.example.android.trackmysleepquality.database.Receiver
import com.example.android.trackmysleepquality.databinding.FragmentPlanFormBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import java.time.Instant
import java.util.Date

class PlanFormFragment : Fragment() {

    private lateinit var viewModel: PlanFormViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentPlanFormBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_plan_form, container, false)//

        val application = requireNotNull(this.activity).application
        val dao = AppDatabase.getInstance(application).getAppDatabaseDao()
        val viewModelFactory = PlanFormViewModelFactory(dao, application)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(PlanFormViewModel::class.java)

        val planId = arguments?.get("planId").toString().toLongOrNull()
        if (planId != null) {
            viewModel.getPlanById(planId)
        }

        val listForSpinner = mutableListOf<Receiver>()
        val listForSpinnerString = mutableListOf<String>()
        viewModel.receivers.observe(viewLifecycleOwner, Observer {receivers ->
            receivers.map {
                listForSpinner.plusAssign(it)
                listForSpinnerString.plusAssign(it.receiverName)
            }
            val arrayAdapter = ArrayAdapter(this.requireContext(),
                android.R.layout.simple_spinner_item, listForSpinnerString)
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.receiveSpinner.setAdapter(arrayAdapter)
        })

        val listForChipGroup = mutableListOf<Gift>()
        val listForChipGroupChips = mutableListOf<Chip>()
        viewModel.gifts.observe(viewLifecycleOwner, Observer { gifts ->
            gifts.map {
                val chip = Chip(context)
                chip.id = it.gId.toInt()
                chip.text = it.giftName
                chip.isClickable = true
                chip.isCheckable = true

                listForChipGroupChips.plusAssign(chip)
                listForChipGroup.plusAssign(it)
            }
            listForChipGroupChips.map {
                binding.giftsChips.addView(it)
            }
        })

        var holiday: String = ""
        var date: Date = Date.from(Instant.now())
        var receiver: Receiver
        val gifts = mutableListOf<Gift>()

        binding.addButton.setOnClickListener {
            receiver = listForSpinner.first { it.receiverName.equals(binding.receiveSpinner.selectedItem.toString()) }
            listForChipGroup.filter { binding.giftsChips.checkedChipIds.contains(it.gId.toInt())}.map {
                gifts.plusAssign(it)
            }
            holiday = binding.holidayField.editText?.text.toString()

            val plan = Plan(
                holiday = "asdasd",
                date = date.time
            )


            Log.i("asdsafsfa", plan.toString()+receiver.toString()+gifts.toString())
            viewModel.insertNewPlan(plan, receiver, gifts)
            this.findNavController().navigate(R.id.action_clothesFormFragment_to_sleepTrackerFragment)
        }

        viewModel.currentPlan.observe(viewLifecycleOwner, Observer {pl ->
            /*binding.nameField.editText?.setText(cl?.name, TextView.BufferType.EDITABLE)
            binding.descriptionField.editText?.setText(cl?.description, TextView.BufferType.EDITABLE)

            val seasonShouldCheck = seasonRadioButtons.filter {
                it.text.toString() == cl?.season.toString()
            }.get(0).id
            binding.season.check(seasonShouldCheck)

            val typeShouldCheck = typeRadioButtons.filter {
                it.text.toString() == cl?.type.toString()
            }.get(0).id
            binding.type.check(typeShouldCheck)

            if (cl?.clothesSize != null) {
                binding.clothesSize.isVisible = true
                val clothesSizeShouldCheck = clothesSizeRadioButtons.filter {
                    it.text.toString() == cl.clothesSize.toString()
                }.get(0).id
                binding.clothesSize.check(clothesSizeShouldCheck)
            }
            else if (cl?.shoesSize != null) {
                binding.shoesSizeField.isVisible = true
                binding.shoesSizeField.editText?.setText(cl.shoesSize.toString(), TextView.BufferType.EDITABLE)
            }

            binding.addButton.setOnClickListener(null)
            binding.addButton.text = resources.getString(R.string.update)
            binding.addButton.setOnClickListener{
                name = binding.nameField.editText?.text.toString()
                description = binding.descriptionField.editText?.text.toString()
                if (binding.shoesSizeField.editText?.text.toString() != "") shoesSize = Integer.parseInt(binding.shoesSizeField.editText?.text.toString())

                cl?.name = name
                cl?.season = season
                cl?.type = type
                cl?.description = description
                cl?.clothesSize = clothesSize
                cl?.shoesSize = shoesSize
                if (cl != null) {
                    viewModel.updateCurrentClothes(cl)
                }*//*
                this.findNavController().navigate(R.id.action_clothesFormFragment_to_sleepTrackerFragment)
            }*/
        })

        return binding.root
    }
}
