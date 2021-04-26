package com.sedra.sis.data.remote

import com.sedra.sis.data.model.BaseResponse
import com.sedra.sis.data.model.User
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    companion object{
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
}