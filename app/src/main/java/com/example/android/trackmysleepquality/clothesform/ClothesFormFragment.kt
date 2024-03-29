package com.example.android.trackmysleepquality.clothesform

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.Clothes
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.databinding.FragmentClothesFormBinding
import com.example.android.trackmysleepquality.databinding.FragmentSleepTrackerBinding
import com.example.android.trackmysleepquality.enums.ClothesSize
import com.example.android.trackmysleepquality.enums.Season
import com.example.android.trackmysleepquality.enums.Type
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

        var name: String
        var season: Season = Season.summer
        var type: Type = Type.top
        var description: String
        var clothesSize: ClothesSize? = null
        var shoesSize: Int? = null

        binding.addButton.setOnClickListener {
            name = binding.nameField.editText?.text.toString()
            description = binding.descriptionField.editText?.text.toString()
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
            when (radio.text){
                "summer" -> season = Season.summer
                "winter" -> season = Season.winter
                "demiseason" -> season = Season.demiseason
            }
        }

        binding.type.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = group.findViewById(checkedId)
            when (radio.text){
                "Top" -> type = Type.top
                "Bottom" -> type = Type.bottom
                "Shoes" -> type = Type.shoes
                "Headdress" -> type = Type.headdress
                "Accessories" -> type = Type.accessories
            }
        }

        return binding.root
    }
}