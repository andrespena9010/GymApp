package com.bochicapp.gym.ui.model

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.bochicapp.gym.ui.custom.*
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

interface GymView {

    val modInfo: ModInfo

    @Composable
    fun Content(
        modifier: Modifier,
        lastId: String = ""
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
            modifier: Modifier,
            lastId: String
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
            modifier: Modifier,
            lastId: String
        ) {

            modifier
                .fillMaxSize()
                .background( MaterialTheme.colorScheme.background )

            Ejecucion(
                modifier = modifier
            )
        }

    }

    object InfoUsuarioV: GymView {

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
            modifier: Modifier,
            lastId: String
        ) {

            modifier
                .fillMaxSize()
                .background( MaterialTheme.colorScheme.background )

            InfoUsuario(
                modifier = modifier
            )
        }

    }

    object TomaDatosV: GymView {

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
            modifier: Modifier,
            lastId: String
        ) {

            modifier
                .fillMaxSize()
                .background( MaterialTheme.colorScheme.background )

            TomaDatos(
                modifier = modifier,
                lastId = lastId
            )
        }

    }

}

data class ModInfo(
    val size: DpSize,
    val useMaxWidth: Boolean,
    val useMaxHeight: Boolean
)

fun <T : Any> getProps(obj: T): List<KProperty1<out T, *>> {

    val kClass = obj::class
    val propiedades = kClass.memberProperties

    val ordenConstructor = kClass.primaryConstructor
        ?.parameters?.mapNotNull { it.name }
        ?: emptyList()

    return propiedades.sortedBy { prop ->
        ordenConstructor.indexOf(prop.name).takeIf { it >= 0 } ?: Int.MAX_VALUE
    }

}