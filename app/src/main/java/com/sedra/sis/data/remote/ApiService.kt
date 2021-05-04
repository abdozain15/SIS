package com.sedra.sis.data.remote

import com.sedra.sis.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    companion object {
        const val BASE_URL = "http://mahmoude16.sg-host.com/"
    }


    @POST("/sisapp/api/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): BaseResponse<User>

    @POST("/sisapp/api/register")
    @FormUrlEncoded
    suspend fun register(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("name") name: String,
        @Field("gender") gender: String,
        @Field("age") age: Int,
        @Field("height") height: Int,
        @Field("weight") weight: Int,
    ): BaseResponse<User>

    @POST("/sisapp/api/editProfile")
    @FormUrlEncoded
    suspend fun editProfile(
        @Header("Authorization") authorization: String,
        @Field("id") id: Int,
        @Field("name") name: String,
        @Field("gender") gender: String,
        @Field("age") age: Int,
        @Field("height") height: Int,
        @Field("weight") weight: Int,
    ): BaseResponse<Int>

    @POST("/sisapp/api/workout")
    suspend fun getWorkouts(
        @Header("Authorization") authorization: String
    ): BaseResponse<List<Workout>>


    @POST("/sisapp/api/department")
    suspend fun getShoppingDepartments(
        @Header("Authorization") authorization: String
    ): BaseResponse<List<Department>>

    @POST("/sisapp/api/product")
    @FormUrlEncoded
    suspend fun getProduct(
        @Header("Authorization") authorization: String,
        @Field("id") id: Int,
    ): BaseResponse<Product>

    @POST("/sisapp/api/complaints")
    @FormUrlEncoded
    suspend fun sendOpinion(
        @Header("Authorization") authorization: String,
        @Field("user_id") id: Int,
        @Field("body") body: String,
    ): BaseResponse<Product>


    @POST("/sisapp/api/askQuestion")
    @FormUrlEncoded
    suspend fun askQuestions(
        @Header("Authorization") authorization: String,
        @Field("user_id") id: Int,
        @Field("req_body") body: String,
    ): BaseResponse<Product>

    @POST("/sisapp/api/search")
    @FormUrlEncoded
    suspend fun searchProducts(
        @Header("Authorization") authorization: String,
        @Field("name") name: String,
    ): BaseResponse<List<Product>>

    @POST("/sisapp/api/editUserImage")
    @Multipart
    suspend fun updateImage(
        @Header("Authorization") authorization: String,
        @Part("id") id: RequestBody,
        @Part image: MultipartBody.Part,
    ): UpdateImageResponse


    @POST("/sisapp/api/orders")
    suspend fun sendOrder(
        @Header("Authorization") authorization: String,
        @Body cart: CartRequest,
    ): BaseResponse<List<Product>>


}