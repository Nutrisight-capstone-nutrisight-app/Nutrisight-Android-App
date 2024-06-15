package com.capstone.nutrisight.ui.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.nutrisight.data.api.config.ArticleApiConfig
import com.capstone.nutrisight.data.response.ArticleResponse
import com.capstone.nutrisight.data.response.ArticlesItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticleViewModel: ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _articlesItems = MutableLiveData<List<ArticlesItem>?>()
    val articlesItem: LiveData<List<ArticlesItem>?> = _articlesItems

    fun setArticlesItems(query: String, language: String, apiKey: String) {
        _isLoading.value = true
        val client = ArticleApiConfig.getApiService().getArticles(query, language, apiKey)
        client.enqueue(object : Callback<ArticleResponse> {
            override fun onResponse(
                call: Call<ArticleResponse>,
                response: Response<ArticleResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val article = response.body()?.articles
                    _articlesItems.value = article
                }
            }

            override fun onFailure(call: Call<ArticleResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("responseError", "onFailure: ${t.message}")
            }

        })
    }
}