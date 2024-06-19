package com.capstone.nutrisight.ui.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.CompoundButton
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.lifecycleScope
import com.capstone.nutrisight.R
import com.capstone.nutrisight.databinding.ActivitySettingsBinding
import com.capstone.nutrisight.databinding.DialogLanguageBinding
import com.capstone.nutrisight.databinding.DialogLogoutBinding
import com.capstone.nutrisight.preferences.SettingsPreferences
import com.capstone.nutrisight.preferences.dataStore
import com.capstone.nutrisight.ui.model.factory.MainViewModelFactory
import com.capstone.nutrisight.ui.model.SettingViewModel
import kotlinx.coroutines.launch
import java.util.Locale

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var settingsPreferences: SettingsPreferences
    private val factory: MainViewModelFactory = MainViewModelFactory.getInstance(this)
    private val settingViewModel: SettingViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        window.statusBarColor = Color.TRANSPARENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
        setContentView(binding.root)

        settingsPreferences = SettingsPreferences.getInstance(dataStore)

        binding.btnLogout.setOnClickListener {
            showLogoutDialog()
        }

        binding.btnTranslate.setOnClickListener {
            showLanguageDialog()
        }

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchDarkmode.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchDarkmode.isChecked = false
            }
        }

        settingViewModel.getLanguage().observe(this) { language ->
            setLocale(language)
        }

        val username = intent.getStringExtra("username")
        val email = intent.getStringExtra("email")
        binding.usernameSettings.text = username

        binding.cardSettingProfile.setOnClickListener {
            val intent = Intent(this@SettingsActivity, EditActivity::class.java)
            intent.putExtra("username", username)
            intent.putExtra("email", email)
            startActivity(intent)
            finish()
        }

        binding.btnEdit.setOnClickListener {
            val intent = Intent(this@SettingsActivity, EditActivity::class.java)
            intent.putExtra("username", username)
            intent.putExtra("email", email)
            startActivity(intent)
            finish()
        }

        binding.switchDarkmode.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingViewModel.saveThemeSetting(isChecked)
        }

        binding.btnBackSettingsMain.setOnClickListener {
            val intent = Intent(this@SettingsActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        onBackPressedCallback()
    }

    private fun showLogoutDialog() {
        val dialog = Dialog(this)
        val binding: DialogLogoutBinding = DialogLogoutBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(true)

        binding.btnLogout.setOnClickListener {
            lifecycleScope.launch {
                settingViewModel.logout()
            }
            dialog.dismiss()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showLanguageDialog() {
        val dialog = Dialog(this)
        val binding: DialogLanguageBinding = DialogLanguageBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(true)

        binding.btnConfirm.setOnClickListener {
            val id = binding.radioLanguage.checkedRadioButtonId
            when (id) {
                R.id.radio_english -> {
                    setLocale("en")
                    settingViewModel.saveLanguage("en")
                    val intent = intent
                    finish()
                    startActivity(intent)
                }
                R.id.radio_indonesia -> {
                    setLocale("in")
                    settingViewModel.saveLanguage("in")
                    val intent = intent
                    finish()
                    startActivity(intent)
                }
                R.id.radio_japanese -> {
                    setLocale("ja")
                    settingViewModel.saveLanguage("ja")
                    val intent = intent
                    finish()
                    startActivity(intent)
                }
            }
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setLocale(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        createConfigurationContext(config)
        resources.updateConfiguration(config, resources.displayMetrics)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.create(locale))
        }
    }

    private fun onBackPressedCallback() {
        val dispatcher = onBackPressedDispatcher

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@SettingsActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        dispatcher.addCallback(this, onBackPressedCallback)
    }
}
