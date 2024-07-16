package com.learning.expencetracker.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.learning.expencetracker.R
import com.learning.expencetracker.Utils.Constants


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val handler = Handler()
        handler.postDelayed( {
            val sharedPreference =  getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
            val token =sharedPreference.getString(Constants.TOKEN,"defaultName")
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

        }, 3000)
    }
}