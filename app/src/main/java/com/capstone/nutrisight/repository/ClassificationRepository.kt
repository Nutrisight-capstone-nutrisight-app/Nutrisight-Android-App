package com.capstone.nutrisight.repository

import com.capstone.nutrisight.data.api.service.ClassificationApiService
import com.capstone.nutrisight.data.response.ClassificationResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class ClassificationRepository private constructor(
    private val classificationApiService: ClassificationApiService
){

    suspend fun upload(image: MultipartBody.Part): ClassificationResponse {
        return withContext(Dispatchers.IO) {
            classificationApiService.uploadImage(image)
        }
    }

    companion object {
        fun getInstance(
            classificationApiService: ClassificationApiService
        ): ClassificationRepository = ClassificationRepository(
            classificationApiService
        )
    }


}