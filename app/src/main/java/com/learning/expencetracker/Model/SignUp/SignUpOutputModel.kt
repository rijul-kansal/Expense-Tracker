package com.learning.expencetracker.Model.SignUp

data class SignUpOutputModel(
    var `data`: Data? = Data(),
    var message: String? = "", // Email has been send please verify your email id
    var status: String? = "" // success
)