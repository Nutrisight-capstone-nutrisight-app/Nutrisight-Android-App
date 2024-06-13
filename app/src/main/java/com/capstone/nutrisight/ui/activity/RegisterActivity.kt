package com.capstone.nutrisight.ui.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
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
import com.capstone.nutrisight.R
import com.capstone.nutrisight.databinding.ActivityRegisterBinding
import com.capstone.nutrisight.databinding.DialogRegisterFailedBinding
import com.capstone.nutrisight.databinding.DialogRegisterSuccessBinding
import com.capstone.nutrisight.ui.model.MainViewModelFactory
import com.capstone.nutrisight.ui.model.RegisterViewModel
import com.capstone.nutrisight.ui.model.SettingViewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

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
        val registerViewModel: RegisterViewModel by viewModels {
            factory
        }

        binding.btnRegisterNow.setOnClickListener {
            val nameEmpty = binding.edtUsername.checkEditTextEmpty()
            val emailEmpty = binding.edtEmailRegister.checkEditTextEmpty()
            val passwordEmpty = binding.edtPasswordRegister.checkEditTextEmpty()
            val confirmPasswordEmpty = binding.edtConfirmPassword.checkEditTextEmpty()
            val name = binding.edtUsername.text.toString().trim()
            val email = binding.edtEmailRegister.text.toString().trim()
            val password = binding.edtPasswordRegister.text.toString().trim()
            val confirmPassword = binding.edtConfirmPassword.text.toString().trim()

            if (!nameEmpty && !emailEmpty && !passwordEmpty && !confirmPasswordEmpty) {
                if (password == confirmPassword) {
                    registerViewModel.isLoading.observe(this) {
                        showLoading(it)
                    }
                    registerViewModel.message.observe(this) {
                        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                    }
                    registerViewModel.register(name, email, password)
                } else {
                    Toast.makeText(this, R.string.password_not_match, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, R.string.empty_field, Toast.LENGTH_SHORT).show()
            }
            showSuccessDialog()
        }

        registerViewModel.registerResponse.observe(this) {
            showSuccessDialog()
        }

        onBackPressedCallback()

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

    private fun showFailedDialog() {
        val dialog = Dialog(this)
        val binding: DialogRegisterFailedBinding = DialogRegisterFailedBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)


        binding.btnTry.setOnClickListener {
            dialog.dismiss()
        }
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