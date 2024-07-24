package com.learning.expencetracker.Adapter

import android.widget.ImageView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.learning.expencetracker.R


class ViewPagerAdapter2(var context: Context) : PagerAdapter() {
    var images = intArrayOf(
        R.drawable.free_image,
        R.drawable.payment1,
        R.drawable.payment2
    )
    var type = intArrayOf(
        R.string.type_1,
        R.string.type_2,
        R.string.type_3
    )
    var amount = intArrayOf(
        R.string.amount_1,
        R.string.amount_2,
        R.string.amount_3
    )

    override fun getCount(): Int {
        return type.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = layoutInflater.inflate(R.layout.payment_choosen, container, false)
        val slideImage = view.findViewById<View>(R.id.image) as ImageView
        val slideType = view.findViewById<View>(R.id.type) as TextView
        val slideAmount = view.findViewById<View>(R.id.amountPC) as TextView


        slideImage.setImageResource(images[position])
        slideType.setText(type[position])
        slideAmount.setText(amount[position])
        val a = view.findViewById<View>(R.id.cfIV) as ImageView
        val b = view.findViewById<View>(R.id.sgfIV) as ImageView
        if(position == 0)
        {
            a.setImageResource(R.drawable.grey_tick)
            b.setImageResource(R.drawable.grey_tick)
        }
        else if(position == 1)
        {
            b.setImageResource(R.drawable.grey_tick)
        }
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}