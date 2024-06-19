package com.capstone.nutrisight.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.capstone.nutrisight.R
import com.capstone.nutrisight.data.response.UserResponse
import com.capstone.nutrisight.databinding.FragmentProfileBinding
import com.capstone.nutrisight.ui.activity.SettingsActivity
import com.capstone.nutrisight.ui.model.UserViewModel


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var userViewModelInstance: UserViewModel? = null

    fun setUserViewModel(viewModel: UserViewModel) {
        userViewModelInstance = viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.btnSettings.setOnClickListener {
            val intent = Intent(activity, SettingsActivity::class.java)
            intent.putExtra("username", binding.username.text.toString())
            intent.putExtra("email", binding.email.text.toString())
            startActivity(intent)
        }

        userViewModelInstance?.let { viewModel ->
            if (viewModel.userResponse.value == null) {
                viewModel.getUser()
            }
            viewModel.userResponse.observe(viewLifecycleOwner) { response ->
                displayProfile(response)
            }
        }

        return view
    }

    private fun displayProfile(response: UserResponse) {
        binding.email.text = response.user.email
        binding.username.text = response.user.username

        binding.totalCalories.text = response.data.totalCal.toString()
        binding.totalCaloriesFood.text = response.data.foodCal.toString()
        binding.totalCaloriesDrink.text = response.data.drinkCal.toString()

        val grade = response.data.gradeAvg
        val cardViewBackgroundColor = getBackgroundColorForGrade(grade)
        val textViewColor = getTextColorForGrade(grade)
        binding.cardViewGradeAverage.backgroundTintList = ContextCompat.getColorStateList(requireContext(), cardViewBackgroundColor)
        binding.gradeAverageFood.setTextColor(ContextCompat.getColor(requireContext(), textViewColor))
        binding.gradeAverageFood.text = grade
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
