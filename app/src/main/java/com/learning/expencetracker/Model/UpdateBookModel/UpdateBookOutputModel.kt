package com.learning.expencetracker.Model.UpdateBookModel

data class UpdateBookOutputModel(
    var `data`: Data? = Data(),
    var status: String? = "" ,
    var message :String ? = ""
)