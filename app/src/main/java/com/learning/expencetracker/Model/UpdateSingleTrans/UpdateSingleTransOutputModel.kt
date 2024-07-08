package com.learning.expencetracker.Model.UpdateSingleTrans

data class UpdateSingleTransOutputModel(
    var `data`: Data? = Data(),
    var status: String? = "" ,
    var message:String?// success
)