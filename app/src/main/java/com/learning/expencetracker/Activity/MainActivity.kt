package com.learning.expencetracker.Activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.learning.expencetracker.Fragments.HomeFragment
import com.learning.expencetracker.Fragments.SettingsFragment
import com.learning.expencetracker.Fragments.StatisticsFragment
import com.learning.expencetracker.R
import com.learning.expencetracker.Utils.Constants
import com.learning.expencetracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView
    lateinit var token : String
    lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val sharedPreference =  getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        token = sharedPreference.getString(Constants.TOKEN,"defaultName").toString()
        Log.d("rk",token)
        loadFragment(HomeFragment())
        bottomNav = findViewById(R.id.bottomNav) as BottomNavigationView
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.stats -> {
                    loadFragment(StatisticsFragment())
                    true
                }
                R.id.settings -> {
                    loadFragment(SettingsFragment())
                    true
                }
                else -> {
                    loadFragment(HomeFragment())
                    true}
            }
        }

    }
    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }

}