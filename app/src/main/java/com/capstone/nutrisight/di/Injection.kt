package com.capstone.nutrisight.di

import android.content.Context
import com.capstone.nutrisight.data.api.config.ClassificationApiConfig
import com.capstone.nutrisight.data.api.config.RegisterLoginApiConfig
import com.capstone.nutrisight.preferences.SettingsPreferences
import com.capstone.nutrisight.preferences.dataStore
import com.capstone.nutrisight.repository.ClassificationRepository
import com.capstone.nutrisight.repository.RegisterLoginRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun registerLoginRepository(context: Context): RegisterLoginRepository {
        val pref = SettingsPreferences.getInstance(context.dataStore)
        val user = runBlocking { pref.getUser().first() }
        val apiService = if (user != null) {
            RegisterLoginApiConfig.getApiService(user.accessToken)
        } else {
            RegisterLoginApiConfig.getApiService("")
        }

        return RegisterLoginRepository.getInstance(apiService, pref)
    }

    fun provideClassificationRepository(): ClassificationRepository {
        val apiService = ClassificationApiConfig.getApiService()
        return ClassificationRepository.getInstance(apiService)
    }
}