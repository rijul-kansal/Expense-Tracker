package com.learning.expencetracker.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.learning.expencetracker.Activity.SignInActivity
import com.learning.expencetracker.Model.UpdateUser.UpdateUserInputModel

class FireBaseViewModel : ViewModel() {

    fun generateToken(normalToken:String,activity:SignInActivity){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            val token = task.result
            Log.d("rk",token)

            AuthenticationModel().updateUser("Bearer $normalToken",activity, UpdateUserInputModel(fcmToken = token))
        })
    }
}