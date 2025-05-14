package com.bochicapp.gym.ui.custom

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bochicapp.gym.data.model.Dat
import com.bochicapp.gym.data.model.Dia
import com.bochicapp.gym.data.model.Info
import com.bochicapp.gym.ui.model.Views
import com.bochicapp.gym.ui.viewmodel.GymViewModel
import com.bochicapp.gym.ui.viewmodel.GymViewModelClass

@Composable
fun DiasCompose (
    viewModel: GymViewModelClass = GymViewModel,
    modifier: Modifier
) {

    val info by viewModel.navInfo.collectAsStateWithLifecycle()
    var listId by rememberSaveable { mutableStateOf( "" ) }
    var dias by rememberSaveable { mutableStateOf( listOf<Dia>() ) }

    LaunchedEffect( info ) {
        info.targetId?.let {
            viewModel.getDias( info.targetId!! ){ items ->
                listId = info.targetId!!
                dias = items
            }
        }
    }

    BackHandler {
        if ( info.launcherView == null ){
            viewModel.goTo( Views.RutinasView )
        } else {
            viewModel.goTo(
                view = info.launcherView!!,
                navInfo = Info(
                    targetId = info.launcherViewId
                )
            )
        }
    }

    Box {

        Icon(
            modifier = Modifier
                .zIndex(1f)
                .padding(25.dp)
                .align(Alignment.BottomStart)
                .size(50.dp)
                .shadow(
                    elevation = 5.dp,
                    shape = RoundedCornerShape(20.dp)
                )
                .clip(RoundedCornerShape(20.dp))
                .background( Color( 0xFF99CC99 ) )
                .clickable(
                    onClick = {
                        /*viewModel.updateDia ( getDia( diaToEdit ), listId ){ dia ->
                            val new = dias.toMutableList()
                            val i = new.indexOfFirst { it.id == dia.id }
                            if ( i != -1 ) {
                                new[i] = dia
                            } else {
                                new.add(dia)
                            }
                            diaToEdit = Dia().toDataList()
                            dias = new.toList()
                            edit = false
                        }*/
                    }
                ),
            imageVector = Icons.Default.Add,
            contentDescription = ""
        )

        Column (
            modifier = modifier
                .fillMaxSize()
                .background( Color( 0xCCFFFFFF ) ),
        ){

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .shadow( 5.dp )
                    .background( MaterialTheme.colorScheme.background ),
                contentAlignment = Alignment.Center
            ){
                Text("Dias")
            }

            if ( dias.isEmpty()  ){

                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Text("No hay dias")
                }

            } else {

                LazyColumn (
                    modifier = Modifier
                        .padding( 10.dp )
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){

                    items ( dias ){ dia ->

                        var detalle by remember { mutableStateOf(false) }

                        HorizontalDivider()
                        Row(
                            modifier = Modifier
                                .background( Color.White )
                                .fillMaxWidth()
                                .clickable(
                                    onClick = {
                                        detalle = !detalle
                                    }
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            Text(
                                modifier = Modifier
                                    .padding( 10.dp ),
                                text = dia.numerodia.toString()
                            )
                            Icon(
                                modifier = Modifier
                                    .clickable(
                                        onClick = {
                                            // TODO: borrar dia
                                        }
                                    )
                                    .padding( 5.dp ),
                                imageVector = Icons.Default.Close,
                                contentDescription = ""
                            )
                        }

                        AnimatedVisibility(
                            visible = detalle,
                            enter = expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut()
                        ) {

                            val diaDetails = dia.toDataList()

                            LazyColumn (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height( 600.dp )
                                    .background( Color(0x11000000) ),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ){
                                items ( diaDetails ){ campo ->

                                    when ( campo.type ){

                                        Dat.Entero -> {

                                            Column (
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                            ){
                                                HorizontalDivider()
                                                Text(
                                                    text = campo.name,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text( campo.value.value.toString() )
                                            }

                                        }

                                    }

                                }
                            }

                        }

                    }

                }

            }

        }

    }

}