package com.capstone.nutrisight.ui.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.nutrisight.preferences.SettingsPreferences
import com.capstone.nutrisight.repository.RegisterLoginRepository
import kotlinx.coroutines.launch

class SettingViewModel(private val pref: SettingsPreferences, private val registerLoginRepository: RegisterLoginRepository): ViewModel() {

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    fun getLanguage(): LiveData<String> {
        return pref.getLanguageSetting().asLiveData()
    }

    fun saveLanguage(language: String) {
        viewModelScope.launch {
            pref.saveLanguageSetting(language)
        }
    }

    fun logout() {
        viewModelScope.launch {
            pref.clearPreferences()
            registerLoginRepository.logout()
        }
    }

}