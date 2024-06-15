package com.capstone.nutrisight.ui.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.capstone.nutrisight.R
import com.capstone.nutrisight.data.response.ClassificationResponse
import com.capstone.nutrisight.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = Color.TRANSPARENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

        @Suppress("DEPRECATION")
        val response = intent.getParcelableExtra<ClassificationResponse>("classification_response")
        if (response != null) {
            displayResult(response)
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayResult(response: ClassificationResponse) {
        // all grade
        val grade = response.product.gradeAll
        val cardViewBackgroundColor = getBackgroundColorForGrade(grade)
        val textViewColor = getTextColorForGrade(grade)
        binding.cardViewGradeAll.backgroundTintList = ContextCompat.getColorStateList(this, cardViewBackgroundColor)
        binding.gradeSavedFood.setTextColor(ContextCompat.getColor(this, textViewColor))
        binding.gradeSavedFood.text = grade

        // fat grade
        val gradeFat = response.product.fatGrade
        val cardViewBackgroundColorFat = getBackgroundColorForGrade(gradeFat)
        val textViewColorFat = getTextColorForGrade(gradeFat)
        binding.cardGradeFat.backgroundTintList = ContextCompat.getColorStateList(this, cardViewBackgroundColorFat)
        binding.gradeFatFood.setTextColor(ContextCompat.getColor(this, textViewColorFat))
        binding.gradeFatFood.text = gradeFat

        // sugar grade
        val gradeSugar = response.product.sugarGrade
        val cardViewBackgroundColorSugar = getBackgroundColorForGrade(gradeSugar)
        val textViewColorSugar = getTextColorForGrade(gradeSugar)
        binding.cardGradeSugar.backgroundTintList = ContextCompat.getColorStateList(this, cardViewBackgroundColorSugar)
        binding.gradeSugarFood.setTextColor(ContextCompat.getColor(this, textViewColorSugar))
        binding.gradeSugarFood.text = gradeSugar

        // salt grade
        val gradeSalt = response.product.natriumGrade
        val cardViewBackgroundColorSalt = getBackgroundColorForGrade(gradeSalt)
        val textViewColorSalt = getTextColorForGrade(gradeSalt)
        binding.cardGradeSalt.backgroundTintList = ContextCompat.getColorStateList(this, cardViewBackgroundColorSalt)
        binding.gradeSaltFood.setTextColor(ContextCompat.getColor(this, textViewColorSalt))
        binding.gradeSaltFood.text = gradeSalt


        binding.nameResultFood.text = response.product.name
        binding.categoryResultFood.text = response.product.category
        if (response.product.category == "Food") {
            binding.netto.text = "${response.product.netWeight.toString()}g"
            binding.servingSize.text = "${response.product.servingSize}g)"
        } else {
            binding.netto.text = "${response.product.netWeight.toString()}ml"
            binding.servingSize.text = "${response.product.servingSize}ml)"
        }
        binding.calResultFood.text = response.product.energyTotal.toString()
        binding.perServing.text = "${response.product.servingAmount} per serving"
        binding.energy.text = "${response.product.energyTotal}Kj"
        binding.energyFat.text = "${response.product.energyFat}kcal"
        binding.fat.text = "${response.product.energyFat}g"
        binding.saturatedFat.text = "${response.product.saturatedFat}g"
        binding.protein.text = "${response.product.protein}g"
        binding.carbs.text = "${response.product.carbohydrate}g"
        binding.sugar.text = "${response.product.sugar}g"
        binding.natrium.text = "${response.product.natrium}mg"
        Glide.with(binding.root.context)
            .load(response.product.url)
            .into(binding.imgSavedFood)

    }

    private fun getBackgroundColorForGrade(grade: String?): Int {
        return when (grade) {
            "A" -> R.color.bg_grade_a
            "B" -> R.color.bg_grade_b
            "C" -> R.color.bg_grade_c
            "D" -> R.color.bg_grade_d
            "E" -> R.color.bg_grade_e
            else -> com.google.android.material.R.color.design_default_color_background
        }
    }

    private fun getTextColorForGrade(grade: String?): Int {
        return when (grade) {
            "A" -> R.color.text_grade_a
            "B" -> R.color.text_grade_b
            "C" -> R.color.text_grade_c
            "D" -> R.color.text_grade_d
            "E" -> R.color.text_grade_e
            else -> com.google.android.material.R.color.design_default_color_background
        }
    }
}