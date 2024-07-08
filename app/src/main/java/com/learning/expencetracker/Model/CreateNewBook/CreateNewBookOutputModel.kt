package com.learning.expencetracker.Model.CreateNewBook

data class CreateNewBookOutputModel(
    var `data`: Data? = Data(),
    var status: String? = "" // success
)