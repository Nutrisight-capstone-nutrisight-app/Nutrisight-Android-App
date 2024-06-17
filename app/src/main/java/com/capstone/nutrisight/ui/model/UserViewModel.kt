package com.capstone.nutrisight.ui.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.nutrisight.data.response.UserResponse
import com.capstone.nutrisight.repository.RegisterLoginRepository
import kotlinx.coroutines.launch

class UserViewModel(private val registerLoginRepository: RegisterLoginRepository): ViewModel() {
    private val _userResponse = MutableLiveData<UserResponse>()
    val userResponse: LiveData<UserResponse> = _userResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

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

}