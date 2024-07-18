package com.learning.expencetracker.ViewModel

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.expencetracker.Activity.ProfileActivity
import com.learning.expencetracker.Activity.SignInActivity
import com.learning.expencetracker.Activity.SignUpActivity
import com.learning.expencetracker.Model.ChangePasswordModel.ChangePasswordInputModel
import com.learning.expencetracker.Model.ChangePasswordModel.ChangePasswordOutputModel
import com.learning.expencetracker.Model.ForgottenPassword.ForgottenPasswordInputModel
import com.learning.expencetracker.Model.ForgottenPassword.ForgottenPasswordOutputModel
import com.learning.expencetracker.Model.LoginUser.LoginUserInputModel
import com.learning.expencetracker.Model.LoginUser.LoginUserOutputModel
import com.learning.expencetracker.Model.ResendVerificationOTP.ResendVerificationCodeInputModel
import com.learning.expencetracker.Model.ResendVerificationOTP.ResendVerificationOTPOutputModel
import com.learning.expencetracker.Model.ResetPassword.ResetPasswordInputModel
import com.learning.expencetracker.Model.ResetPassword.ResetPasswordOutputModel
import com.learning.expencetracker.Model.SignUp.SignUpInputModel
import com.learning.expencetracker.Model.SignUp.SignUpOutputModel
import com.learning.expencetracker.Model.UpdateUser.UpdateUserInputModel
import com.learning.expencetracker.Model.UpdateUser.UpdateUserOutputModel
import com.learning.expencetracker.Model.VerifyOTP.VerifyOTPInputModel
import com.learning.expencetracker.Model.VerifyOTP.VerifyOTPOutputModel
import com.learning.expencetracker.Utils.Constants
import com.learning.expencetracker.Utils.RetrofitApis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthenticationModel : ViewModel() {

    // Sign up user
    var resultOfSignUpUser: MutableLiveData<SignUpOutputModel> = MutableLiveData()
    fun signUpUser(input: SignUpInputModel, context: Context, activity : SignUpActivity) {
        try {
            if (Constants.checkForInternet(context)) {
                val func = Constants.getInstance().create(RetrofitApis::class.java)
                viewModelScope.launch {
                    val result = func.signUp(input)
                    Log.d("rk",result.toString())
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful) {
                            resultOfSignUpUser.value = result.body()
                        } else {
                                val errorBody = result.errorBody()?.string()
                                val errorMessage = Constants.parseErrorMessage(errorBody)
                                activity.errorFn(errorMessage ?: "Unknown error")
                        }
                    }
                }
            } else {
                activity.errorFn("No internet connection")
            }
        } catch (err: Exception) {
            Log.e("rk", "Exception occurred during sign up: ${err.message}")
        }
    }
    fun observerForSignUpUser(): LiveData<SignUpOutputModel> = resultOfSignUpUser


    // verify email
    var resultOfVerifyOTPUser: MutableLiveData<VerifyOTPOutputModel> = MutableLiveData()
    fun verifyOTP(input: VerifyOTPInputModel, context: Context, activity : Activity) {
        try {
            if (Constants.checkForInternet(context)) {
                val func = Constants.getInstance().create(RetrofitApis::class.java)
                viewModelScope.launch {
                    val result = func.verifyOTP(input)
                    Log.d("rk",result.toString())
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful) {
                            resultOfVerifyOTPUser.value = result.body()
                        } else {
                            val errorBody = result.errorBody()?.string()
                            val errorMessage = Constants.parseErrorMessage(errorBody)
                            when(activity)
                            {
                                is SignUpActivity->{
                                    activity.errorFn(errorMessage ?: "Unknown error")
                                }
                                is SignInActivity ->{
                                    activity.errorFn(errorMessage ?: "Unknown error")
                                }
                            }
                        }
                    }
                }
            } else {
                when(activity)
                {
                    is SignUpActivity->{
                        activity.errorFn("No internet connection")
                    }
                    is SignInActivity ->{
                        activity.errorFn("No internet connection")
                    }
                }
            }
        } catch (err: Exception) {
            Log.e("rk", "Exception occurred during sign up: ${err.message}")
        }
    }
    fun observerForVerifyOTPUser(): LiveData<VerifyOTPOutputModel> = resultOfVerifyOTPUser

    // resend verification code
    var resultOfResendVerificationOTP: MutableLiveData<ResendVerificationOTPOutputModel> = MutableLiveData()
    fun resendVerificationOTP(input: ResendVerificationCodeInputModel, context: Context, activity : Activity) {
        try {
            if (Constants.checkForInternet(context)) {
                val func = Constants.getInstance().create(RetrofitApis::class.java)
                viewModelScope.launch {
                    val result = func.resendVerificationCode(input)
                    Log.d("rk",result.toString())
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful) {
                            resultOfResendVerificationOTP.value = result.body()
                        } else {
                            val errorBody = result.errorBody()?.string()
                            val errorMessage = Constants.parseErrorMessage(errorBody)

                            when(activity)
                            {
                                is SignUpActivity->{
                                    activity.errorFn(errorMessage ?: "Unknown error")
                                }
                                is SignInActivity ->{
                                    activity.errorFn(errorMessage ?: "Unknown error")
                                }
                            }

                        }
                    }
                }
            } else {
                when(activity)
                {
                    is SignUpActivity->{
                        activity.errorFn("No internet connection")
                    }
                    is SignInActivity ->{
                        activity.errorFn("No internet connection")
                    }
                }
            }
        } catch (err: Exception) {
            Log.e("rk", "Exception occurred during sign up: ${err.message}")
        }
    }
    fun observerForResendVerificationOTP(): LiveData<ResendVerificationOTPOutputModel> = resultOfResendVerificationOTP


    // login User
    var resultOfLoginUser: MutableLiveData<LoginUserOutputModel> = MutableLiveData()
    fun loginUser(input: LoginUserInputModel, context: Context, activity : SignInActivity) {
        try {
            if (Constants.checkForInternet(context)) {
                val func = Constants.getInstance().create(RetrofitApis::class.java)
                viewModelScope.launch {
                    val result = func.loginUser(input)
                    Log.d("rk",result.toString())
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful) {
                            resultOfLoginUser.value = result.body()
                        } else {
                            val errorBody = result.errorBody()?.string()
                            val errorMessage = Constants.parseErrorMessage(errorBody)
                            activity.errorFn(errorMessage ?: "Unknown error")

                        }
                    }
                }
            } else {
                activity.errorFn("No internet connection")
            }
        } catch (err: Exception) {
            Log.e("rk", "Exception occurred during sign up: ${err.message}")
        }
    }
    fun observerForLoginUser(): LiveData<LoginUserOutputModel> = resultOfLoginUser

    // forgotten password
    var resultOfForgoteenPassword: MutableLiveData<ForgottenPasswordOutputModel> = MutableLiveData()
    fun forgottenPassword(input: ForgottenPasswordInputModel, context: Context, activity : SignInActivity) {
        try {
            if (Constants.checkForInternet(context)) {
                val func = Constants.getInstance().create(RetrofitApis::class.java)
                viewModelScope.launch {
                    val result = func.forgottenPassword(input)
                    Log.d("rk",result.toString())
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful) {
                            resultOfForgoteenPassword.value = result.body()
                        } else {
                            val errorBody = result.errorBody()?.string()
                            val errorMessage = Constants.parseErrorMessage(errorBody)
                            activity.errorFn(errorMessage ?: "Unknown error")

                        }
                    }
                }
            } else {
                activity.errorFn("No internet connection")
            }
        } catch (err: Exception) {
            Log.e("rk", "Exception occurred during sign up: ${err.message}")
        }
    }
    fun observerForForgottenPassword(): LiveData<ForgottenPasswordOutputModel> = resultOfForgoteenPassword

    // reset password
    var resultOfResetPassword: MutableLiveData<ResetPasswordOutputModel> = MutableLiveData()
    fun resetPassword(input: ResetPasswordInputModel, context: Context, activity : SignInActivity) {
        try {
            if (Constants.checkForInternet(context)) {
                val func = Constants.getInstance().create(RetrofitApis::class.java)
                viewModelScope.launch {
                    val result = func.resetPassword(input)
                    Log.d("rk",result.toString())
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful) {
                            resultOfResetPassword.value = result.body()
                        } else {
                            val errorBody = result.errorBody()?.string()
                            val errorMessage = Constants.parseErrorMessage(errorBody)
                            activity.errorFn(errorMessage ?: "Unknown error")

                        }
                    }
                }
            } else {
                activity.errorFn("No internet connection")
            }
        } catch (err: Exception) {
            Log.e("rk", "Exception occurred during sign up: ${err.message}")
        }
    }
    fun observerForResetPassword(): LiveData<ResetPasswordOutputModel> = resultOfResetPassword

    // change password
    var resultOfChangePassword: MutableLiveData<ChangePasswordOutputModel> = MutableLiveData()
    fun changePassword(input: ChangePasswordInputModel, token: String, activity : ProfileActivity) {
        try {
            if (Constants.checkForInternet(activity)) {
                val func = Constants.getInstance().create(RetrofitApis::class.java)
                viewModelScope.launch {
                    val result = func.changePassword(token,input)
                    Log.d("rk",result.toString())
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful) {
                            resultOfChangePassword.value = result.body()
                        } else {
                            val errorBody = result.errorBody()?.string()
                            val errorMessage = Constants.parseErrorMessage(errorBody)
                            activity.errorFn(errorMessage ?: "Unknown error")

                        }
                    }
                }
            } else {
                activity.errorFn("No internet connection")
            }
        } catch (err: Exception) {
            Log.e("rk", "Exception occurred during sign up: ${err.message}")
        }
    }
    fun observerForChangePassword(): LiveData<ChangePasswordOutputModel> = resultOfChangePassword


    var resultOfUser: MutableLiveData<UpdateUserOutputModel> = MutableLiveData()
    fun updateUser(token: String, activity : SignInActivity, input:UpdateUserInputModel) {
        try {
            if (Constants.checkForInternet(activity)) {
                val func = Constants.getInstance().create(RetrofitApis::class.java)
                viewModelScope.launch {
                    val result = func.updateMe(token,input)
                    Log.d("rk",result.toString())
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful) {
                            resultOfUser.value = result.body()
                            Log.d("rk",result.body().toString())
                        } else {
                            val errorBody = result.errorBody()?.string()
                            val errorMessage = Constants.parseErrorMessage(errorBody)
                            Log.d("rk",errorMessage.toString())
                            activity.errorFn(errorMessage ?: "Unknown error")

                        }
                    }
                }
            } else {
                activity.errorFn("No internet connection")
            }
        } catch (err: Exception) {
            Log.e("rk", "Exception occurred during sign up: ${err.message}")
        }
    }
    fun observerForUser(): LiveData<UpdateUserOutputModel> = resultOfUser
}