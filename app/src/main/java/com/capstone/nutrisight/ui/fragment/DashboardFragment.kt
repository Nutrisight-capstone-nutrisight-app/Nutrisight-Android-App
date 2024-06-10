package com.capstone.nutrisight.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.nutrisight.R
import com.capstone.nutrisight.data.api.NEWS_API_KEY
import com.capstone.nutrisight.data.response.ArticlesItem
import com.capstone.nutrisight.databinding.FragmentDashboardBinding
import com.capstone.nutrisight.ui.adapter.ArticleAdapter
import com.capstone.nutrisight.ui.model.ArticleViewModel
import com.capstone.nutrisight.ui.model.MainViewModelFactory

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private lateinit var articleAdapter: ArticleAdapter
    private val binding get() = _binding!!
    private val articleViewModel: ArticleViewModel by activityViewModels<ArticleViewModel>()


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

        if (articleViewModel.articlesItem.value.isNullOrEmpty()) {
            articleViewModel.setArticlesItems("Healthy food", "en", NEWS_API_KEY)
        }


        articleViewModel.articlesItem.observe(viewLifecycleOwner) {items ->
            if (items != null) {
                setArticleData(items)
            }

        }

        articleViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvArticle.layoutManager = layoutManager

        return view
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

}