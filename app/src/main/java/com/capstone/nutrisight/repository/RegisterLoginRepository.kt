package com.capstone.nutrisight.repository

import com.capstone.nutrisight.data.api.service.RegisterLoginApiService
import com.capstone.nutrisight.data.response.DetailResponse
import com.capstone.nutrisight.data.response.LoginResponse
import com.capstone.nutrisight.data.response.MessageResponse
import com.capstone.nutrisight.data.response.RegisterDeleteResponse
import com.capstone.nutrisight.data.response.SaveProductRequest
import com.capstone.nutrisight.data.response.SaveResponse
import com.capstone.nutrisight.data.response.SavedResponse
import com.capstone.nutrisight.data.response.UserResponse
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

    suspend fun getUser(): UserResponse {
        return withContext(Dispatchers.IO) {
            registerLoginApiService.getUser()
        }
    }

    suspend fun updateUser(email: String, username: String, password: String): MessageResponse {
        return withContext(Dispatchers.IO) {
            registerLoginApiService.updateUser(email, username, password)
        }
    }

    suspend fun deleteUser(): MessageResponse {
        return withContext(Dispatchers.IO) {
            registerLoginApiService.deleteUser()
        }
    }

    suspend fun uploadSavedProduct(request: SaveProductRequest): SaveResponse {
        return withContext(Dispatchers.IO) {
            registerLoginApiService.saveProduct(request)
        }
    }

    suspend fun getSavedProduct(): SavedResponse {
        return withContext(Dispatchers.IO) {
            registerLoginApiService.getSaved()
        }
    }

    suspend fun getDetailProduct(id: String): DetailResponse {
        return withContext(Dispatchers.IO) {
            registerLoginApiService.getDetail(id)
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