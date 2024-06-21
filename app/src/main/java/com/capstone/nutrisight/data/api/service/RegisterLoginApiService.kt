package com.capstone.nutrisight.data.api.service

import com.capstone.nutrisight.data.response.DetailResponse
import com.capstone.nutrisight.data.response.LoginResponse
import com.capstone.nutrisight.data.response.MessageResponse
import com.capstone.nutrisight.data.response.RegisterDeleteResponse
import com.capstone.nutrisight.data.response.SaveProductRequest
import com.capstone.nutrisight.data.response.SaveResponse
import com.capstone.nutrisight.data.response.SavedResponse
import com.capstone.nutrisight.data.response.UserResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface RegisterLoginApiService {

    // User auth
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

    // User configuration
    @GET("/user")
    suspend fun getUser(): UserResponse

    @FormUrlEncoded
    @PATCH("/user")
    suspend fun updateUser(
        @Field("email") email: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): MessageResponse

    @DELETE("/user")
    suspend fun deleteUser(): MessageResponse

    // Save product
    @POST("/save")
    suspend fun saveProduct(
        @Body request: SaveProductRequest
    ): SaveResponse

    @GET("/save")
    suspend fun getSaved(): SavedResponse

    @GET("/product/{id}")
    suspend fun getDetail(
        @Path("id") id: String
    ): DetailResponse


}