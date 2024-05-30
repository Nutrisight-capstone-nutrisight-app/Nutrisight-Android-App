package com.capstone.nutrisight.ui.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.capstone.nutrisight.R
import com.capstone.nutrisight.databinding.ActivityMainBinding
import com.capstone.nutrisight.ui.fragment.DashboardFragment
import com.capstone.nutrisight.ui.fragment.FoodFragment
import com.capstone.nutrisight.ui.fragment.ProfileFragment
import com.capstone.nutrisight.ui.fragment.SavedFragment
import com.capstone.nutrisight.ui.fragment.ScanBottomSheet

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var scanBottomSheet: ScanBottomSheet
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
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }
}