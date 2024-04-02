package com.example.android.trackmysleepquality.planslist

import android.content.ContextWrapper
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.PlanReceiverGifts
import com.example.android.trackmysleepquality.formatPlansForOneItem

class PlanListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val planStrings: TextView = itemView.findViewById(R.id.plan_strings)
}
class PlanListAdapter(private val itemClickListener: ItemClickListener, private val resources: Resources) : RecyclerView.Adapter<PlanListViewHolder>() {
    var data = listOf<PlanReceiverGifts>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    interface ItemClickListener {
        fun onItemClick(plan: PlanReceiverGifts, item: PlanListViewHolder)
        fun onLongClick(plan: PlanReceiverGifts)
    }

    override fun onBindViewHolder(holder: PlanListViewHolder, position: Int) {
        val item = data[position]

        holder.planStrings.text = formatPlansForOneItem(item, resources)

        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(item, holder)
        }
        holder.itemView.setOnLongClickListener{
            itemClickListener.onLongClick(item)
            return@setOnLongClickListener true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.text_item_view, parent, false)
        return PlanListViewHolder(view)
    }
}