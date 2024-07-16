package com.learning.expencetracker.Model.ChangePasswordModel

data class ChangePasswordInputModel(
    var newPassword: String? = null, // 12345671
    var oldPassword: String? = null // 12345678
)