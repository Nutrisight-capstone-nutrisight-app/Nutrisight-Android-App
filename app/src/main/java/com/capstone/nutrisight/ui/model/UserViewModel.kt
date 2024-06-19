package com.capstone.nutrisight.ui.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.nutrisight.data.response.MessageResponse
import com.capstone.nutrisight.data.response.UserResponse
import com.capstone.nutrisight.repository.RegisterLoginRepository
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

class UserViewModel(private val registerLoginRepository: RegisterLoginRepository): ViewModel() {
    private val _userResponse = MutableLiveData<UserResponse>()
    val userResponse: LiveData<UserResponse> = _userResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<MessageResponse>()
    val message: LiveData<MessageResponse> = _message


    fun getUser() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = registerLoginRepository.getUser()
                _userResponse.value = response
            } catch (e: Exception) {
                _isLoading.value = false
            }
        }
    }

    fun updateUser(name: String, email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = registerLoginRepository.updateUser(name, email, password)
                _message.value = response
            } catch (e: Exception) {
                _isLoading.value = false
                parseErrorResponse(e)
                _message.value = MessageResponse(message = e.message.toString())
            }
        }
    }

    fun deleteUser() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = registerLoginRepository.deleteUser()
                _message.value = response
            } catch (e: Exception) {
                _isLoading.value = false
                parseErrorResponse(e)
                _message.value = MessageResponse(message = e.message.toString())
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