package com.learning.expencetracker.Model.ResendVerificationOTP

data class ResendVerificationOTPOutputModel(
    var message: String? = null, // Please enter correct email address
    var status: String? = null // error
)