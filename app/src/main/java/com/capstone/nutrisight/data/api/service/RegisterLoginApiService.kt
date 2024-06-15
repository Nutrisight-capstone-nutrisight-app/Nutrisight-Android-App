package com.capstone.nutrisight.data.api.service

import com.capstone.nutrisight.data.response.LoginResponse
import com.capstone.nutrisight.data.response.RegisterDeleteResponse
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RegisterLoginApiService {

    @FormUrlEncoded
    @POST("/register")
    suspend fun register(
        @Field("username") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterDeleteResponse

    @FormUrlEncoded
    @POST("/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @DELETE("/logout")
    suspend fun logout(): RegisterDeleteResponse

}