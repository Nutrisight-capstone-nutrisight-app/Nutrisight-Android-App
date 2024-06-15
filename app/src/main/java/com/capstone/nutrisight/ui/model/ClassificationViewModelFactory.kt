package com.capstone.nutrisight.ui.model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.nutrisight.di.Injection
import com.capstone.nutrisight.repository.ClassificationRepository

class ClassificationViewModelFactory(
    private val classificationRepository: ClassificationRepository
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ClassificationViewModel::class.java)) {
            ClassificationViewModel(classificationRepository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        fun getInstance(): ClassificationViewModelFactory {
            return ClassificationViewModelFactory(Injection.provideClassificationRepository())
        }
    }


}