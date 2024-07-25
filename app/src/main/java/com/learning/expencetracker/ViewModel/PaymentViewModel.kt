package com.learning.expencetracker.ViewModel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.expencetracker.Activity.MainActivity
import com.learning.expencetracker.Activity.PaymentActivity
import com.learning.expencetracker.Activity.PaymentHistoryActivity
import com.learning.expencetracker.Activity.ProfileActivity
import com.learning.expencetracker.Activity.SignInActivity
import com.learning.expencetracker.Activity.SplashActivity
import com.learning.expencetracker.Model.KeysModel.GetKeysOutputModel
import com.learning.expencetracker.Model.PaymentHistory.PaymentHistoryOutputModel
import com.learning.expencetracker.Model.UpdateUser.UpdateUserInputModel
import com.learning.expencetracker.Model.UpdateUser.UpdateUserOutputModel
import com.learning.expencetracker.Model.VerifyTransAndAddToDB.VerifyTransAndAddToDBInputModel
import com.learning.expencetracker.Model.VerifyTransAndAddToDB.VerifyTransIdAndAddToDBOutputModel
import com.learning.expencetracker.Utils.Constants
import com.learning.expencetracker.Utils.RetrofitApis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PaymentViewModel : ViewModel() {

    var resultOfGetKeys: MutableLiveData<GetKeysOutputModel> = MutableLiveData()
    fun getKeys(token: String, activity : Activity, amount:String) {
        try {
            if (Constants.checkForInternet(activity)) {
                val func = Constants.getInstance().create(RetrofitApis::class.java)
                viewModelScope.launch {
                    val result = func.getKeys(token, amount)
                    Log.d("rk",result.toString())
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful) {
                            resultOfGetKeys.value = result.body()
                            Log.d("rk",result.body().toString())
                        } else {
                            val errorBody = result.errorBody()?.string()
                            val errorMessage = Constants.parseErrorMessage(errorBody)
                            Log.d("rk",errorMessage.toString())
                            when(activity)
                            {
                                is PaymentActivity ->{
                                    activity.errorFn(errorMessage ?: "Unknown error")
                                }
                            }

                        }
                    }
                }
            } else {
                when(activity)
                {
                    is PaymentActivity ->{
                        activity.errorFn("No internet connection")
                    }
                }
            }
        } catch (err: Exception) {
            Log.e("rk", "Exception occurred during sign up: ${err.message}")
        }
    }
    fun observerForGetKeys(): LiveData<GetKeysOutputModel> = resultOfGetKeys


    var resultOfVerifyTransAndAddToDB: MutableLiveData<VerifyTransIdAndAddToDBOutputModel> = MutableLiveData()
    fun verifyTransAndAddToDB(token: String, activity : Activity, input:VerifyTransAndAddToDBInputModel) {
        try {
            if (Constants.checkForInternet(activity)) {
                val func = Constants.getInstance().create(RetrofitApis::class.java)
                viewModelScope.launch {
                    val result = func.verifyAndAddToDB(token, input)
                    Log.d("rk",result.toString())
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful) {
                            resultOfVerifyTransAndAddToDB.value = result.body()
                            Log.d("rk",result.body().toString())
                        } else {
                            val errorBody = result.errorBody()?.string()
                            val errorMessage = Constants.parseErrorMessage(errorBody)
                            Log.d("rk",errorMessage.toString())
                            when(activity)
                            {
                                is PaymentActivity ->{
                                    activity.errorFn(errorMessage ?: "Unknown error")
                                }
                            }

                        }
                    }
                }
            } else {
                when(activity)
                {
                    is PaymentActivity ->{
                        activity.errorFn("No internet connection")
                    }
                }
            }
        } catch (err: Exception) {
            Log.e("rk", "Exception occurred during sign up: ${err.message}")
        }
    }
    fun observerForVerifyTransAndAddToDB(): LiveData<VerifyTransIdAndAddToDBOutputModel> = resultOfVerifyTransAndAddToDB


    var resultOfPaymentHistory: MutableLiveData<PaymentHistoryOutputModel> = MutableLiveData()
    fun paymentHistory(token: String, activity : Activity) {
        try {
            if (Constants.checkForInternet(activity)) {
                val func = Constants.getInstance().create(RetrofitApis::class.java)
                viewModelScope.launch {
                    val result = func.paymentHistory(token)
                    Log.d("rk",result.toString())
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful) {
                            resultOfPaymentHistory.value = result.body()
                            Log.d("rk",result.body().toString())
                        } else {
                            val errorBody = result.errorBody()?.string()
                            val errorMessage = Constants.parseErrorMessage(errorBody)
                            Log.d("rk",errorMessage.toString())
                            when(activity)
                            {
                                is PaymentHistoryActivity ->{
                                    activity.errorFn(errorMessage ?: "Unknown error")
                                }
                                is MainActivity ->{
                                    activity.errorFn(errorMessage ?: "Unknown error")
                                }
                            }

                        }
                    }
                }
            } else {
                when(activity)
                {
                    is PaymentHistoryActivity ->{
                        activity.errorFn("No internet connection")
                    }
                    is MainActivity ->{
                        activity.errorFn("No internet connection")
                    }
                }
            }
        } catch (err: Exception) {
            Log.e("rk", "Exception occurred during sign up: ${err.message}")
        }
    }
    fun observerForPaymentHistory(): LiveData<PaymentHistoryOutputModel> = resultOfPaymentHistory
}