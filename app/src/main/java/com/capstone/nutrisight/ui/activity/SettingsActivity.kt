package com.capstone.nutrisight.ui.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.capstone.nutrisight.R
import com.capstone.nutrisight.databinding.ActivitySettingsBinding
import com.capstone.nutrisight.databinding.DialogLanguageBinding
import com.capstone.nutrisight.databinding.DialogLogoutBinding
import java.util.Locale

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        window.statusBarColor = Color.TRANSPARENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
        setContentView(binding.root)

        binding.btnLogout.setOnClickListener {
            showLogoutDialog()
        }

        binding.btnTranslate.setOnClickListener {
            showLanguageDialog()
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
                R.id.radio_english -> setLocale("en")
                R.id.radio_indonesia -> setLocale("in")
                R.id.radio_japanese -> setLocale("ja")
            }
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setLocale(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val resources = resources
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        val intent = intent
        finish()
        startActivity(intent)
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