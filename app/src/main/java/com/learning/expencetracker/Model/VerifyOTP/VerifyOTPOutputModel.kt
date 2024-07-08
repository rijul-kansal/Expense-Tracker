package com.learning.expencetracker.Model.VerifyOTP

data class VerifyOTPOutputModel(
    var message: String? = null, // user verified successfully please login
    var status: String? = null // success
)