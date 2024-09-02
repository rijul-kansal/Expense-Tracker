package com.learning.expencetracker.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.learning.expencetracker.R
import com.learning.expencetracker.Utils.Constants
import com.learning.expencetracker.ViewModel.PaymentViewModel


class SplashActivity : BaseActivity() {

    lateinit var viewModel : PaymentViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Log.d("rk","OnCreate Activity")
        viewModel=ViewModelProvider(this)[PaymentViewModel::class.java]
        val sharedPreference =  getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        val token =sharedPreference.getString(Constants.TOKEN,"defaultName")
        val handler = Handler()
        handler.postDelayed( {
            if(token == "defaultName")
            {
                startActivity(Intent(this, WalkThroughScreen::class.java))
                finish()
            }
            else
            {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }, 1000)
    }
    fun errorFn(message: String) {
        toast(this, message)
    }
}