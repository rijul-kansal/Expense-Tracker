package com.learning.expencetracker.Model.GetTransFilters

data class GetTransFilterOutputModel(
    var `data`: Data? = Data(),
    var length: Int? = 0, // 5
    var status: String? = "" // success
)