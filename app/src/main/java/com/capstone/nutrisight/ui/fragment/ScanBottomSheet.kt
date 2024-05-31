package com.capstone.nutrisight.ui.fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.capstone.nutrisight.R
import com.capstone.nutrisight.databinding.FragmentScanBottomSheetBinding
import com.capstone.nutrisight.ui.activity.CameraActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ScanBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentScanBottomSheetBinding
    private var currentImageUri: Uri? = null

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var launcherGallery: ActivityResultLauncher<PickVisualMediaRequest>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Permission request granted", Toast.LENGTH_SHORT).show()
                startCameraX()
            } else {
                Toast.makeText(requireContext(), "Permission request denied", Toast.LENGTH_SHORT).show()
            }
        }

        launcherGallery = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri: Uri? ->
            if (uri != null) {
                currentImageUri = uri
                Toast.makeText(requireContext(), "Media selected: $uri", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("Photo Picker", "No media selected")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScanBottomSheetBinding.inflate(inflater, container, false)

        binding.btnClose.setOnClickListener {
            dismiss()
        }

        binding.btnCamera.setOnClickListener {
            if (allPermissionsGranted()) {
                startCameraX()
            } else {
                requestPermissionLauncher.launch(REQUIRED_PERMISSION)
            }
        }

        binding.btnGallery.setOnClickListener {
            startGallery()
        }

        return binding.root
    }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private fun startCameraX() {
        val intent = Intent(requireContext(), CameraActivity::class.java)
        startActivity(intent)
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}



