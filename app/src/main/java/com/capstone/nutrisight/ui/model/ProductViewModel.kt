package com.capstone.nutrisight.ui.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.nutrisight.data.response.Product
import com.capstone.nutrisight.data.response.ProductsOnSavesItem
import com.capstone.nutrisight.data.response.SaveProductRequest
import com.capstone.nutrisight.data.response.SaveResponse
import com.capstone.nutrisight.data.response.SavedProductItem
import com.capstone.nutrisight.data.response.SavedResponse
import com.capstone.nutrisight.repository.RegisterLoginRepository
import kotlinx.coroutines.launch

class ProductViewModel(private val registerLoginRepository: RegisterLoginRepository): ViewModel()  {

    private val _uploadProduct = MutableLiveData<SaveResponse>()
    val uploadProduct: LiveData<SaveResponse> = _uploadProduct

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _savedProducts = MutableLiveData<List<ProductsOnSavesItem>>()
    val savedProducts: LiveData<List<ProductsOnSavesItem>> = _savedProducts

    fun uploadProduct(productId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val request = SaveProductRequest(productId)
                val response = registerLoginRepository.uploadSavedProduct(request)
                _uploadProduct.value = response
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
            }
        }

    }

    fun getSavedProduct() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = registerLoginRepository.getSavedProduct()
                val productOnSavesList = response.savedProduct.flatMap { it.productsOnSaves }
                _savedProducts.value = productOnSavesList
                _isLoading.postValue(false)
            } catch (e: Exception) {
                _isLoading.value = false
            }
        }
    }


}