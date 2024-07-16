package com.learning.expencetracker.Activity

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.learning.expencetracker.Model.ChangePasswordModel.ChangePasswordInputModel
import com.learning.expencetracker.Utils.Constants
import com.learning.expencetracker.ViewModel.AuthenticationModel
import com.learning.expencetracker.databinding.ActivityProfileBinding


class ProfileActivity : BaseActivity() {
    lateinit var changePasswordDialog:Dialog
    lateinit var binding:ActivityProfileBinding
    lateinit var viewModel:AuthenticationModel
    lateinit var token:String
    private val STORAGE_PERMISSION_CODE = 23

    companion object {
        private const val CAMERA_PERMISSION_CODE = 100
        private const val STORAGE_PERMISSION_CODE = 101
    }

    val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        val galleryUri = it
        try{
            binding.profileImage.setImageURI(galleryUri)
        }catch(e:Exception){
            e.printStackTrace()
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        try{

            binding.changePasswordCv.setOnClickListener {
                changePasswordDialog()
            }

            binding.profileImage.setOnClickListener {
                checkPermission()
            }
            viewModel=ViewModelProvider(this)[AuthenticationModel::class.java]
            val sharedPreference =  getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
            token = sharedPreference.getString(Constants.TOKEN,"defaultName").toString()

            viewModel.observerForChangePassword().observe(this, Observer {
                result->
                cancelProgressBar()
                if(result!=null)
                {
                    toast(this,"Password change successfully Please login again")
                    changePasswordDialog.dismiss()
                    val sharedPreference = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
                    var editor = sharedPreference.edit()
                    editor.remove(Constants.TOKEN)
                    editor.commit()
                    var intent= Intent(this, SignInActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                }
            })

        }catch (err:Exception)
        {
            Log.d("rk",err.message.toString())
        }
    }

    fun changePasswordDialog() {
        changePasswordDialog = Dialog(this)
        val view: View = LayoutInflater.from(this).inflate(com.learning.expencetracker.R.layout.change_pass_view, null)
        val submitOtpButton = view.findViewById<TextView>(com.learning.expencetracker.R.id.changePassTV)
        changePasswordDialog.setContentView(view)
        val window = changePasswordDialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setGravity(Gravity.BOTTOM)



        submitOtpButton.setOnClickListener {
            val newpassword = view.findViewById<EditText>(com.learning.expencetracker.R.id.passwordNewEt).text.toString()
            val oldpassword = view.findViewById<EditText>(com.learning.expencetracker.R.id.passwordOldEt).text.toString()
            showProgressBar(this)
            viewModel.changePassword(ChangePasswordInputModel(newpassword,oldpassword),"Baerer $token" , this)
        }
        changePasswordDialog.show()
    }

    fun errorFn(message: String) {
        cancelProgressBar()
        toast(this, message)
    }

    private fun checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermessionLauncher.launch(READ_MEDIA_IMAGES)
        } else {
            requestPermessionLauncher.launch(READ_EXTERNAL_STORAGE)
        }
    }

    val requestPermessionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            galleryLauncher.launch("image/*")
        } else {
            checkPermission()
        }
    }
}