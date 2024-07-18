package com.learning.expencetracker.Model.UpdateUser

data class UpdateUserInputModel(
    var fcmToken: String? = null ,
    var name: String? = null ,
    var mobileNumber: String? = null ,
    var imageUrl: String? = null
)