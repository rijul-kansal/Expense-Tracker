package com.learning.expencetracker.Model.LoginUser

data class LoginUserOutputModel(
    var `data`: Data? = Data(),
    var message: String? = "", // successfully login
    var status: String? = "", // success
    var token: String? = "" // eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6ImFAZ21haWwuY29tIiwiaWF0IjoxNzE5NDc0MzkzLCJleHAiOjE3MjAzMzgzOTN9.y9j7aQHhwCyZj2WTBORCd8k1jrhtjh5EmfjA0HHcENk
)