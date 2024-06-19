package com.capstone.nutrisight.ui.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.nutrisight.data.response.DetailProduct
import com.capstone.nutrisight.data.response.DetailResponse
import com.capstone.nutrisight.repository.RegisterLoginRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val registerLoginRepository: RegisterLoginRepository): ViewModel() {
    private val _detailProduct = MutableLiveData<DetailResponse>()
    val detailProduct: MutableLiveData<DetailResponse> = _detailProduct

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getDetailProduct(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = registerLoginRepository.getDetailProduct(id)
                Log.d("DetailViewModel", id)
                _detailProduct.value = response
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
            }


        }
    }

}