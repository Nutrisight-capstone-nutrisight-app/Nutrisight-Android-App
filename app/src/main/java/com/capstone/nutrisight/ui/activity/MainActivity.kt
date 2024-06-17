package com.capstone.nutrisight.ui.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
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
import com.capstone.nutrisight.ui.model.UserViewModel
import com.capstone.nutrisight.ui.model.factory.MainViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var scanBottomSheet: ScanBottomSheet
    private val articleViewModel: ArticleViewModel by viewModels<ArticleViewModel>() {
        MainViewModelFactory.getInstance(applicationContext)
    }
    private val userViewModel: UserViewModel by viewModels<UserViewModel>() {
        MainViewModelFactory.getInstance(applicationContext)
    }



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
        scanBottomSheet.dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)


        binding.fabScan.setOnClickListener {
            openBottomSheet()
        }
        loadFragment(DashboardFragment())

        setupBottomNavigation()
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
        val dashboardFragment = if (fragment is DashboardFragment) {
            fragment.apply {
                setArticleViewModel(articleViewModel)
                setUserViewModel(userViewModel)
            }
        } else if (fragment is ProfileFragment) {
            fragment.apply {
                setUserViewModel(userViewModel)
            }
        } else {
            fragment
        }

        transaction.replace(R.id.container, dashboardFragment)
        transaction.commit()
    }




}