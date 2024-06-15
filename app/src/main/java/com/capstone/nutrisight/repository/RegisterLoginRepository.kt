package com.capstone.nutrisight.repository

import com.capstone.nutrisight.data.api.service.RegisterLoginApiService
import com.capstone.nutrisight.data.response.LoginResponse
import com.capstone.nutrisight.data.response.RegisterDeleteResponse
import com.capstone.nutrisight.preferences.SettingsPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RegisterLoginRepository private constructor(
    private val registerLoginApiService: RegisterLoginApiService,
    private val settingsPreferences: SettingsPreferences
){


    suspend fun register(username: String, email: String, password: String): RegisterDeleteResponse {
        return withContext(Dispatchers.IO) {
            registerLoginApiService.register(username, email, password)
        }
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return withContext(Dispatchers.IO) {
            registerLoginApiService.login(email, password)
        }
    }

    suspend fun logout(): RegisterDeleteResponse {
        return withContext(Dispatchers.IO) {
            registerLoginApiService.logout()
        }
    }

    companion object {
        fun getInstance(
            registerLoginApiService: RegisterLoginApiService,
            settingsPreferences: SettingsPreferences
        ): RegisterLoginRepository = RegisterLoginRepository(
            registerLoginApiService,
            settingsPreferences
        )
    }

}