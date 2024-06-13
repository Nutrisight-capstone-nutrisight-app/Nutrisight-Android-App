package com.capstone.nutrisight.ui.model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.nutrisight.di.Injection
import com.capstone.nutrisight.preferences.SettingsPreferences
import com.capstone.nutrisight.preferences.dataStore
import com.capstone.nutrisight.repository.RegisterLoginRepository

class MainViewModelFactory(
    private val registerLoginRepository: RegisterLoginRepository,
    private val pref: SettingsPreferences
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ArticleViewModel::class.java)) {
            ArticleViewModel() as T
        } else if(modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            LoginViewModel(registerLoginRepository) as T
        } else if (modelClass.isAssignableFrom(SettingViewModel::class.java)){
            SettingViewModel(pref, registerLoginRepository) as T
        } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)){
            RegisterViewModel(registerLoginRepository) as T
        } else {
                throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
            }
    }

    companion object {

        fun getInstance(context: Context): MainViewModelFactory {
            val pref = SettingsPreferences.getInstance(context.dataStore)
            return MainViewModelFactory(Injection.registerLoginRepository(context), pref)
        }
    }

}