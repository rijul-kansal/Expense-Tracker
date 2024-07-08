package com.learning.expencetracker.Model.SignUp

data class SignUpInputModel(
    var email: String? = null, // c@gmail.com
    var mobileNumber: String? = null, // 8059364551
    var name: String? = null, // rijul
    var password: String? = null // 12345678
)