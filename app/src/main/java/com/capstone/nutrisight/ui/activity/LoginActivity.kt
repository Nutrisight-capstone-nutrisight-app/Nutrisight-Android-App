package com.capstone.nutrisight.ui.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.capstone.nutrisight.R
import com.capstone.nutrisight.databinding.ActivityLoginBinding
import com.capstone.nutrisight.preferences.SettingsPreferences
import com.capstone.nutrisight.preferences.dataStore
import com.capstone.nutrisight.ui.model.LoginViewModel
import com.capstone.nutrisight.ui.model.MainViewModelFactory
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var settingsPreferences: SettingsPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
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

        val factory: MainViewModelFactory = MainViewModelFactory.getInstance(this)
        val viewModel: LoginViewModel by viewModels {
            factory
        }
        getSettingPreferences(applicationContext)
        settingsPreferences = SettingsPreferences.getInstance(dataStore)

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnLogin.setOnClickListener {
            val emailEmpty = binding.edtEmail.checkEditTextEmpty()
            val passwordEmpty = binding.edtPassword.checkEditTextEmpty()

            if (!emailEmpty && !passwordEmpty) {
                val emailValid = binding.edtEmail.error == null

                if (emailValid) {
                    val email = binding.edtEmail.text.toString().trim()
                    val password = binding.edtPassword.text.toString().trim()
                    viewModel.isLoading.observe(this) {
                        showLoading(it)
                    }
                    viewModel.login(email, password)
                } else {
                    if (!emailValid) {
                        Toast.makeText(this, R.string.invalid_email, Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, R.string.empty_field, Toast.LENGTH_SHORT).show()
            }


            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        viewModel.loginResponse.observe(this) { response ->
            lifecycleScope.launch {
                settingsPreferences.saveUser(response)
            }
            Toast.makeText(this, R.string.login_successful, Toast.LENGTH_SHORT).show()
            val intent = Intent(this@LoginActivity, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }
    }

    private fun showFailedDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_login_failed)
        dialog.setCancelable(false)

        val btnOk = dialog.findViewById<Button>(R.id.btn_ok)
        btnOk.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showLoadingDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_loading)
        dialog.setCancelable(false)

        dialog.show()
    }

    private fun hideLoadingDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_loading)
        dialog.setCancelable(false)

        dialog.dismiss()
    }

    private fun getSettingPreferences(context: Context): SettingsPreferences {
        return SettingsPreferences.getInstance(context.dataStore)
    }



    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            showLoadingDialog()
        } else {
            hideLoadingDialog()
        }
    }
}