package com.learning.expencetracker.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.learning.expencetracker.R


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getSupportActionBar()?.hide();
        val handler = Handler()
        handler.postDelayed( {
            startActivity(Intent(this, WalkThroughScreen::class.java))
            finish()
        }, 3000)
    }
}