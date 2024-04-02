package com.example.android.trackmysleepquality.giftslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.Gift
import com.google.android.material.textfield.TextInputLayout

class GiftListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val image: ImageView = itemView.findViewById(R.id.image)
    val nameText: TextView = itemView.findViewById(R.id.gift_name)
    val priceText: TextView = itemView.findViewById(R.id.gift_price)
    val nameField: TextInputLayout = itemView.findViewById(R.id.update_name_layout)
    val priceField: TextInputLayout = itemView.findViewById(R.id.update_price_layout)
    val updateButton: Button = itemView.findViewById(R.id.update_button)
}
class GiftListAdapter(private val itemClickListener: ItemClickListener) : RecyclerView.Adapter<GiftListViewHolder>() {
    var data = listOf<Gift>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    interface ItemClickListener {
        fun onItemClick(gift: Gift, item: GiftListViewHolder)
        fun onLongClick(gift: Gift)
    }

    override fun onBindViewHolder(holder: GiftListViewHolder, position: Int) {
        val item = data[position]
        holder.nameText.text = item.giftName
        holder.priceText.text = item.price.toString()+" $"
        holder.image.setImageResource(R.drawable.ic_sleep_5)

        holder.nameField.editText?.setText(item.giftName)
        holder.nameField.isVisible = false
        holder.priceField.editText?.setText(item.price.toString())
        holder.priceField.isVisible = false
        holder.updateButton.isVisible = false

        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(item, holder)
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