package com.example.android.trackmysleepquality.receiverslist

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.AppDatabase
import com.example.android.trackmysleepquality.database.Receiver
import com.example.android.trackmysleepquality.databinding.FragmentReceiverListBinding

class ReceiverListFragment : Fragment(), ReceiverListAdapter.ItemClickListener {

    private lateinit var viewModel: ReceiverListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: FragmentReceiverListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_receiver_list, container, false
        )

        val application = requireNotNull(this.activity).application
        val dao = AppDatabase.getInstance(application).getAppDatabaseDao()
        val viewModelFactory = ReceiverListViewModelFactory(dao, application)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(ReceiverListViewModel::class.java)

        val adapter = ReceiverListAdapter(this)
        binding.receiverList.adapter = adapter

        viewModel.receivers.observe(viewLifecycleOwner, Observer { receivers ->
            if (receivers != null)
                adapter.data = receivers
        })

        binding.createReceiveButton.setOnClickListener {
            val name = binding.createEditText.text.toString()
            if (name != "") {
                viewModel.insertReceiver(name)
                binding.createEditText.setText("")
            }
        }

        return binding.root
    }

    override fun onItemClick(receiver: Receiver) {
        Toast.makeText(context, receiver.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onLongClick(receiver: Receiver) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.delete)
        builder.setMessage(R.string.deleteAlert)
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes") { _, _ ->
            viewModel.deleteReceive(receiver)
        }
        builder.setNegativeButton("No") { _, _ ->}

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(true)

        alertDialog.show()
    }
}