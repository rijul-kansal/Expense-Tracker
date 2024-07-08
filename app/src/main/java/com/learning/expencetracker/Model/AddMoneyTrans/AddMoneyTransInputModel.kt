package com.learning.expencetracker.Model.AddMoneyTrans

data class AddMoneyTransInputModel(
    var amount: String? = null, // 29
    var category: String? = null, // Food
    var description: String? = null, // AA
    var moneyType: String? = null // In
)