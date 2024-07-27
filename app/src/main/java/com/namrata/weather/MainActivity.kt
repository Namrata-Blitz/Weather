package com.namrata.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.namrata.weather.ui.screen.WeatherScreen
import com.namrata.weather.ui.theme.WeatherTheme
import com.namrata.weather.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val weatherViewModel= ViewModelProvider(this)[WeatherViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            WeatherTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    innerPadding->
                    WeatherScreen(paddingValues = innerPadding,weatherViewModel)
                }
            }
        }
    }
}