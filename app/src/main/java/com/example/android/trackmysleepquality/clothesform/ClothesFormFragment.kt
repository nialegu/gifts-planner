package com.example.android.trackmysleepquality.clothesform

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
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
import com.example.android.trackmysleepquality.databinding.FragmentClothesFormBinding
import com.example.android.trackmysleepquality.enums.ClothesSize
import com.example.android.trackmysleepquality.enums.Season
import com.example.android.trackmysleepquality.enums.Type

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

        val clothesId = arguments?.get("clothesId").toString().toLongOrNull()
        if (clothesId != null) {
            viewModel.getClothesById(clothesId)
        }

        val clothesSizeRadioButtons: MutableList<RadioButton> = emptyList<RadioButton>().toMutableList()
        ClothesSize.entries.map {
            val rb = RadioButton(context)
            rb.text = it.toString()
            clothesSizeRadioButtons += rb
        }
        clothesSizeRadioButtons.map {
            binding.clothesSize.addView(it)
        }

        val seasonRadioButtons: MutableList<RadioButton> = emptyList<RadioButton>().toMutableList()
        Season.entries.map {
            val rb = RadioButton(context)
            rb.text = it.toString()
            seasonRadioButtons.plusAssign(rb)
        }
        seasonRadioButtons.map {
            binding.season.addView(it)
        }

        val typeRadioButtons: MutableList<RadioButton> = emptyList<RadioButton>().toMutableList()
        Type.entries.map {
            val rb = RadioButton(context)
            rb.text = it.toString()
            typeRadioButtons.plusAssign(rb)
        }
        typeRadioButtons.map {
            binding.type.addView(it)
        }

        var name: String
        var season: Season = Season.summer
        var type: Type = Type.top
        var description: String
        var clothesSize: ClothesSize? = null
        var shoesSize: Int? = null

        binding.clothesSize.setOnCheckedChangeListener { group, id ->
            val radio: RadioButton = group.findViewById(id)
            clothesSize = ClothesSize.valueOf(radio.text.toString())
        }

        binding.addButton.setOnClickListener {
            name = binding.nameField.editText?.text.toString()
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
            viewModel.insertNewClothes(newClothes)
            this.findNavController().navigate(R.id.action_clothesFormFragment_to_sleepTrackerFragment)
        }

        binding.season.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = group.findViewById(checkedId)
            season = Season.valueOf(radio.text.toString())
        }

        binding.type.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = group.findViewById(checkedId)
            type = Type.valueOf(radio.text.toString())
            when (type) {
                Type.top, Type.bottom -> {
                    clothesSize = null
                    binding.clothesSize.isVisible = true
                    binding.shoesSizeField.isVisible = false
                }
                Type.shoes -> {
                    shoesSize = null
                    binding.clothesSize.isVisible = false
                    binding.shoesSizeField.isVisible = true
                }
                else -> {
                    shoesSize = null
                    clothesSize = null
                    binding.clothesSize.isVisible = false
                    binding.shoesSizeField.isVisible = false
                }
            }
        }

        viewModel.currentClothesItem.observe(viewLifecycleOwner, Observer {cl ->
            binding.nameField.editText?.setText(cl?.name, TextView.BufferType.EDITABLE)
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
                }
                this.findNavController().navigate(R.id.action_clothesFormFragment_to_sleepTrackerFragment)
            }
        })

        return binding.root
    }
}