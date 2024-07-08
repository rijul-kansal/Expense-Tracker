package com.learning.expencetracker.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.learning.expencetracker.R
import com.learning.expencetracker.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {
    lateinit var binding:ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityIntroBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        getSupportActionBar()?.hide()


        binding.signIn.setOnClickListener {
            startActivity(Intent(this,SignInActivity::class.java))
        }
        binding.signUp.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }
    }
}