package com.learning.expencetracker.Model.ForgottenPassword

data class ForgottenPasswordOutputModel(
    var message: String? = null, // OTP has beed send on then registered email
    var status: String? = null // success
)