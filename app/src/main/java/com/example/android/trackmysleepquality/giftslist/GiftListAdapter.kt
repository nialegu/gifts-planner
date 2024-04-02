package com.example.android.trackmysleepquality.giftslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.Gift

class GiftListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val image: ImageView = itemView.findViewById(R.id.image)
    val nameText: TextView = itemView.findViewById(R.id.gift_name)
    val priceText: TextView = itemView.findViewById(R.id.gift_price)
}
class GiftListAdapter(private val itemClickListener: ItemClickListener) : RecyclerView.Adapter<GiftListViewHolder>() {
    var data = listOf<Gift>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    interface ItemClickListener {
        fun onItemClick(gift: Gift)
        fun onLongClick(gift: Gift)
    }

    override fun onBindViewHolder(holder: GiftListViewHolder, position: Int) {
        val item = data[position]
        holder.nameText.text = item.giftName
        holder.priceText.text = item.price.toString()+" $"
        holder.image.setImageResource(R.drawable.ic_sleep_5)

        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(item)
        }
        holder.itemView.setOnLongClickListener{
            itemClickListener.onLongClick(item)
            return@setOnLongClickListener true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiftListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.list_item_gift, parent, false)
        return GiftListViewHolder(view)
    }
}