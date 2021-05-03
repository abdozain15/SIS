package com.sedra.sis.data

import com.sedra.sis.data.model.CartRequest
import com.sedra.sis.data.remote.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
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


    suspend fun editProfile(
        auth: String, id: Int, name: String, gender: String, age: Int,
        height: Int, weight: Int
    ) =
        service.editProfile(auth, id, name, gender, age, height, weight)

    suspend fun getWorkouts(auth: String) =
        service.getWorkouts(auth)

    suspend fun getShoppingDepartments(auth: String) =
        service.getShoppingDepartments(auth)

    suspend fun getProduct(auth: String, id: Int) =
        service.getProduct(auth, id)

    suspend fun searchProducts(auth: String, name: String) =
        service.searchProducts(auth, name)

    suspend fun askQuestions(auth: String, id: Int, body: String) =
        service.askQuestions(auth, id, body)


    suspend fun updateImage(auth: String, id: RequestBody, body: MultipartBody.Part) =
        service.updateImage(auth, id, body)

    suspend fun sendOpinion(auth: String, id: Int, body: String) =
        service.sendOpinion(auth, id, body)

    suspend fun sendOrder(auth: String, request: CartRequest) =
        service.sendOrder(auth, request)
}