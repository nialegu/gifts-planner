package com.example.android.trackmysleepquality.clothesform

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
import com.example.android.trackmysleepquality.databinding.FragmentPlanFormBinding

class ClothesFormFragment : Fragment() {

    private lateinit var viewModel: ClothesFormViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentPlanFormBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_plan_form, container, false)//

        val application = requireNotNull(this.activity).application
        val dao = AppDatabase.getInstance(application).getAppDatabaseDao()
        val viewModelFactory = ClothesFormViewModelFactory(dao, application)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(ClothesFormViewModel::class.java)

        val planId = arguments?.get("planId").toString().toLongOrNull()
        if (planId != null) {
            viewModel.getPlanById(planId)
        }

        val listForSpinner = mutableListOf<String>()
        viewModel.receivers.observe(viewLifecycleOwner, Observer {receivers ->
            receivers.map {
                listForSpinner.plusAssign(it.receiverName)
            }
            Log.i("asdsadasd", listForSpinner.size.toString())
            val arrayAdapter = ArrayAdapter(this.requireContext(),
                android.R.layout.simple_spinner_item, listForSpinner)
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.receiveSpinner.setAdapter(arrayAdapter)
        })


        binding.addButton.setOnClickListener {
            binding.receiveSpinner.selectedItem
            /*name = binding.nameField.editText?.text.toString()
            description = binding.descriptionField.editText?.text.toString()
            if (binding.shoesSizeField.editText?.text.toString() != "") shoesSize = Integer.parseInt(binding.shoesSizeField.editText?.text.toString())
            val newClothes: Clothes = Clothes(
                name = name,
                season = season,
                type = type,
                description = description,
                clothesSize = clothesSize,
                shoesSize = shoesSize
            )
            viewModel.insertNewClothes(newClothes)*/
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
