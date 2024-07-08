package com.learning.expencetracker.Model.UpdateBookModel

data class UpdateBookInputModel(
    var name: String? = null, // ABC
    var newUserId: String? = null, // c@gmail.com
    var removeUser: List<String?>? = null
)