package com.learning.expencetracker.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.learning.expencetracker.Model.PaymentHistoryModel
import com.learning.expencetracker.R
import com.learning.expencetracker.databinding.PaymentHistoryAdapterViewBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PaymentHistoryAdapter(
    private val items: ArrayList<PaymentHistoryModel>
) : RecyclerView.Adapter<PaymentHistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PaymentHistoryAdapterViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
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
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        if(item.amount == "600")
        {
            holder.binding.planType.text = "Standard Plan"
            holder.binding.imageView.setImageResource(R.drawable.payment1)
        }
        else
        {
            holder.binding.planType.text = "Pro Plan"
            holder.binding.imageView.setImageResource(R.drawable.payment2)
        }
        holder.binding.date.text= getDateTime(item.date)
        holder.binding.validTill.text= getDateTime(item.validTill)

    }

    override fun getItemCount(): Int {
        return items.size
    }
    class ViewHolder(val binding: PaymentHistoryAdapterViewBinding) : RecyclerView.ViewHolder(binding.root)
}

