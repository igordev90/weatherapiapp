package com.example.weatherapp.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.viewmodel.MainViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var inputField: TextInputLayout

    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewPager = binding.viewPager
        tabLayout = binding.tabLayout
        inputField = binding.mainInputField
        prepareViewPager()

        inputField.setEndIconOnClickListener {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (inputMethodManager.isActive) {
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            }

            lifecycleScope.launch {
                mainViewModel.getCoordinates(inputField.editText?.text.toString())
            }
        }

        mainViewModel.coordinatesResult.observe(this, Observer {
            lifecycleScope.launch {
                mainViewModel.getCurrentWeather(it.lat, it.lon)
                mainViewModel.getForecast(it.lat, it.lon)
            }
        })

        inputField.editText?.requestFocus()
    }

    private fun prepareViewPager() {
        val fragmentList = arrayListOf(
            WeatherFragment.newInstance(),
            ForecastFragment.newInstance(),
        )

        val tabTitlesArray = arrayOf(
            "Weather",
            "Forecast"
        )

        viewPager.adapter = ViewPagerAdapter(this, fragmentList)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitlesArray[position]
            when (position) {
                0 -> tab.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_wb_sunny_24)
                1 -> tab.icon =
                    ContextCompat.getDrawable(this, R.drawable.ic_baseline_calendar_month_24)
            }
        }.attach()
    }

}