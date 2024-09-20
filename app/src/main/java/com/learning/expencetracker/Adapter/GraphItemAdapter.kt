package com.learning.expencetracker.Adapter


import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.learning.expencetracker.Model.GraphShownModel
import com.learning.expencetracker.databinding.CharDispViewBinding


class GraphItemAdapter(
    private val items: ArrayList<GraphShownModel>
) : RecyclerView.Adapter<GraphItemAdapter.ViewHolder>() {
    var c=0


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
        var arr = ArrayList<String>()
        arr.add("#FF5733")
        arr.add("#33FF57")
        arr.add("#3357FF")
        arr.add("#FFFF33")
        arr.add("#FF33FF")
        arr.add("#33FFFF")
        arr.add("#800080")
        arr.add("#FFA500")
        holder.binding.colourIv.setBackgroundColor(Color.parseColor(arr[c]))
        holder.binding.itemNameTv.text = item.name+"->"
        holder.binding.itemAmountTv.text = item.quantity
        c=(c+1)%8

    }
    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(val binding: CharDispViewBinding) : RecyclerView.ViewHolder(binding.root)
}

