package com.example.android.trackmysleepquality.receiverslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.Receiver


class TextItemViewHolder(val textView: TextView): RecyclerView.ViewHolder(textView)
class ReceiveListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val image: ImageView = itemView.findViewById(R.id.image)
    val nameText: TextView = itemView.findViewById(R.id.receiver_name_text)
}
class ReceiverListAdapter(private val itemClickListener: ItemClickListener) : RecyclerView.Adapter<ReceiveListViewHolder>() {
    var data = listOf<Receiver>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    interface ItemClickListener {
        fun onItemClick(receiver: Receiver)
        fun onLongClick(receiver: Receiver)
    }

    override fun onBindViewHolder(holder: ReceiveListViewHolder, position: Int) {
        val item = data[position]
        holder.nameText.text = item.receiverName
        holder.image.setImageResource(R.drawable.ic_sleep_5)

        holder.itemView.setOnClickListener {
                itemClickListener.onItemClick(item)
        }
        holder.itemView.setOnLongClickListener{
            itemClickListener.onLongClick(item)
            return@setOnLongClickListener true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiveListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.list_item_receive, parent, false)
        return ReceiveListViewHolder(view)
    }
}