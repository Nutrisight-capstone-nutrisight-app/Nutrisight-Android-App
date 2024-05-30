package com.capstone.nutrisight.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.capstone.nutrisight.R
import com.capstone.nutrisight.databinding.FragmentScanBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ScanBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentScanBottomSheetBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScanBottomSheetBinding.inflate(inflater, container, false)

        binding.btnClose.setOnClickListener {
            dismiss()
        }
        return binding.root
    }


}