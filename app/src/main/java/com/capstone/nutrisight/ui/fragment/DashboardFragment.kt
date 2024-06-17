package com.capstone.nutrisight.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.nutrisight.R
import com.capstone.nutrisight.data.api.ApiConstant.NEWS_API_KEY
import com.capstone.nutrisight.data.response.ArticlesItem
import com.capstone.nutrisight.data.response.UserResponse
import com.capstone.nutrisight.databinding.FragmentDashboardBinding
import com.capstone.nutrisight.ui.adapter.ArticleAdapter
import com.capstone.nutrisight.ui.model.ArticleViewModel
import com.capstone.nutrisight.ui.model.UserViewModel

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private lateinit var articleAdapter: ArticleAdapter
    private val binding get() = _binding!!
    private lateinit var articleViewModelInstance: ArticleViewModel
    private lateinit var userViewModelInstance: UserViewModel

    fun setArticleViewModel(viewModel: ArticleViewModel) {
        articleViewModelInstance = viewModel
    }

    fun setUserViewModel(viewModel: UserViewModel) {
        userViewModelInstance = viewModel
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val view = binding.root

        articleAdapter = ArticleAdapter()

        articleAdapter.setOnItemClickCallback(object : ArticleAdapter.OnItemClickCallback {
            override fun onItemClicked(news: ArticlesItem) {
                showUrlArticle(news)
            }
        })

        if (articleViewModelInstance.articlesItem.value.isNullOrEmpty()) {
            articleViewModelInstance.setArticlesItems("Healthy food", "en", NEWS_API_KEY)
        }


        articleViewModelInstance.articlesItem.observe(viewLifecycleOwner) {items ->
            if (items != null) {
                setArticleData(items)
            }

        }

        articleViewModelInstance.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }


        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvArticle.layoutManager = layoutManager

        userViewModelInstance.getUser()

        userViewModelInstance.userResponse.observe(viewLifecycleOwner) { response ->
            displayUser(response)
        }



        return view
    }

    private fun displayUser(response: UserResponse) {
        binding.usernameDashboard.text = response.user.username

        // average grade dashboard
        val grade = response.data.gradeAvg
        val cardViewBackgroundColor = getBackgroundColorForGrade(grade)
        val textViewColor = getTextColorForGrade(grade)
        binding.cardViewGradeDashboard.backgroundTintList = ContextCompat.getColorStateList(requireContext(), cardViewBackgroundColor)
        binding.gradeSavedFood.setTextColor(ContextCompat.getColor(requireContext(), textViewColor))
        binding.gradeSavedFood.text = grade

        // total food & drink calories
        binding.totalCaloriesFoodDashboard.text = response.data.foodCal.toString()
        binding.totalCaloriesDrinkDashboard.text = response.data.drinkCal.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarArticle.visibility = View.VISIBLE
        } else {
            binding.progressBarArticle.visibility = View.GONE
        }
    }

    private fun showUrlArticle(article: ArticlesItem) {
        val url = article.url
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)

    }

    private fun setArticleData(article: List<ArticlesItem>) {
        articleAdapter.submitList(article)
        binding.rvArticle.adapter = articleAdapter
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