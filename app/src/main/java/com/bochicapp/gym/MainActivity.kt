package com.bochicapp.gym

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.bochicapp.gym.ui.theme.GymTheme
import com.bochicapp.gym.ui.view.Gym
import com.bochicapp.gym.ui.viewmodel.GymViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        GymViewModel.load( context = applicationContext )
        setContent {
            GymTheme {
                Gym()
            }
        }
    }
}