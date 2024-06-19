package com.capstone.nutrisight.ui.model.factory

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.nutrisight.di.Injection
import com.capstone.nutrisight.preferences.SettingsPreferences
import com.capstone.nutrisight.preferences.dataStore
import com.capstone.nutrisight.repository.RegisterLoginRepository
import com.capstone.nutrisight.ui.model.ArticleViewModel
import com.capstone.nutrisight.ui.model.DetailViewModel
import com.capstone.nutrisight.ui.model.LoginViewModel
import com.capstone.nutrisight.ui.model.ProductViewModel
import com.capstone.nutrisight.ui.model.RegisterViewModel
import com.capstone.nutrisight.ui.model.SettingViewModel
import com.capstone.nutrisight.ui.model.UserViewModel

class MainViewModelFactory(
    private val registerLoginRepository: RegisterLoginRepository,
    private val pref: SettingsPreferences
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return try {
            if (modelClass.isAssignableFrom(ArticleViewModel::class.java)) {
                ArticleViewModel() as T
            } else if(modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                LoginViewModel(registerLoginRepository) as T
            } else if (modelClass.isAssignableFrom(SettingViewModel::class.java)){
                SettingViewModel(pref, registerLoginRepository) as T
            } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)){
                RegisterViewModel(registerLoginRepository) as T
            } else if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                UserViewModel(registerLoginRepository) as T
            } else if(modelClass.isAssignableFrom(ProductViewModel::class.java)) {
                ProductViewModel(registerLoginRepository) as T
            } else if(modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                DetailViewModel(registerLoginRepository) as T
            } else {
                throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
            }
        } catch (ex: Exception) {
            Log.e("MainViewModelFactory", "Error creating ViewModel", ex)
            throw ex
        }

    }

    companion object {

        fun getInstance(context: Context): MainViewModelFactory {
            val pref = SettingsPreferences.getInstance(context.dataStore)
            return MainViewModelFactory(Injection.registerLoginRepository(context),  pref)
        }
    }

}