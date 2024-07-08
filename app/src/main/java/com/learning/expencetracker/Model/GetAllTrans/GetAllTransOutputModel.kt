package com.learning.expencetracker.Model.GetAllTrans

data class GetAllTransOutputModel(
    var `data`: Data? = Data(),
    var length: Int? = 0, // 17
    var status: String? = "", // success
    var totalBalance: List<TotalBalance>? = listOf()
)