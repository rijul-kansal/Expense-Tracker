package com.learning.expencetracker.Activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.chaos.view.PinView
import com.learning.expencetracker.Model.ForgottenPassword.ForgottenPasswordInputModel
import com.learning.expencetracker.Model.LoginUser.LoginUserInputModel
import com.learning.expencetracker.Model.ResendVerificationOTP.ResendVerificationCodeInputModel
import com.learning.expencetracker.Model.ResetPassword.ResetPasswordInputModel
import com.learning.expencetracker.Model.VerifyOTP.VerifyOTPInputModel
import com.learning.expencetracker.R
import com.learning.expencetracker.Utils.Constants
import com.learning.expencetracker.ViewModel.AuthenticationModel
import com.learning.expencetracker.databinding.ActivitySignInBinding

class SignInActivity : BaseActivity() {
    lateinit var binding:ActivitySignInBinding
    var email :String?=null
    var emailUserEmailDialog :String?=null
    var password :String?=null
    lateinit var otpDialog: Dialog
    lateinit var userNameDialog: Dialog
    lateinit var resetPasswordDialog: Dialog
    lateinit var countDownTimer :  CountDownTimer
    lateinit var viewModel: AuthenticationModel

    lateinit var timer : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivitySignInBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        try{
            viewModel = ViewModelProvider(this)[AuthenticationModel::class.java]

            if(intent.hasExtra(Constants.EMAIL))
            {
                email =intent.getStringExtra(Constants.EMAIL)
                binding.emailET.setText(email!!)
            }
            if(intent.hasExtra(Constants.PASSWORD))
            {
                password =intent.getStringExtra(Constants.PASSWORD)
                binding.passwordET.setText(password!!)
            }
            binding.signInBtn.setOnClickListener {
                email = binding.emailET.text.toString()
                password = binding.passwordET.text.toString()

                if(valid())
                {
                    showProgressBar(this)
                    viewModel.loginUser(LoginUserInputModel(email,password), this, this )
                }
            }
            binding.forgottenPassword.setOnClickListener {
                forgottenPasswordDialog()
            }
            viewModel.observerForLoginUser().observe(this , Observer {
              result ->
                cancelProgressBar()
                val sharedPreference =  getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
                var editor = sharedPreference.edit()
                editor.putString(Constants.TOKEN,result.token)
                editor.apply()


                toast(this,result.message.toString())
                val i =Intent(this, MainActivity::class.java)
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(i)
                finish()
            } )

            viewModel.observerForVerifyOTPUser().observe(this, Observer { result ->
                cancelProgressBar()
                otpDialog.dismiss()
                toast(this, result.message!!)
            })
            viewModel.observerForForgottenPassword().observe(this, Observer { result ->
                cancelProgressBar()
                userNameDialog.dismiss()
                toast(this, result.message!!)
                resetPasswordDialog()
            })
            viewModel.observerForResetPassword().observe(this, Observer { result ->
                cancelProgressBar()
                resetPasswordDialog.dismiss()
                toast(this, result.message!!)
            })
        }catch(err:Exception)
        {
            Log.d("rk",err.message.toString())
        }

    }

    private fun valid(): Boolean {
        if (email!!.length == 0) {
            binding.emailET.setError("a email should be there")
            return false
        }
        if (password!!.length == 0) {
            binding.passwordET.setError("a password should be there")
            return false
        }
        return true;
    }

    fun errorFn(message: String) {
        cancelProgressBar()

        if(message == "Please verify your email first . OTP send to your registered email id ")
        {
            onCreateDialog()
        }
        if(message == "OTP Expired. New OTP Send to youe registered email address")
        {
            timerFn(300000,timer)
        }
        toast(this, message)
    }

    fun onCreateDialog() {
        otpDialog = Dialog(this, android.R.style.Theme_Translucent_NoTitleBar)

        otpDialog = Dialog(this)
        val view: View = LayoutInflater.from(this).inflate(R.layout.otp_verification_dialog, null)
        val submitOtpButton = view.findViewById<TextView>(R.id.Enter_otp_btn)
        timer = view.findViewById<TextView>(R.id.timer)
        val resendCode = view.findViewById<LinearLayout>(R.id.resendVerificationCode)
        timer.visibility = View.VISIBLE
        timerFn(300000,timer)
        otpDialog.setContentView(view)
        otpDialog.setCanceledOnTouchOutside(false)
        val window = otpDialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setGravity(Gravity.BOTTOM)

        resendCode.setOnClickListener {
            countDownTimer.cancel()
            showProgressBar(this)
            timerFn(300000,timer)
            viewModel.resendVerificationOTP(ResendVerificationCodeInputModel(email),this,this )
        }

        submitOtpButton.setOnClickListener {
            showProgressBar(this)
            val otp = view.findViewById<PinView>(R.id.pinview).text.toString()
            viewModel.verifyOTP(VerifyOTPInputModel(email,otp),this, this)
        }
        otpDialog.show()
    }

    fun resetPasswordDialog() {
        resetPasswordDialog = Dialog(this, android.R.style.Theme_Translucent_NoTitleBar)

        resetPasswordDialog = Dialog(this)
        val view: View = LayoutInflater.from(this).inflate(R.layout.reset_password_dialog, null)
        val submitOtpButton = view.findViewById<TextView>(R.id.resetOtpBtn)
        timer = view.findViewById<TextView>(R.id.timer)
        val resendCode = view.findViewById<LinearLayout>(R.id.resendVerificationCode)
        timer.visibility = View.VISIBLE
        timerFn(300000,timer)
        resetPasswordDialog.setContentView(view)
        resetPasswordDialog.setCanceledOnTouchOutside(false)
        val window = resetPasswordDialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setGravity(Gravity.BOTTOM)

        resendCode.setOnClickListener {
            countDownTimer.cancel()
            showProgressBar(this)
            timerFn(300000,timer)
            viewModel.resendVerificationOTP(ResendVerificationCodeInputModel(email),this,this )
        }

        submitOtpButton.setOnClickListener {
            showProgressBar(this)
            val otp = view.findViewById<PinView>(R.id.pinview).text.toString()
            val newpassword = view.findViewById<EditText>(R.id.passwordEt).text.toString()
            viewModel.resetPassword(ResetPasswordInputModel(emailUserEmailDialog,newpassword,otp),this, this)
        }
        resetPasswordDialog.show()
    }

    fun forgottenPasswordDialog() {
        userNameDialog = Dialog(this, android.R.style.Theme_Translucent_NoTitleBar)
        userNameDialog=Dialog(this)
        val view: View = LayoutInflater.from(this).inflate(R.layout.useremail_dialog, null)
        val submitOtpButton = view.findViewById<TextView>(R.id.sendOtpBtn)
        userNameDialog.setContentView(view)
        userNameDialog.setCanceledOnTouchOutside(false)
        val window = userNameDialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setGravity(Gravity.BOTTOM)

        submitOtpButton.setOnClickListener {
            showProgressBar(this)
            emailUserEmailDialog = view.findViewById<EditText>(R.id.emailET).text.toString()
            viewModel.forgottenPassword(ForgottenPasswordInputModel(emailUserEmailDialog),this, this)
        }
        userNameDialog.show()
    }
    fun timerFn(time:Long , textField : TextView) {
        countDownTimer = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                textField.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                textField.text = "Time's finished! Please click on Resent OTP button"
            }
        }.start()
    }
}