package com.capstone.nutrisight.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.nutrisight.preferences.SettingsPreferences

class SettingViewModelFactory(private val pref: SettingsPreferences): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            SettingViewModel(pref) as T
        } else if (modelClass.isAssignableFrom(ArticleViewModel::class.java)){
            ArticleViewModel() as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SettingViewModelFactory? = null
        @JvmStatic
        fun getInstance(pref: SettingsPreferences): SettingViewModelFactory {
            if (INSTANCE == null) {
                synchronized(SettingViewModelFactory::class.java) {
                    INSTANCE = SettingViewModelFactory(pref)
                }
            }
            return INSTANCE as SettingViewModelFactory
        }
    }

}