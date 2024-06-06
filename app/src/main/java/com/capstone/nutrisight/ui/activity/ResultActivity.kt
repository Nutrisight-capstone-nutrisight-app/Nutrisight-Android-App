package com.capstone.nutrisight.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.capstone.nutrisight.R
import com.capstone.nutrisight.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val displayResult = intent.getStringExtra(EXTRA_RESULT)
        binding.resultText.text = displayResult

        onBackPressedCallback()

    }

    private fun onBackPressedCallback() {
        val dispatcher = onBackPressedDispatcher

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@ResultActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        dispatcher.addCallback(this, onBackPressedCallback)
    }

    companion object {
        const val EXTRA_RESULT = "extra_result"
    }
}