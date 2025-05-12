package com.bochicapp.gym.ui.view

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bochicapp.gym.R
import com.bochicapp.gym.ui.viewmodel.GymViewModel

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun Gym() {

    val viewModel = GymViewModel
    val selectedView by viewModel.selectedView.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackBarState = SnackbarHostState()
    val transTime = 500

    viewModel.setSnack( scope, snackBarState )

    Scaffold (
        snackbarHost = { SnackbarHost( snackBarState ) }
    ){ innerpaddings ->

        Image(
            painter = painterResource(R.drawable.f1),
            contentDescription = "Fondo principal",
            modifier = Modifier
                .fillMaxSize()
                .padding(innerpaddings),
            contentScale = ContentScale.Crop
        )

        BoxWithConstraints (
            modifier = Modifier
                .padding(innerpaddings)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){

            val contWidth by animateDpAsState(
                targetValue = if ( selectedView.modInfo.useMaxWidth ) maxWidth else selectedView.modInfo.size.width,
                animationSpec = tween( transTime, easing = EaseOut ),
            )

            val contHeight by animateDpAsState(
                targetValue = if ( selectedView.modInfo.useMaxHeight ) maxHeight else selectedView.modInfo.size.height,
                animationSpec = tween( transTime, easing = EaseOut ),
            )

            AnimatedContent(
                targetState = selectedView,
                contentAlignment = Alignment.Center,
                transitionSpec = {
                    fadeIn( tween( transTime, easing = EaseOut) ).togetherWith( fadeOut( tween( transTime, easing = EaseOut ) ) )
                }
            ) { view ->

                val mod = Modifier
                    .size(
                        width = contWidth,
                        height = contHeight
                    )

                view.Content( modifier = mod )

            }

        }

    }
}

@Preview(
    device = "spec:width=360dp,height=640dp,dpi=320"
)
@Composable
private fun Vertical(){
    Gym()
}

@Preview(
    device = "spec:width=640dp,height=360dp,dpi=320"
)
@Composable
private fun Horizontal(){
    Gym()
}