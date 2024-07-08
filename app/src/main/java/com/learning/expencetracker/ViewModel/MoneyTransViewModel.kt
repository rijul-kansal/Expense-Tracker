package com.learning.expencetracker.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.expencetracker.Activity.AllTransDisplayActivity
import com.learning.expencetracker.Model.AddMoneyTrans.AddMoneyTransInputModel
import com.learning.expencetracker.Model.AddMoneyTrans.AddMoneyTransOutputModel
import com.learning.expencetracker.Model.DeleteTrans.DeleteTransOutputModel
import com.learning.expencetracker.Model.GetAllTrans.GetAllTransOutputModel
import com.learning.expencetracker.Model.UpdateSingleTrans.UpdateSingleTransInputModel
import com.learning.expencetracker.Model.UpdateSingleTrans.UpdateSingleTransOutputModel
import com.learning.expencetracker.Utils.Constants
import com.learning.expencetracker.Utils.RetrofitApis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoneyTransViewModel : ViewModel() {
    // add new transaction
    var resultOfCreateNewTrans: MutableLiveData<AddMoneyTransOutputModel> = MutableLiveData()
    fun addNewTrans(
        input: AddMoneyTransInputModel,
        activity:AllTransDisplayActivity,
        token: String,
        id:String
    ) {
        try {
            if (Constants.checkForInternet(activity)) {
                val func = Constants.getInstance().create(RetrofitApis::class.java)
                viewModelScope.launch {
                    val result = func.addMoneyTrans(token,id,input)
                    Log.d("rk", result.toString())
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful) {
                            resultOfCreateNewTrans.value = result.body()
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
    fun observerForAddNewTrans(): LiveData<AddMoneyTransOutputModel> = resultOfCreateNewTrans
    fun clearCreateNewBook() {
        resultOfCreateNewTrans.value = null
    }

   // get all trans
    var resultOfGetAllTrans: MutableLiveData<GetAllTransOutputModel> = MutableLiveData()
    fun getAllTrans(
        activity:AllTransDisplayActivity,
        token: String,
        id:String
    ) {
        try {
            if (Constants.checkForInternet(activity)) {
                val func = Constants.getInstance().create(RetrofitApis::class.java)
                viewModelScope.launch {
                    val result = func.getAllTrans(token,id)
                    Log.d("rk", result.toString())
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful) {
                            resultOfGetAllTrans.value = result.body()
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
    fun observerForGetAllTrans(): LiveData<GetAllTransOutputModel> = resultOfGetAllTrans
    fun clearGetAllTrans() {
        resultOfGetAllTrans.value = null
    }

    // updateTrans
    var resultOfUpdateSingleTrans: MutableLiveData<UpdateSingleTransOutputModel> = MutableLiveData()
    fun updateSingleTrans(
        activity:AllTransDisplayActivity,
        token: String,
        id:String,
        input:UpdateSingleTransInputModel
    ) {
        try {
            if (Constants.checkForInternet(activity)) {
                val func = Constants.getInstance().create(RetrofitApis::class.java)
                viewModelScope.launch {
                    val result = func.updateSingleTrans(token,id,input)
                    Log.d("rk", result.toString())
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful) {
                            resultOfUpdateSingleTrans.value = result.body()
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
    fun observerForUpdateSingleTrans(): LiveData<UpdateSingleTransOutputModel> = resultOfUpdateSingleTrans
    fun clearUpdateSingleTrans() {
        resultOfUpdateSingleTrans.value = null
    }


    // delete single  trans
    var resultOfdeleteTrans: MutableLiveData<DeleteTransOutputModel> = MutableLiveData()
    fun deleteTrans(
        activity:AllTransDisplayActivity,
        token: String,
        id:String
    ) {
        try {
            if (Constants.checkForInternet(activity)) {
                val func = Constants.getInstance().create(RetrofitApis::class.java)
                viewModelScope.launch {
                    val result = func.deleteSingleTrans(token,id)
                    Log.d("rk", result.toString())
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful) {
                            resultOfdeleteTrans.value = result.body()
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
    fun observerForDeleteTrans(): LiveData<DeleteTransOutputModel> = resultOfdeleteTrans
    fun clearDeleteTrans() {
        resultOfdeleteTrans.value = null
    }

}