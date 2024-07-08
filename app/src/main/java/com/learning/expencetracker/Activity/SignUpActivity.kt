package com.learning.expencetracker.Activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.chaos.view.PinView
import com.learning.expencetracker.Model.ResendVerificationOTP.ResendVerificationCodeInputModel
import com.learning.expencetracker.Model.SignUp.SignUpInputModel
import com.learning.expencetracker.Model.VerifyOTP.VerifyOTPInputModel
import com.learning.expencetracker.R
import com.learning.expencetracker.Utils.Constants
import com.learning.expencetracker.ViewModel.AuthenticationModel
import com.learning.expencetracker.databinding.ActivitySignUpBinding


class SignUpActivity : BaseActivity() {

    lateinit var otpDialog: Dialog
    var name: String? = null
    var email: String? = null
    var password: String? = null
    var mobileNumber: String? = null

    lateinit var timer : TextView

    lateinit var countDownTimer :  CountDownTimer
    lateinit var binding: ActivitySignUpBinding

    lateinit var viewModel: AuthenticationModel
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        try {
            viewModel = ViewModelProvider(this)[AuthenticationModel::class.java]
            binding.signUpBtn.setOnClickListener {
                name = binding.nameET.text.toString()
                email = binding.emailET.text.toString()
                password = binding.passwordET.text.toString()
                mobileNumber = binding.mobileNumber.text.toString()

                if (valid()) {
                    showProgressBar(this)
                    var input = SignUpInputModel(email, mobileNumber, name, password)
                    try {
                        viewModel.signUpUser(input, this, this)
                    } catch (err: Exception) {
                        Log.d("rk", err.toString())
                    }
                }

            }
            binding.redirectToLoginPage.setOnClickListener {
                startActivity(Intent(this, SignInActivity::class.java))
                finish()
            }
            viewModel.observerForSignUpUser().observe(this, Observer { result ->
                cancelProgressBar()
                toast(this, result.message!!)
                toast(this, "User successfully login")
                onCreateDialog()
            })
            viewModel.observerForVerifyOTPUser().observe(this, Observer { result ->
                cancelProgressBar()
                otpDialog.dismiss()
                toast(this, result.message!!)
                var i =Intent(this, SignInActivity::class.java)
                i.putExtra(Constants.EMAIL, email!!)
                i.putExtra(Constants.PASSWORD, password!!)
                startActivity(i)
                finish()
            })
            viewModel.observerForResendVerificationOTP().observe(this, Observer { result ->
                cancelProgressBar()
                toast(this, result.message!!)

            })
        } catch (err: Exception) {
            Log.w("rk", err.toString())
        }
    }

    private fun valid(): Boolean {
        if (name!!.length == 0) {
            binding.nameET.setError("a name should be there")
            return false
        }
        if (email!!.length == 0) {
            binding.emailET.setError("a email should be there")
            return false
        }

        if (password!!.length == 0) {
            binding.passwordET.setError("a password should be there")
            return false
        }

        if (password!!.length < 8) {
            binding.passwordET.setError("a password with minimum length is 8")
            return false
        }

        return true;
    }

    fun errorFn(message: String) {
        cancelProgressBar()

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
        timer = view.findViewById(R.id.timer)
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

    fun timerFn(time:Long , textField : TextView)
    {
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