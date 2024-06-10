package com.capstone.nutrisight.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.nutrisight.data.response.ArticlesItem
import com.capstone.nutrisight.databinding.ArticleItemRowBinding

class ArticleAdapter: ListAdapter<ArticlesItem, ArticleAdapter.ArticleViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(news: ArticlesItem)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArticleAdapter.ArticleViewHolder {
        val binding = ArticleItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleAdapter.ArticleViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(article)
        }
    }

    inner class ArticleViewHolder(private val binding: ArticleItemRowBinding) :
            RecyclerView.ViewHolder(binding.root) {
                fun bind(article: ArticlesItem) {
                    binding.tvArticleTitle.text = article.title
                    Glide.with(binding.root.context)
                        .load(article.urlToImage)
                        .into(binding.imgArticle)
                }
            }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticlesItem>() {
            override fun areItemsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }

        }
    }
}