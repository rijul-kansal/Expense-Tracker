package com.learning.expencetracker.Model

data class   BookNamesDisplayModel(
    var name: String? = null,
    var _id: String? = null,
    var updatedLast: Long? = null,
    var userId: List<String?>? =null
)