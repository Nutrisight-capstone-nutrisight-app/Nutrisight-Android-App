package com.capstone.nutrisight.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.nutrisight.data.response.ProductsOnSavesItem
import com.capstone.nutrisight.databinding.FragmentSavedBinding
import com.capstone.nutrisight.ui.activity.DetailActivity
import com.capstone.nutrisight.ui.adapter.SavedAdapter
import com.capstone.nutrisight.ui.model.ProductViewModel


class SavedFragment : Fragment() {
    private var _binding: FragmentSavedBinding? = null
    private val binding get() = _binding!!
    private var productViewModelInstance: ProductViewModel? = null
    private lateinit var savedAdapter: SavedAdapter
    fun setProductViewModel(viewModel: ProductViewModel) {
        productViewModelInstance = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("SavedFragment", "onViewCreated called")
        productViewModelInstance?.let {viewModel ->
            if (viewModel.savedProducts.value.isNullOrEmpty()) {
                viewModel.getSavedProduct()
            }
            viewModel.savedProducts.observe(viewLifecycleOwner) { response ->
                setProductData(response)
            }
            viewModel.isLoading.observe(viewLifecycleOwner) {
                showLoading(it)
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedBinding.inflate(inflater, container, false)
        val view = binding.root

        savedAdapter = SavedAdapter()
        savedAdapter.setOnItemClickCallback(object : SavedAdapter.OnItemClickCallback {
            override fun onItemClicked(product: ProductsOnSavesItem) {
                setDetails(product)
            }

        })

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvSaved.layoutManager = layoutManager
        binding.rvSaved.adapter = savedAdapter


        return view
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun setProductData(product: List<ProductsOnSavesItem>) {
        savedAdapter.submitList(product)
    }

    private fun setDetails(product: ProductsOnSavesItem) {
        val intent = Intent(requireContext(), DetailActivity::class.java)
        Log.d("SavedFragment", "Product ID: ${product.product.id}")
        intent.putExtra(DetailActivity.PRODUCT_ID, product.product.id.toString())
        startActivity(intent)
    }


}