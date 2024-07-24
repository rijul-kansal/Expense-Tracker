package com.learning.expencetracker.ViewModel

import android.app.ProgressDialog
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.learning.expencetracker.Activity.ProfileActivity
import com.learning.expencetracker.Activity.SignInActivity
import com.learning.expencetracker.Model.UpdateUser.UpdateUserInputModel
import java.util.UUID


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


    var storage: FirebaseStorage? = null
    var storageReference: StorageReference? = null

     fun uploadImage(filePath: Uri,token:String,activity:ProfileActivity) {
         storage = FirebaseStorage.getInstance()
         storageReference = storage!!.reference
         val ref = storageReference!!.child("images/" + UUID.randomUUID().toString())
         ref.putFile(filePath).addOnCompleteListener { task ->
             if (task.isSuccessful) {
                 ref.downloadUrl.addOnSuccessListener { uri ->
                     AuthenticationModel().updateUser("Bearer $token",activity, UpdateUserInputModel(imageUrl = uri.toString()))
                 }.addOnFailureListener { e ->

                 }
             }
         }.addOnFailureListener { e -> // Error, Image not uploaded
                 Log.d("rk",e.toString())
             }
     }
}