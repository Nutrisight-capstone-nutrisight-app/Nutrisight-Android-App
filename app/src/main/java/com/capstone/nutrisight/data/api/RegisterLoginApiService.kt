package com.capstone.nutrisight.data.api

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
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterDeleteResponse

    @FormUrlEncoded
    @POST("/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @DELETE("/logout")
    suspend fun logout(): RegisterDeleteResponse

}