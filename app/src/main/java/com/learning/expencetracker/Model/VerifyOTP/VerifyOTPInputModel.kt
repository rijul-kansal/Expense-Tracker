package com.learning.expencetracker.Model.VerifyOTP

data class VerifyOTPInputModel(
    var email: String? = null, // c@gmail.com
    var otp: String? = null // 887203
)