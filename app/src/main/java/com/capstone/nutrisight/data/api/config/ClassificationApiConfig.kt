package com.capstone.nutrisight.data.api.config

import com.capstone.nutrisight.data.api.ApiConstant
import com.capstone.nutrisight.data.api.service.ClassificationApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ClassificationApiConfig {
    companion object {
        fun getApiService(): ClassificationApiService {
            val authInterceptor = Interceptor { chain ->
                val req = chain.request()
                val requestHeaders = req.newBuilder()
                    .build()
                chain.proceed(requestHeaders)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(ApiConstant.ML_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(ClassificationApiService::class.java)
        }
    }
}