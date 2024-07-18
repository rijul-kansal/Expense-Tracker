package com.learning.expencetracker.Activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

        if(!checkPermission())
        {
            requestPermission()
        }
        binding.signIn.setOnClickListener {
            startActivity(Intent(this,SignInActivity::class.java))
        }
        binding.signUp.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }
    }

    private fun checkPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
           this,
            Manifest.permission.READ_MEDIA_IMAGES
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this ,permissions(), 1)

    }
    val storagePermissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.POST_NOTIFICATIONS,
    )
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    val storagePermissions33 = arrayOf(
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.POST_NOTIFICATIONS,
    )
    fun permissions(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            storagePermissions33
        } else {
            storagePermissions
        }
    }

}