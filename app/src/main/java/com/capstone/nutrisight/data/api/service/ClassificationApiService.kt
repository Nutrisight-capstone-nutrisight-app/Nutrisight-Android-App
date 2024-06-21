package com.capstone.nutrisight.data.api.service

import com.capstone.nutrisight.data.response.ClassificationResponse
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ClassificationApiService {

    @GET("")
    suspend fun getClassification(): ClassificationResponse

    @Multipart
    @POST("/predict")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): ClassificationResponse

}