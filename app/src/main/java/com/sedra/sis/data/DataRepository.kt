package com.sedra.sis.data

import com.sedra.sis.data.remote.ApiService
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val service: ApiService
) {

    suspend fun login(email: String, password: String) =
            service.login(email, password)


    suspend fun register(
        email: String, password: String, name: String, gender: String, age: Int,
        height: Int, weight: Int
    ) =
        service.register(email, password, name, gender, age, height, weight)

    suspend fun getWorkouts(auth: String) =
        service.getWorkouts(auth)

    suspend fun getShoppingDepartments(auth: String) =
        service.getShoppingDepartments(auth)

    suspend fun getProduct(auth: String, id: Int) =
        service.getProduct(auth, id)

    suspend fun searchProducts(auth: String, name: String) =
        service.searchProducts(auth, name)
}