package com.learning.expencetracker.Model.VerifyTransAndAddToDB

data class VerifyTransIdAndAddToDBOutputModel(
    var message: Any? = null, // Trans Id is already exists
    var status: String? = null // fail
)