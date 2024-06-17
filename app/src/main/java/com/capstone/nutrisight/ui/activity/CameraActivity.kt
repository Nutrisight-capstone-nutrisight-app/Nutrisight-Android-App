package com.capstone.nutrisight.ui.activity

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.OrientationEventListener
import android.view.Surface
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.capstone.nutrisight.R
import com.capstone.nutrisight.databinding.ActivityCameraBinding
import com.capstone.nutrisight.ui.model.ClassificationViewModel
import com.capstone.nutrisight.ui.model.factory.ClassificationViewModelFactory
import com.capstone.nutrisight.utils.createCustomTempFile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    private var imageCapture: ImageCapture? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private val factory: ClassificationViewModelFactory = ClassificationViewModelFactory.getInstance()
    private val viewModel: ClassificationViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.captureImage.setOnClickListener {
            takePhoto()
        }

        viewModel.classificationResponse.observe(this) {response ->
            response?.let {
                showLoading(false)
                val intent = Intent(this@CameraActivity, ResultActivity::class.java)
                intent.putExtra("classification_response", response)
                startActivity(intent)
                finish()
            }
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUI()
        startCamera()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                Toast.makeText(
                    this@CameraActivity,
                    "Failed to show camera",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e(TAG, "startCamera: ${e.message}")
            }
        }, ContextCompat.getMainExecutor(this))

    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = createCustomTempFile(application)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    uploadImage(photoFile)
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(
                        this@CameraActivity,
                        "Gagal mengambil gambar.",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(TAG, "onError: ${exception.message}")
                    showLoading(false)
                }

            }
        )
    }

    private fun uploadImage(file: File) {
        try {
            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
            viewModel.upload(body)
        } catch (e: Exception) {
            showError(e.message)
            showLoading(false)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            showLoadingDialog()
        } else {
            hideLoadingDialog()
        }
    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private val orientationEventListener by lazy {
        object : OrientationEventListener(this) {
            override fun onOrientationChanged(orientation: Int) {
                if (orientation == ORIENTATION_UNKNOWN) {
                    return
                }
                val rotation = when (orientation) {
                    in 45 until 135 -> Surface.ROTATION_270
                    in 135 until 225 -> Surface.ROTATION_180
                    in 225 until 315 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }
                imageCapture?.targetRotation = rotation
            }
        }
    }

    private fun showError(message: String?) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_capture_failed)
        dialog.setCancelable(true)

        val errorMessageTextView = dialog.findViewById<TextView>(R.id.tv_failed)
        errorMessageTextView.text = message

        val btnOk = dialog.findViewById<TextView>(R.id.btn_ok)
        btnOk.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }

    private fun showLoadingDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_loading)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }

    private fun hideLoadingDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_loading)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.dismiss()
    }

    override fun onStart() {
        super.onStart()
        orientationEventListener.enable()
    }
    override fun onStop() {
        super.onStop()
        orientationEventListener.disable()
    }

    companion object {
        private const val TAG = "CameraActivity"
    }
}