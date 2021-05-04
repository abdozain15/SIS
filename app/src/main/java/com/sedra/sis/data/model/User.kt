package com.sedra.sis.data.model

data class User(
    var age: Int,
    val api_token: String,
    val created_at: String,
    val email: String,
    val email_verified_at: Any,
    var gender: String,
    var height: Int,
    val id: Int,
    val join_date: Any,
    var name: String,
    val updated_at: String,
    val user_name: String,
    var image: String?,
    var weight: Int
)