package com.learning.expencetracker.Model.GetBooks

data class GetBooksOutputModel(
    var `data`: Data? = Data(),
    var status: String? = "" ,
    var  message : String?= null
)