package com.capstone.nutrisight.ui.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.nutrisight.R
import com.capstone.nutrisight.data.response.LoginResponse
import com.capstone.nutrisight.repository.RegisterLoginRepository
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

class LoginViewModel(private val registerLoginRepository: RegisterLoginRepository) : ViewModel() {
    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = registerLoginRepository.login(email, password)
                _loginResponse.value = response
            } catch (e: Exception) {
                val errorResponse = parseErrorResponse(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun parseErrorResponse(exception: Exception): String {
        return try {
            val errorBody = (exception as? HttpException)?.response()?.errorBody()?.string()
            val errorJson = errorBody?.let { JSONObject(it) }
            errorJson!!.getString("message")
        } catch (ex: Exception) {
            "Unknown error occurred"
        }
    }
}