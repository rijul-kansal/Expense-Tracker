package com.learning.expencetracker.Model.GetDataBasedOnCategory

data class GetDataBasedOnCatOutputModel(
    var `data`: Data? = Data(),
    var status: String? = "" // success
)