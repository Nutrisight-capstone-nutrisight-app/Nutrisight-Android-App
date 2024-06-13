package com.capstone.nutrisight.ui.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.nutrisight.data.response.RegisterDeleteResponse
import com.capstone.nutrisight.repository.RegisterLoginRepository
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerLoginRepository: RegisterLoginRepository
): ViewModel() {

    private val _registerResponse = MutableLiveData<RegisterDeleteResponse>()
    val registerResponse: MutableLiveData<RegisterDeleteResponse> = _registerResponse

    private val _message = MutableLiveData<String>()
    val message: MutableLiveData<String> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> = _isLoading

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = registerLoginRepository.register(name, email, password)
                _registerResponse.value = response
                _message.value = response.message
                _isLoading.value = false
            } catch (e: Exception) {
                Log.d("RegisterViewModel", "register: ${e.message.toString()}")
                _isLoading.value = false
            }
        }
    }

}