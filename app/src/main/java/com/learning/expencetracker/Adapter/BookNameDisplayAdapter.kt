package com.learning.expencetracker.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.learning.expencetracker.Model.BookNamesDisplayModel
import com.learning.expencetracker.R
import com.learning.expencetracker.databinding.BooksNameDisplayLayoutBinding

class BookNameDisplayAdapter(
    private val items: ArrayList<BookNamesDisplayModel>
) : RecyclerView.Adapter<BookNameDisplayAdapter.ViewHolder>() {
    private var onClickListener: OnClickListener? = null
    private var onClickListenerForMembers: OnClickListenerForMembers? = null
    private var onClickListenerFor3Dots: OnClickListenerFor3Dots? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = BooksNameDisplayLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.bookNameTV.text = item.name

        // Determine the icon based on userId size
        if ((item.userId?.size ?: 0) > 1) {
            holder.binding.personIv.setImageResource(R.drawable.users)
        } else {
            // Set default image if needed
        }
        // Set click listeners for different views
        holder.itemView.setOnClickListener {
            onClickListener?.onClick(position, item)
        }
        holder.binding.membersLL.setOnClickListener {
            onClickListenerForMembers?.onClickForMembers(position, holder.binding.membersLL)
        }
        holder.binding.optionIv.setOnClickListener {
            var location  = IntArray(2)
            holder.binding.optionIv.getLocationOnScreen(location)
            onClickListenerFor3Dots?.onClickFor3Dots(position, location)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setOnClickListener(listener: OnClickListener?) {
        this.onClickListener = listener
    }

    fun setOnClickListenerForMembers(listener: OnClickListenerForMembers?) {
        this.onClickListenerForMembers = listener
    }

    fun setOnClickListenerFor3Dots(listener: OnClickListenerFor3Dots?) {
        this.onClickListenerFor3Dots = listener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: BookNamesDisplayModel)
    }

    interface OnClickListenerForMembers {
        fun onClickForMembers(position: Int, model: LinearLayout)
    }

    interface OnClickListenerFor3Dots {
        fun onClickFor3Dots(position: Int, location: IntArray)
    }

    class ViewHolder(val binding: BooksNameDisplayLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}

