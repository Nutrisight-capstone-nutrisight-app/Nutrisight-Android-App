package com.capstone.nutrisight.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager.widget.ViewPager
import com.capstone.nutrisight.R
import com.capstone.nutrisight.databinding.ActivityOnBoardingBinding
import com.capstone.nutrisight.preferences.SettingsPreferences
import com.capstone.nutrisight.preferences.dataStore
import com.capstone.nutrisight.ui.adapter.SliderAdapter
import com.capstone.nutrisight.ui.model.SettingViewModel
import com.capstone.nutrisight.ui.model.SettingViewModelFactory
import com.google.android.material.animation.AnimationUtils
import java.util.Locale

class OnBoardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardingBinding
    private lateinit var sliderAdapter: SliderAdapter
    private lateinit var viewPager: ViewPager
    private lateinit var dotsLayout: LinearLayout
    private lateinit var dots: Array<TextView?>
    private var showAnimation = false
    private var animation: Animation? = null
    private val settingViewModel: SettingViewModel by viewModels<SettingViewModel>() {
        SettingViewModelFactory.getInstance(getSettingPreferences(this))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()

        setContentView(binding.root)

        sliderAdapter = SliderAdapter(this)
        viewPager = binding.slider
        dotsLayout = binding.dots

        viewPager.adapter = sliderAdapter


        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                // Do Nothing
            }

            override fun onPageSelected(position: Int) {
                addDots(position)
                animation = android.view.animation.AnimationUtils.loadAnimation(this@OnBoardingActivity, androidx.appcompat.R.anim.abc_fade_in)

                when (position) {
                    0 -> {
                        binding.startBtn.visibility = View.GONE
                        binding.skipBtn.visibility = View.VISIBLE
                        binding.nextBtn.visibility = View.VISIBLE
                    }
                    1 -> {
                        if (showAnimation) {
                            binding.skipBtn.startAnimation(animation)
                            binding.nextBtn.startAnimation(animation)
                            showAnimation = false
                        }
                        binding.startBtn.visibility = View.GONE
                        binding.skipBtn.visibility = View.VISIBLE
                        binding.nextBtn.visibility = View.VISIBLE
                    }
                    2 -> {
                        if (showAnimation) {
                            binding.skipBtn.startAnimation(animation)
                            binding.nextBtn.startAnimation(animation)
                            showAnimation = false
                        }
                        binding.startBtn.visibility = View.GONE
                        binding.skipBtn.visibility = View.VISIBLE
                        binding.nextBtn.visibility = View.VISIBLE
                    }
                    else -> {
                        if (position >= 3) {
                            binding.startBtn.startAnimation(animation)
                            showAnimation = true
                        }

                        binding.startBtn.visibility = View.VISIBLE
                        binding.skipBtn.visibility = View.GONE
                        binding.nextBtn.visibility = View.GONE
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })

        binding.nextBtn.setOnClickListener{ viewPager.currentItem++ }

        binding.skipBtn.setOnClickListener {
            val intent = Intent(this@OnBoardingActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.startBtn.setOnClickListener {
            val intent = Intent(this@OnBoardingActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        settingViewModel.getLanguage().observe(this) { language: String ->
            setLocale(language)
        }


    }

    private fun setLocale(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val resources = resources
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun addDots(position: Int) {
        dotsLayout.removeAllViews()

        val numPages = 4

        dots = Array(numPages) { null }

        for (i in 0 until numPages) {
            val dot = TextView(this)
            dot.text = "•"
            dot.textSize = 35f

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            dot.layoutParams = params

            dotsLayout.addView(dot)
            dots[i] = dot

            if (i == position) {
                dots[i]?.setTextColor(getColor(R.color.main_green))
            } else {
                dots[i]?.setTextColor(getColor(R.color.black_white))
            }
        }
    }

    private fun getSettingPreferences(context: Context): SettingsPreferences {
        return SettingsPreferences.getInstance(context.dataStore)
    }
}