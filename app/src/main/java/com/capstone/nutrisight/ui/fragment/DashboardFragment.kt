package com.capstone.nutrisight.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.nutrisight.R
import com.capstone.nutrisight.data.api.ApiConstant.NEWS_API_KEY
import com.capstone.nutrisight.data.response.ArticlesItem
import com.capstone.nutrisight.data.response.UserResponse
import com.capstone.nutrisight.databinding.FragmentDashboardBinding
import com.capstone.nutrisight.ui.activity.SettingsActivity
import com.capstone.nutrisight.ui.adapter.ArticleAdapter
import com.capstone.nutrisight.ui.model.ArticleViewModel
import com.capstone.nutrisight.ui.model.UserViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF

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


        binding.btnSettings.setOnClickListener {
            val intent = Intent(activity, SettingsActivity::class.java)
            intent.putExtra("username", binding.usernameDashboard.text.toString())
            intent.putExtra("email", userViewModelInstance?.let {
                it.userResponse.value?.user?.email
            })
            startActivity(intent)
        }

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
                setPieChart(response)
            }
        }



        return view
    }

    @SuppressLint("SetTextI18n")
    private fun displayUser(response: UserResponse) {
        binding.usernameDashboard.text = response.user.username


        val grade = response.data.gradeAvg
        val cardViewBackgroundColor = getBackgroundColorForGrade(grade)
        val textViewColor = getTextColorForGrade(grade)
        binding.cardViewGradeDashboard.backgroundTintList = ContextCompat.getColorStateList(requireContext(), cardViewBackgroundColor)
        binding.gradeSavedFood.setTextColor(ContextCompat.getColor(requireContext(), textViewColor))
        binding.gradeSavedFood.text = grade

        binding.totalCaloriesFoodDashboard.text = "${response.data.foodCal}kcal"
        binding.totalCaloriesDrinkDashboard.text = "${response.data.drinkCal}kcal"
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

    private fun setPieChart(response: UserResponse) {
        val pieChart = binding.pieChart
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

        pieChart.dragDecelerationFrictionCoef = 0.95f
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(ContextCompat.getColor(requireContext(), R.color.white_black))

        pieChart.setTransparentCircleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(110)

        pieChart.holeRadius = 60f
        pieChart.transparentCircleRadius = 63f

        pieChart.setDrawCenterText(true)


        val typeface = ResourcesCompat.getFont(requireContext(), R.font.josefin_sans)

        if (response.data.foodCal == 0 && response.data.drinkCal == 0) {
            pieChart.centerText = getString(R.string.no_data_available)
            pieChart.setCenterTextTypeface(typeface)
            pieChart.setCenterTextSize(18f)
            pieChart.data = null
        } else {
            pieChart.setCenterTextTypeface(typeface)
            pieChart.setCenterTextSize(18f)

            pieChart.centerText = getString(R.string.total_calories_in_percent)
            pieChart.setCenterTextColor(ContextCompat.getColor(requireContext(), R.color.black_white))

            // Add entries to the pie chart
            val entries = ArrayList<PieEntry>()
            entries.add(PieEntry(response.data.foodCal.toFloat(), getString(R.string.food)).apply {
                icon = ContextCompat.getDrawable(requireContext(), R.drawable.round_local_dining_24)
            })
            entries.add(PieEntry(response.data.drinkCal.toFloat(), getString(R.string.drink)).apply {
                icon = ContextCompat.getDrawable(requireContext(), R.drawable.round_local_cafe_24)
            })

            // Create a data set from the entries
            val dataSet = PieDataSet(entries, "")
            dataSet.sliceSpace = 3f
            dataSet.iconsOffset = MPPointF(0f, 56f)
            dataSet.selectionShift = 5f
            dataSet.valueTypeface = typeface
            dataSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.black_white)

            // Add colors to the data set
            val colors = ArrayList<Int>()
            colors.add(ContextCompat.getColor(requireContext(), R.color.bg_card_food))
            colors.add(ContextCompat.getColor(requireContext(), R.color.bg_card_drink))
            dataSet.colors = colors

            // Create pie data object and set it to the pie chart
            val data = PieData(dataSet)
            data.setValueFormatter(PercentFormatter(pieChart))
            data.setValueTextSize(12f)
            data.setValueTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            data.setValueTypeface(typeface)
            pieChart.data = data



        }

        pieChart.rotationAngle = 0f

        pieChart.isRotationEnabled = true
        pieChart.isHighlightPerTapEnabled = true

        pieChart.animateY(1400, Easing.EaseInOutQuad)

        pieChart.setEntryLabelColor(Color.TRANSPARENT)


        // Refresh the pie chart
        pieChart.invalidate()


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
