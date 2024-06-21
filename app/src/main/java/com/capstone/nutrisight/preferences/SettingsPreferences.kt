package com.capstone.nutrisight.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.capstone.nutrisight.data.response.LoginResponse
import com.capstone.nutrisight.preferences.SettingsPreferences.PreferencesKeys.LANGUAGE_KEY
import com.capstone.nutrisight.preferences.SettingsPreferences.PreferencesKeys.THEME_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.concurrent.Volatile


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class SettingsPreferences private constructor(private val dataStore: DataStore<Preferences>){
    companion object {
        @Volatile
        private var INSTANCE: SettingsPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingsPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingsPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

    private object PreferencesKeys {
        val TOKEN = stringPreferencesKey("token")
        val THEME_KEY = booleanPreferencesKey("theme_setting")
        val LANGUAGE_KEY = stringPreferencesKey("language_setting")
    }

    fun getUser(): Flow<LoginResponse?> {
        return dataStore.data.map { preferences ->
            val token = preferences[PreferencesKeys.TOKEN]

            if (token != null) {
                LoginResponse(token, "")
            } else {
                null
            }
        }
    }

    suspend fun saveUser(user: LoginResponse) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.TOKEN] = user.accessToken
        }
    }

    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: false
        }
    }

    fun getLanguageSetting(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[LANGUAGE_KEY] ?: "en"
        }
    }

    suspend fun saveLanguageSetting(language: String){
        dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = language
        }
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean){
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDarkModeActive
        }
    }

    suspend fun clearPreferences() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

}