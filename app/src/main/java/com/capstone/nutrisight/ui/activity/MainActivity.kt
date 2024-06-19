package com.capstone.nutrisight.ui.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.capstone.nutrisight.R
import com.capstone.nutrisight.databinding.ActivityMainBinding
import com.capstone.nutrisight.ui.fragment.DashboardFragment
import com.capstone.nutrisight.ui.fragment.FoodFragment
import com.capstone.nutrisight.ui.fragment.ProfileFragment
import com.capstone.nutrisight.ui.fragment.SavedFragment
import com.capstone.nutrisight.ui.fragment.ScanBottomSheet
import com.capstone.nutrisight.ui.model.ArticleViewModel
import com.capstone.nutrisight.ui.model.ClassificationViewModel
import com.capstone.nutrisight.ui.model.ProductViewModel
import com.capstone.nutrisight.ui.model.UserViewModel
import com.capstone.nutrisight.ui.model.factory.ClassificationViewModelFactory
import com.capstone.nutrisight.ui.model.factory.MainViewModelFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var scanBottomSheet: ScanBottomSheet
    private val articleViewModel: ArticleViewModel by viewModels<ArticleViewModel>() {
        MainViewModelFactory.getInstance(applicationContext)
    }
    private val userViewModel: UserViewModel by viewModels<UserViewModel>() {
        MainViewModelFactory.getInstance(applicationContext)
    }
    private val productViewModel: ProductViewModel by viewModels<ProductViewModel>() {
        MainViewModelFactory.getInstance(applicationContext)
    }
    private val factory: ClassificationViewModelFactory = ClassificationViewModelFactory.getInstance()
    private val classificationViewModel: ClassificationViewModel by viewModels {
        factory
    }
    private var loadingDialog: Dialog? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        window.statusBarColor = Color.TRANSPARENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
        setContentView(binding.root)

        scanBottomSheet = ScanBottomSheet()


        binding.fabScan.setOnClickListener {
            openBottomSheet()
        }
        loadFragment(DashboardFragment())

        setupBottomNavigation()

        classificationViewModel.classificationResponse.observe(this) {response ->
            response?.let {
                showLoading(false)
                val intent = Intent(this@MainActivity, ResultActivity::class.java)
                intent.putExtra("classification_response", response)
                startActivity(intent)
                finish()
            }
        }


        classificationViewModel.isLoading.observe(this) {
            showLoading(it)
        }


    }

    private fun setupBottomNavigation() {
        binding.navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> loadFragment(DashboardFragment())
                R.id.nav_profile -> loadFragment(ProfileFragment())
                R.id.nav_saved -> loadFragment(SavedFragment())
                R.id.nav_food -> loadFragment(FoodFragment())
            }
            true
        }

    }

    private fun openBottomSheet() {
        scanBottomSheet.show(supportFragmentManager, "ScanBottomSheet")
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        val dashboardFragment = when (fragment) {
            is DashboardFragment -> {
                fragment.apply {
                    setArticleViewModel(articleViewModel)
                    setUserViewModel(userViewModel)
                }
            }

            is ProfileFragment -> {
                fragment.apply {
                    setUserViewModel(userViewModel)
                }
            }

            is SavedFragment -> {
                fragment.apply {
                    setProductViewModel(productViewModel)
                }
            }

            else -> {
                fragment
            }
        }

        transaction.replace(R.id.container, dashboardFragment)
        transaction.commit()
    }

    fun processSelectedImage(uri: Uri) {
        uploadImage(uri)
    }

    private fun uploadImage(uri: Uri) {
        try {
            val contentResolver = applicationContext.contentResolver

            val inputStream = contentResolver.openInputStream(uri)
            val file = File(applicationContext.filesDir, "image.jpg")
            val outputStream = FileOutputStream(file)

            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

            classificationViewModel.upload(body)
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
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

    private fun showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = Dialog(this).apply {
                setContentView(R.layout.dialog_loading)
                setCancelable(false)
                window?.setBackgroundDrawableResource(android.R.color.transparent)
            }
        }
        loadingDialog?.show()
    }

    private fun hideLoadingDialog() {
        loadingDialog?.dismiss()
    }


}