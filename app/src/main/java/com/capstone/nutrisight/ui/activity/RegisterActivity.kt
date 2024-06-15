package com.capstone.nutrisight.ui.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.capstone.nutrisight.R
import com.capstone.nutrisight.databinding.ActivityRegisterBinding
import com.capstone.nutrisight.databinding.DialogRegisterFailedBinding
import com.capstone.nutrisight.databinding.DialogRegisterSuccessBinding
import com.capstone.nutrisight.ui.model.MainViewModelFactory
import com.capstone.nutrisight.ui.model.RegisterViewModel
import com.capstone.nutrisight.ui.model.SettingViewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
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
        registerViewModel = ViewModelProvider(this, factory)[RegisterViewModel::class.java]

        registerViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        registerViewModel.registerResponse.observe(this) { response ->
            response.message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                if (it == "User successfully created") {
                    showSuccessDialog()
                } else {
                    showFailedDialog("Error")
                }
            }
            response.error?.let {
                handleError(it)
            }
        }

        binding.btnRegisterNow.setOnClickListener {
            handleRegisterClick()
        }

        onBackPressedCallback()
    }

    private fun handleRegisterClick() {
        val usernameEmpty = binding.edtUsername.checkEditTextEmpty()
        val emailEmpty = binding.edtEmailRegister.checkEditTextEmpty()
        val passwordEmpty = binding.edtPasswordRegister.checkEditTextEmpty()
        val confirmPasswordEmpty = binding.edtConfirmPassword.checkEditTextEmpty()
        val username = binding.edtUsername.text.toString().trim()
        val email = binding.edtEmailRegister.text.toString().trim()
        val password = binding.edtPasswordRegister.text.toString().trim()
        val confirmPassword = binding.edtConfirmPassword.text.toString().trim()

        if (!usernameEmpty && !emailEmpty && !passwordEmpty && !confirmPasswordEmpty) {
            if (password == confirmPassword) {
                registerViewModel.register(username, email, password)
            } else {
                Toast.makeText(this, R.string.password_not_match, Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, R.string.empty_field, Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleError(error: String) {
        when (error) {
            "Email or username already exist" -> {
                showFailedDialog(error)
            }
            else -> {
                showFailedDialog(error)
            }
        }
    }

    private fun showSuccessDialog() {
        val dialog = Dialog(this)
        val binding: DialogRegisterSuccessBinding = DialogRegisterSuccessBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        binding.btnOk.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        showLoading(false)
        dialog.show()
    }

    private fun showFailedDialog(error: String) {
        val dialog = Dialog(this)
        val binding: DialogRegisterFailedBinding = DialogRegisterFailedBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        binding.btnTry.setOnClickListener {
            dialog.dismiss()
        }
        binding.tvError.text = error
        showLoading(false)
        dialog.show()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun onBackPressedCallback() {
        val dispatcher = onBackPressedDispatcher

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        dispatcher.addCallback(this, onBackPressedCallback)
    }
}
