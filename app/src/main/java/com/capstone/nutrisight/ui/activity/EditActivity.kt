package com.capstone.nutrisight.ui.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.capstone.nutrisight.R
import com.capstone.nutrisight.databinding.ActivityEditBinding
import com.capstone.nutrisight.databinding.DialogDeleteBinding
import com.capstone.nutrisight.databinding.DialogEditFailedBinding
import com.capstone.nutrisight.databinding.DialogEditSuccessBinding
import com.capstone.nutrisight.ui.model.UserViewModel
import com.capstone.nutrisight.ui.model.factory.MainViewModelFactory
import kotlinx.coroutines.launch

class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding
    private val factory: MainViewModelFactory = MainViewModelFactory.getInstance(this)
    private val viewModel: UserViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        window.statusBarColor = Color.TRANSPARENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
        setContentView(binding.root)

        val emailPreview = intent.getStringExtra("email")
        val usernamePreview = intent.getStringExtra("username")

        binding.edtEmailEdit.setText(emailPreview)
        binding.edtUsernameEdit.setText(usernamePreview)

        binding.btnBackEditSettings.setOnClickListener {
            val intent = Intent(this@EditActivity, SettingsActivity::class.java)
            intent.putExtra("email", emailPreview)
            intent.putExtra("username", usernamePreview)
            startActivity(intent)
            finish()
        }

        viewModel.message.observe(this) { response ->
            response.message.let {
                if (it == "User successfully edited") {
                    showSuccessDialog(emailPreview, usernamePreview)
                } else {
                    Log.d("EditActivity", "onCreate: $it")
                    showFailedDialog("Failed to edit user")
                }
            }
        }

        binding.btnSaveEdit.setOnClickListener {
            val email = binding.edtEmailEdit.text.toString()
            val username = binding.edtUsernameEdit.text.toString()
            val password = binding.edtPasswordEdit.text.toString()

            if (email.isEmpty() || username.isEmpty()) {
                Toast.makeText(this, R.string.empty_field, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val emailValid = binding.edtEmailEdit.isValidEmail()
            val passwordValid = binding.edtPasswordEdit.checkEditTextPassword()

            if (email == emailPreview && username == usernamePreview && password.isEmpty()) {
                Toast.makeText(this, R.string.same_field, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!emailValid) {
                Toast.makeText(this, R.string.invalid_email, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isNotEmpty() && passwordValid) {
                Toast.makeText(this, R.string.invalid_password, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.updateUser(email, username, password)
        }


        binding.btnDeleteAccount.setOnClickListener {
            showDeleteDialog()
        }
    }

    private fun showSuccessDialog(email: String?, username: String?) {
        val dialog = Dialog(this)
        val binding: DialogEditSuccessBinding = DialogEditSuccessBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        binding.btnOk.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("email", email)
            intent.putExtra("username", username)
            startActivity(intent)
            finish()
        }

        dialog.show()
    }

    private fun showFailedDialog(error: String) {
        val dialog = Dialog(this)
        val binding: DialogEditFailedBinding = DialogEditFailedBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        binding.btnTry.setOnClickListener {
            dialog.dismiss()
        }
        binding.tvError.text = error
        dialog.show()
    }

    private fun showDeleteDialog() {
        val dialog = Dialog(this)
        val binding: DialogDeleteBinding = DialogDeleteBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(true)

        binding.btnDelete.setOnClickListener {
            lifecycleScope.launch {
                viewModel.deleteUser()
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

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        super.onBackPressed()
        val email = intent.getStringExtra("email")
        val username = intent.getStringExtra("username")
        val intent = Intent(this@EditActivity, SettingsActivity::class.java)
        intent.putExtra("email", email)
        intent.putExtra("username", username)
        startActivity(intent)
        finish()
    }

}
