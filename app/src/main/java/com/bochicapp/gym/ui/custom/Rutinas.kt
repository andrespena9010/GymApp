package com.bochicapp.gym.ui.custom

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bochicapp.gym.data.model.Dat
import com.bochicapp.gym.data.model.ProximoObjetivo
import com.bochicapp.gym.data.model.Rutina
import com.bochicapp.gym.data.model.comparaObjetos
import com.bochicapp.gym.data.model.toAAMMDDHHMMSS
import com.bochicapp.gym.ui.viewmodel.GymViewModel
import com.bochicapp.gym.ui.viewmodel.GymViewModelClass

@Composable
fun RutinasCompose(
    viewModel: GymViewModelClass = GymViewModel,
    modifier: Modifier
){

    val navViews by viewModel.navViews.collectAsStateWithLifecycle()
    val selection by viewModel.selection.collectAsStateWithLifecycle()
    var rutinas by rememberSaveable { mutableStateOf( listOf<Rutina>() ) }


    // TODO: validar que si esta en opcion de seleccionar, devuelve el id al click


    var listId by rememberSaveable { mutableStateOf( "" ) }
    var objToEdit by rememberSaveable { mutableStateOf( ProximoObjetivo().toDataList() ) }
    var andtesDe by rememberSaveable { mutableStateOf( ProximoObjetivo().toDataList() ) }
    var changes by rememberSaveable { mutableStateOf( false ) }
    var editObj by rememberSaveable { mutableStateOf( false ) }
    val fondo by animateColorAsState(
        targetValue = if (editObj) Color( 0x55000000 ) else Color( 0xCCFFFFFF ),
        animationSpec = tween(durationMillis = 1000)
    )
    var labelColor = if ( editObj ) Color.White else Color.Black

    fun validaCambios () {
        changes = !comparaObjetos( objToEdit, andtesDe )
    }

    LaunchedEffect( navViews ) {
        val newId = navViews.keys.lastOrNull()
        newId?.let {
            listId = newId
            viewModel.getRutinas( newId ){ items ->
                rutinas = items
            }
        }
    }

    BackHandler {
        viewModel.goBack()
    }

    Box(
        modifier = modifier
            .background( fondo ),
        contentAlignment = Alignment.Center
    ){

        LazyColumn (
            modifier = Modifier
                .padding( 10.dp )
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            items ( rutinas ){ rutina ->

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
                        text = rutina.fechamodificacion.toAAMMDDHHMMSS()
                    )
                    Icon(
                        modifier = Modifier
                            .clickable(
                                onClick = {
                                    andtesDe = rutina.toDataList()
                                    tomaToEdit = rutina.toDataList()
                                    validaCambios()
                                    editToma = true
                                }
                            )
                            .padding( 5.dp ),
                        imageVector = Icons.Default.Edit,
                        contentDescription = ""
                    )
                }

                AnimatedVisibility(
                    visible = detalle,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {

                    val tomaDetails by rememberSaveable { mutableStateOf( rutina.toDataList() ) }

                    LazyColumn (
                        modifier = Modifier
                            .fillMaxWidth()
                            .height( 600.dp )
                            .background( Color(0x11000000) ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        items ( tomaDetails ){ campo ->

                            when ( campo.type ){

                                Dat.Txt -> {

                                    Column (
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ){
                                        HorizontalDivider()
                                        Text(
                                            text = campo.name,
                                            fontWeight = FontWeight.Bold,
                                            color = labelColor
                                        )
                                        BasicTextField(
                                            modifier = Modifier
                                                .padding(5.dp)
                                                .fillMaxWidth()
                                                .clip( RoundedCornerShape( 5.dp ) )
                                                .background( if ( editToma ) Color( 0xCCFFFFFF ) else Color.Transparent ),
                                            value = campo.value.value.toString(),
                                            onValueChange = {},
                                            enabled = editToma
                                        )
                                    }

                                }

                                Dat.Dobl -> {

                                    Column (
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ){
                                        HorizontalDivider()
                                        Text(
                                            text = campo.name,
                                            fontWeight = FontWeight.Bold,
                                            color = labelColor
                                        )
                                        BasicTextField(
                                            modifier = Modifier
                                                .padding(5.dp)
                                                .fillMaxWidth()
                                                .clip( RoundedCornerShape( 5.dp ) )
                                                .background( if ( editToma ) Color( 0xCCFFFFFF ) else Color.Transparent ),
                                            value = campo.value.value.toString(),
                                            onValueChange = {},
                                            enabled = editToma,
                                            keyboardOptions = KeyboardOptions( keyboardType = KeyboardType.Number )
                                        )
                                    }

                                }

                                Dat.Obj -> {

                                    Column (
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ){

                                        HorizontalDivider()
                                        Text(
                                            text = campo.name,
                                            fontWeight = FontWeight.Bold,
                                            color = labelColor
                                        )
                                        Text(
                                            text = campo.value.value.toString()
                                        )
                                        if ( campo.value.value.toString().isNotEmpty() ){

                                            Box(
                                                modifier = Modifier
                                                    .padding( 10.dp )
                                                    .align( Alignment.CenterHorizontally )
                                                    .shadow(
                                                        elevation = 5.dp,
                                                        shape = RoundedCornerShape( 20.dp )
                                                    )
                                                    .clip( RoundedCornerShape( 20.dp ) )
                                                    .background( Color( 0xFFCCCCCC ) )
                                                    .clickable(
                                                        onClick = {
                                                            // TODO: pendiente
                                                        }
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ){
                                                Text(
                                                    modifier = Modifier
                                                        .padding( 10.dp ),
                                                    text = "Ver rutina"
                                                )
                                            }

                                        }

                                    }

                                }

                                Dat.Ls -> {

                                    Column (
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ){

                                        HorizontalDivider()
                                        Text(
                                            text = campo.name,
                                            fontWeight = FontWeight.Bold,
                                            color = labelColor
                                        )
                                        if ( campo.value.value.toString().isNotEmpty() ){

                                            Box(
                                                modifier = Modifier
                                                    .padding( 10.dp )
                                                    .align( Alignment.CenterHorizontally )
                                                    .shadow(
                                                        elevation = 5.dp,
                                                        shape = RoundedCornerShape( 20.dp )
                                                    )
                                                    .clip( RoundedCornerShape( 20.dp ) )
                                                    .background( Color( 0xFFCCCCCC ) )
                                                    .clickable(
                                                        onClick = {
                                                            campo.onClick( viewModel )
                                                        }
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ){
                                                Text(
                                                    modifier = Modifier
                                                        .padding( 10.dp ),
                                                    text = "Ver Objetivos"
                                                )
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