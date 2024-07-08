package com.learning.expencetracker.Activity


import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.learning.expencetracker.R
import com.learning.expencetracker.Adapter.ViewPagerAdapter
import com.learning.expencetracker.databinding.ActivityWalkThroughScreenBinding


class WalkThroughScreen : AppCompatActivity() {
    private lateinit var dots: ArrayList<TextView>

    lateinit var viewPagerAdapter: ViewPagerAdapter


    lateinit var binding : ActivityWalkThroughScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityWalkThroughScreenBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        try {
            dots = ArrayList()
            binding.backbtn.setOnClickListener {
                if (getItem(0) > 0) {
                    binding.slideViewPager.setCurrentItem(getItem(-1), true)
                }
            }
            binding.frontbtn.setOnClickListener {
                if (getItem(0) < 2) {
                    binding.slideViewPager.setCurrentItem(getItem(1), true)
                } else {
                    val i = Intent(this@WalkThroughScreen, IntroActivity::class.java)
                    startActivity(i)
                    finish()
                }
            }
            binding.skipButton.setOnClickListener {
                val i = Intent(this@WalkThroughScreen, IntroActivity::class.java)
                startActivity(i)
                finish()
            }
            viewPagerAdapter = ViewPagerAdapter(this)
            binding.slideViewPager.adapter = viewPagerAdapter
            setUpIndicator(0)
            binding.slideViewPager.addOnPageChangeListener(viewListener)
        } catch (e: Exception) {
            Log.d("rk", e.message.toString())
        }

    }
    private fun setUpIndicator(position: Int) {
        dots.clear()
        binding.indicatorLayout.removeAllViews()
        for (i in 0..2) {
            dots.add(TextView(this))
            dots[i].text = Html.fromHtml("&#8226;")
            dots[i].textSize = 35F
            dots[i].setTextColor(
                resources.getColor(
                    R.color.grey,
                    applicationContext.theme
                )
            )
            binding.indicatorLayout.addView(dots[i])
        }
        if (dots.isNotEmpty()) {
            dots[position].setTextColor(resources.getColor(R.color.black, applicationContext.theme))
        }
    }

    private val viewListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
        override fun onPageSelected(position: Int) {
            setUpIndicator(position)

            if (position > 0) {
                binding.backbtn.visibility = View.VISIBLE
            } else {
                binding.backbtn.visibility = View.INVISIBLE
            }
        }
        override fun onPageScrollStateChanged(state: Int) {}
    }

    private fun getItem(i: Int): Int {
        return binding.slideViewPager.currentItem.plus(i) ?: 0
    }
}