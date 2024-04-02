package com.example.android.trackmysleepquality.giftslist

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.AppDatabase
import com.example.android.trackmysleepquality.database.Gift
import com.example.android.trackmysleepquality.databinding.FragmentGiftsListBinding

class GiftListFragment : Fragment(), GiftListAdapter.ItemClickListener {

    private lateinit var viewModel: GiftListViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: FragmentGiftsListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_gifts_list, container, false
        )

        val application = requireNotNull(this.activity).application
        val dao = AppDatabase.getInstance(application).getAppDatabaseDao()
        val viewModelFactory = GiftListViewModelFactory(dao, application)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(GiftListViewModel::class.java)

        val adapter = GiftListAdapter(this)
        binding.giftsList.adapter = adapter

        viewModel.gifts.observe(viewLifecycleOwner, Observer { gifts ->
            if (gifts != null)
                adapter.data = gifts
        })

        binding.createGiftButton.setOnClickListener {
            val giftName = binding.editGift.text.toString()

            if (binding.editPrice.text.toString() == "") return@setOnClickListener
            val giftPrice = binding.editPrice.text.toString().toDouble()

            if (giftName != "") {
                viewModel.insertGift(giftName, giftPrice)
                binding.editGift.setText("")
                binding.editPrice.setText("")
            }
        }

        return binding.root
    }

    override fun onItemClick(gift: Gift, item: GiftListViewHolder) {
        item.nameText.isVisible = false
        item.priceText.isVisible = false

        item.nameField.isVisible = true
        item.priceField.isVisible = true
        item.updateButton.isVisible = true

        item.updateButton.setOnClickListener {
            val nameToUpdate = item.nameField.editText?.text.toString()
            val priceToUpdate: Double

            if (item.priceField.editText?.text.toString() != ""){
                priceToUpdate = item.priceField.editText?.text.toString().toDouble()
                viewModel.updateGift(gift.copy(giftName = nameToUpdate, price = priceToUpdate))

                item.nameText.isVisible = true
                item.priceText.isVisible = true

                item.nameField.isVisible = false
                item.priceField.isVisible = false
                item.updateButton.isVisible = false
            }

        }
    }

    override fun onLongClick(gift: Gift) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.delete)
        builder.setMessage(R.string.deleteGiftAlert)
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes") { _, _ ->
            viewModel.deleteGift(gift)
        }
        builder.setNegativeButton("No") { _, _ ->}

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(true)

        alertDialog.show()
    }
}