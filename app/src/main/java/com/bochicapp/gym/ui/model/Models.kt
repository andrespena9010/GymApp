package com.bochicapp.gym.ui.model

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.bochicapp.gym.ui.custom.*

interface GymView {

    val modInfo: ModInfo

    @Composable
    fun Content(
        modifier: Modifier
    )

}

sealed class Views {

    object PrincipalV: GymView {

        override val modInfo = ModInfo(
            size = DpSize(
                width = 220.dp,
                height = 0.dp
            ),
            useMaxWidth = false,
            useMaxHeight = true
        )
        @Composable
        override fun Content(
            modifier: Modifier
        ) {

            modifier
                .width( modInfo.size.width )
                .fillMaxHeight()

            Principal(
                modifier = modifier
            )

        }
    }

    object EjecucionV: GymView {

        override val modInfo = ModInfo(
            size = DpSize(
                width = 0.dp,
                height = 0.dp
            ),
            useMaxWidth = true,
            useMaxHeight = true
        )

        @Composable
        override fun Content(
            modifier: Modifier
        ) {

            modifier
                .fillMaxSize()
                .background( MaterialTheme.colorScheme.background )

            Ejecucion(
                modifier = modifier
            )
        }

    }

}

data class ModInfo(
    val size: DpSize,
    val useMaxWidth: Boolean,
    val useMaxHeight: Boolean
)

data class PElement(
    val text: String,
    val onClick: () -> Unit
)