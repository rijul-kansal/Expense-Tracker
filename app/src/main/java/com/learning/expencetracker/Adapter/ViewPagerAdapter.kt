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


class ViewPagerAdapter(var context: Context) : PagerAdapter() {
    var images = intArrayOf(
        R.drawable.add_trans,
        R.drawable.adding_members,
        R.drawable.downlad_data
    )
    var headings = intArrayOf(
        R.string.walk_through_1h,
        R.string.walk_through_2h,
        R.string.walk_through_3h
    )
    var descriptions = intArrayOf(
        R.string.walk_through_1d,
        R.string.walk_through_2d,
        R.string.walk_through_3d
    )

    override fun getCount(): Int {
        return headings.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = layoutInflater.inflate(R.layout.walk_through_container, container, false)
        val slideTitleImage = view.findViewById<View>(R.id.imageView) as ImageView
        val slideHeading = view.findViewById<View>(R.id.title) as TextView
        val slideDescription = view.findViewById<View>(R.id.description) as TextView

        // Set Lottie animation
        slideTitleImage.setImageResource(images[position])

        // Set heading and description
        slideHeading.setText(headings[position])
        slideDescription.setText(descriptions[position])

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}