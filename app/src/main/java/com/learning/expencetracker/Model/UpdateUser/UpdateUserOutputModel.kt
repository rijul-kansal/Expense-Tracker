package com.learning.expencetracker.Model.UpdateUser

data class UpdateUserOutputModel(
    var `data`: Data? = Data(),
    var status: String? = "" // success
)