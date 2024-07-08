package com.learning.expencetracker.Model.ResetPassword

data class ResetPasswordInputModel(
    var email: String? = null, // kansalrijul111@gmail.com
    var newPassword: String? = null, // 12345678
    var otp: String? = null // 540223
)