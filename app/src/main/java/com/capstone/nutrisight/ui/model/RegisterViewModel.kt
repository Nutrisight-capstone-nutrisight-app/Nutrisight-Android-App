package com.capstone.nutrisight.ui.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.nutrisight.R
import com.capstone.nutrisight.data.response.RegisterDeleteResponse
import com.capstone.nutrisight.repository.RegisterLoginRepository
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

class RegisterViewModel(
    private val registerLoginRepository: RegisterLoginRepository
) : ViewModel() {

    private val _registerResponse = MutableLiveData<RegisterDeleteResponse>()
    val registerResponse: LiveData<RegisterDeleteResponse> = _registerResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = registerLoginRepository.register(username, email, password)
                _registerResponse.value = response
            } catch (e: Exception) {
                val errorResponse = parseErrorResponse(e)
                _registerResponse.value = RegisterDeleteResponse(message = null, error = errorResponse)
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