package com.learning.expencetracker.Activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.learning.expencetracker.Model.ChangePasswordModel.ChangePasswordInputModel
import com.learning.expencetracker.Model.UpdateUser.UpdateUserInputModel
import com.learning.expencetracker.R
import com.learning.expencetracker.Utils.Constants
import com.learning.expencetracker.ViewModel.AuthenticationModel
import com.learning.expencetracker.ViewModel.FireBaseViewModel
import com.learning.expencetracker.databinding.ActivityProfileBinding
import java.io.IOException


class ProfileActivity : BaseActivity() {
    lateinit var changePasswordDialog:Dialog
    lateinit var binding:ActivityProfileBinding
    lateinit var viewModel:AuthenticationModel
    lateinit var token:String
    private var filePath: Uri? = null
    var dialogDisplay:Dialog?=null
    // request code
    private val PICK_IMAGE_REQUEST = 22

    var newMobileNumber:String=""
    var oldMobileNumber:String=""
    var oldName:String=""
    var newName:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        try{
            binding.changePasswordCv.setOnClickListener {
                changePasswordDialog()
            }
            binding.profileImage.setOnClickListener {
                SelectImage()
            }


            viewModel=ViewModelProvider(this)[AuthenticationModel::class.java]


            val sharedPreference =  getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
            token = sharedPreference.getString(Constants.TOKEN,"defaultName").toString()
            showProgressBar(this)
            viewModel.getUser("Bearer ${token}" ,this)

            viewModel.observerForGetUser().observe(this, Observer {
                    result->
                cancelProgressBar()
                if(result!=null)
                {
                    oldName  = result.data!!.name.toString()
                    val email  = result.data!!.email
                    oldMobileNumber  = result.data!!.mobileNumber.toString()
                    val image  = result.data!!.Image

                    Glide
                        .with(this)
                        .load(image)
                        .centerCrop()
                        .placeholder(R.drawable.logo)
                        .into(binding.profileImage)
                    binding.emailTv.text=email
                    binding.emailTv.visibility=View.VISIBLE
                    binding.nameTv.visibility=View.VISIBLE
                    binding.mobileNoTv.visibility=View.VISIBLE
                    binding.nameTv.text=oldName
                    binding.mobileNoTv.text=oldMobileNumber
                }
            })
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

            binding.nameTv.setOnClickListener {
                showDialog("Please Rename your name",1)
            }
            binding.mobileNoTv.setOnClickListener {
                showDialog("Please Reenter your mobile Number",2)
            }

            binding.updateTv.setOnClickListener {
                var name:String?=null
                var mobileNumber:String?=null
                var flag=0
                if(oldName!=newName && newName!="")
                {
                    flag=1
                    name=newName
                }
                if(newMobileNumber!=oldMobileNumber && newMobileNumber!="")
                {
                    flag=1
                    mobileNumber= newMobileNumber
                }
                if(flag==1)
                    viewModel.updateUser("Bearer ${token}" ,this, UpdateUserInputModel(null,name,mobileNumber,null))
            }

            viewModel.observerForUser().observe(this, Observer {
                res->
                toast(this, "Changed Successfully")

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
    private fun SelectImage() {

        // Defining Implicit Intent to mobile gallery
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Select Image from here..."
            ),
            PICK_IMAGE_REQUEST
        )
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {

            // Get the Uri of data
            filePath = data.data
            FireBaseViewModel().uploadImage(filePath!!,token,this)
            try {

                // Setting image on image view using Bitmap
                val bitmap = MediaStore.Images.Media
                    .getBitmap(
                        contentResolver,
                        filePath
                    )
                binding.profileImage.setImageBitmap(bitmap)
            } catch (e: IOException) {
                // Log the exception
                e.printStackTrace()
            }
        }
    }
    fun showDialog(title:String, type:Int) {
        dialogDisplay = Dialog(this, android.R.style.Theme_Translucent_NoTitleBar)
        dialogDisplay=Dialog(this)
        val view: View = LayoutInflater.from(this).inflate(R.layout.useremail_dialog, null)

        val submitOtpButton = view.findViewById<TextView>(R.id.sendOtpBtn)
        val titleTv= view.findViewById<TextView>(R.id.titleTv)
        submitOtpButton.text = "Update"
        titleTv.text=title

        dialogDisplay!!.setContentView(view)
        dialogDisplay!!.setCanceledOnTouchOutside(false)
        val window = dialogDisplay!!.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setGravity(Gravity.BOTTOM)

        submitOtpButton.setOnClickListener {
            if(type==1)
            {
                newName = view.findViewById<EditText>(R.id.emailET).text.toString()
                binding.nameTv.text=newName
                dialogDisplay!!.dismiss()
            }
            else
            {
                newMobileNumber = view.findViewById<EditText>(R.id.emailET).text.toString()
                binding.mobileNoTv.text=newMobileNumber
                dialogDisplay!!.dismiss()
            }
        }
        dialogDisplay!!.show()
    }
}