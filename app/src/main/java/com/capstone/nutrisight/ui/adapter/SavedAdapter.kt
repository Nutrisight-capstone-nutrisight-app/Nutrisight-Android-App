package com.capstone.nutrisight.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.nutrisight.R
import com.capstone.nutrisight.data.response.Product
import com.capstone.nutrisight.data.response.ProductsOnSavesItem
import com.capstone.nutrisight.data.response.SavedProductItem
import com.capstone.nutrisight.databinding.SavedItemRowBinding
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class SavedAdapter: ListAdapter<ProductsOnSavesItem, SavedAdapter.ProductViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(product: ProductsOnSavesItem)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductViewHolder {
        val binding = SavedItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(product)
        }

        Log.d("SavedAdapter", "onBindViewHolder: $product and $position")
    }

    inner class ProductViewHolder(private val binding: SavedItemRowBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(productsOnSavesItem: ProductsOnSavesItem) {
            val product = productsOnSavesItem.product
            binding.nameSavedFood.text = product.name
            binding.categorySavedFood.text = product.category
            binding.calSavedFood.text = product.energyTotal.toString()


//            // Format createdAt date
//            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
//            val parsedDate = productsOnSavesItem.createdAt.let { dateFormat.parse(it) }
//            val formattedDate = parsedDate?.let {
//                SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(it)
//            } ?: "Unknown Date"
//            binding.dateSavedFood.text = formattedDate

            // show grade
            val grade = product.gradeAll
            val cardViewBackgroundColor = getBackgroundColorForGrade(grade)
            val textViewColor = getTextColorForGrade(grade)
            binding.cardViewGradeSaved.backgroundTintList = ContextCompat.getColorStateList(binding.root.context, cardViewBackgroundColor)
            binding.gradeSavedFood.setTextColor(ContextCompat.getColor(binding.root.context, textViewColor))
            binding.gradeSavedFood.text = grade
            Glide.with(binding.root.context)
                .load(product.url)
                .dontAnimate()
                .placeholder(R.drawable.round_person_24)
                .into(binding.imgSavedFood)
        }
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


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductsOnSavesItem>() {
            override fun areItemsTheSame(
                oldItem: ProductsOnSavesItem,
                newItem: ProductsOnSavesItem
            ): Boolean {
                return oldItem.product.id == newItem.product.id
            }

            override fun areContentsTheSame(
                oldItem: ProductsOnSavesItem,
                newItem: ProductsOnSavesItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
