package com.capstone.nutrisight.data.api.service

import com.capstone.nutrisight.data.response.User
import com.capstone.nutrisight.data.response.UserResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface UserApiService {

    @GET("/user/{user_id}")
    fun getUser(
        @Path("user_id") userId: String
    ): UserResponse

    @PATCH("/user/{user_id}")
    fun updateUser(
        @Path("user_id") userId: String,
        @Body user: User
    ): UserResponse

    @DELETE("/user/{user_id}")
    fun deleteUser(
        @Path("user_id") userId: String
    ): UserResponse


}