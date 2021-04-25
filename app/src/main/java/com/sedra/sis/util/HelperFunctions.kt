package com.sedra.sis.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sedra.sis.data.model.User

fun getUserFromString(stringUser: String) : User?{
        val gson = Gson()
        if (stringUser.isEmpty()) return null
        val type = object : TypeToken<User>() {}.type
        return gson.fromJson(stringUser, type)
}