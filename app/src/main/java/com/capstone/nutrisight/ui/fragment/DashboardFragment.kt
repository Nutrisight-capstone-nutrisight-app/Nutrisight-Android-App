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
    private var articleViewModelInstance: ArticleViewModel? = null
    private var userViewModelInstance: UserViewModel? = null

    fun setArticleViewModel(viewModel: ArticleViewModel) {
        articleViewModelInstance = viewModel
    }

    fun setUserViewModel(viewModel: UserViewModel) {
        userViewModelInstance = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        articleViewModelInstance?.let { articleViewModel ->
            if (articleViewModel.articlesItem.value.isNullOrEmpty()) {
                articleViewModel.setArticlesItems("Healthy food", "en", NEWS_API_KEY)
            }

            articleViewModel.articlesItem.observe(viewLifecycleOwner) { items ->
                if (items.isNullOrEmpty()) {
                    binding.errorFetchArticle.visibility = View.VISIBLE
                    binding.errorFetchArticle.text = getString(R.string.error_fetch_article)
                } else {
                    binding.errorFetchArticle.visibility = View.GONE
                    setArticleData(items)
                }
            }

            articleViewModel.isLoading.observe(viewLifecycleOwner) { showLoading(it) }


        }
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



        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvArticle.layoutManager = layoutManager
        binding.rvArticle.adapter = articleAdapter

        userViewModelInstance?.let { userViewModel ->
            if (userViewModel.userResponse.value == null) {
                userViewModel.getUser()
            }
            userViewModel.userResponse.observe(viewLifecycleOwner) { response ->
                displayUser(response)
            }
        }

        return view
    }

    private fun displayUser(response: UserResponse) {
        binding.usernameDashboard.text = response.user.username

        val grade = response.data.gradeAvg
        val cardViewBackgroundColor = getBackgroundColorForGrade(grade)
        val textViewColor = getTextColorForGrade(grade)
        binding.cardViewGradeDashboard.backgroundTintList = ContextCompat.getColorStateList(requireContext(), cardViewBackgroundColor)
        binding.gradeSavedFood.setTextColor(ContextCompat.getColor(requireContext(), textViewColor))
        binding.gradeSavedFood.text = grade

        binding.totalCaloriesFoodDashboard.text = response.data.foodCal.toString()
        binding.totalCaloriesDrinkDashboard.text = response.data.drinkCal.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarArticle.visibility = if (isLoading) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun showUrlArticle(article: ArticlesItem) {
        val url = article.url
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun setArticleData(article: List<ArticlesItem>) {
        articleAdapter.submitList(article)
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
