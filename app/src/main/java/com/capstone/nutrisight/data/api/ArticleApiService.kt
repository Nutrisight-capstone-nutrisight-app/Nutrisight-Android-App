package com.capstone.nutrisight.data.api

import com.capstone.nutrisight.BuildConfig
import com.capstone.nutrisight.data.response.ArticleResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticleApiService {

    @GET("v2/everything")
    fun getArticles(
        @Query("q") query: String = "Healthy Food",
        @Query("language") language: String = "en",
        @Query("apiKey") api: String = ApiConstant.NEWS_API_KEY
    ): Call<ArticleResponse>
}