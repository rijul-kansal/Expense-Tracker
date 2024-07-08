package com.learning.expencetracker.Adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.learning.expencetracker.Model.TransDisplayModel
import com.learning.expencetracker.databinding.MoneyShowBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransDisplayAdapter(
    private val items: ArrayList<TransDisplayModel>
) : RecyclerView.Adapter<TransDisplayAdapter.ViewHolder>() {
    private var onClickListener: OnClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MoneyShowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        Log.d("rk",item.time!!)
        holder.binding.amount.text = item.amount
        holder.binding.description.text = item.description
        holder.binding.category.text = item.category
        holder.binding.addedAt.text = "Added At: "+item.time?.let { getDateTime(it) }
        holder.binding.addedBy.text = "Added By: "+(item.addedBy)!!.split('@')[0]

        if(item.moneyType == "Out")
        {
            holder.binding.amount.setTextColor(Color.parseColor("#d00000"))
        }

        // Set click listeners for different views
        holder.itemView.setOnClickListener {
            onClickListener?.onClick(position, item)
        }
    }
    private fun getDateTime(s: String): String? {
        try {
            val sdf = SimpleDateFormat("dd-MMMM-yyyy, HH:mm:ss", Locale.ENGLISH)
            val netDate = Date(s.toLong())
            return sdf.format(netDate)
        } catch (e: Exception) {
            Log.d("rk",e.message.toString())
            return ""
        }
    }
    override fun getItemCount(): Int {
        return items.size
    }

    fun setOnClickListener(listener: OnClickListener?) {
        this.onClickListener = listener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: TransDisplayModel)
    }
    class ViewHolder(val binding: MoneyShowBinding) : RecyclerView.ViewHolder(binding.root)
}

