package com.learning.expencetracker.Adapter


import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.learning.expencetracker.Model.GraphShownModel
import com.learning.expencetracker.databinding.CharDispViewBinding
import com.learning.expencetracker.databinding.MoneyShowBinding


class GraphItemAdapter(
    private val items: ArrayList<GraphShownModel>
) : RecyclerView.Adapter<GraphItemAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CharDispViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.colourIv.setBackgroundColor(Color.parseColor(item.colour))
        holder.binding.itemNameTv.text = item.name
        holder.binding.itemAmountTv.text = item.quantity

    }
    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(val binding: CharDispViewBinding) : RecyclerView.ViewHolder(binding.root)
}

