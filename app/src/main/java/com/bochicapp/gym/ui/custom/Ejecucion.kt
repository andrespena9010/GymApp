package com.bochicapp.gym.ui.custom

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.bochicapp.gym.ui.viewmodel.GymViewModel
import com.bochicapp.gym.ui.viewmodel.GymViewModelClass

@Composable
fun Ejecucion(
    viewModel: GymViewModelClass = GymViewModel,
    modifier: Modifier
) {

    BackHandler {
        viewModel.goToPrincipal()
    }

    Box(
       modifier = modifier
           .background( Color.Black )
    ){
        Text("Hola")
    }

}