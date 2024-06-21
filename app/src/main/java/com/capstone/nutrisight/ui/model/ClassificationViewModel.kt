package com.capstone.nutrisight.ui.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.nutrisight.data.response.ClassificationResponse
import com.capstone.nutrisight.repository.ClassificationRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import okhttp3.MultipartBody
import java.io.IOException
import java.net.SocketTimeoutException

class ClassificationViewModel(private val classificationRepository: ClassificationRepository): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _classificationResponse = MutableLiveData<ClassificationResponse>()
    val classificationResponse: LiveData<ClassificationResponse> = _classificationResponse

    fun upload(image: MultipartBody.Part) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                withTimeout(15_000) {
                    val response = classificationRepository.upload(image)
                    _classificationResponse.value = response
                }
            } catch (e: SocketTimeoutException) {
                Log.d("ClassificationViewModel", "upload: Socket timeout")
                _isLoading.postValue(false)
            } catch (e: IOException) {
                Log.d("ClassificationViewModel", "upload: Network error")
                _isLoading.postValue(false)
            } catch (e: Exception) {
                Log.d("ClassificationViewModel", "upload: ${e.message}")
                _isLoading.postValue(false)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}