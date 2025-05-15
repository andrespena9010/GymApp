package com.bochicapp.gym.ui.custom

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bochicapp.gym.data.model.Dat
import com.bochicapp.gym.data.model.Info
import com.bochicapp.gym.data.model.ProximoObjetivo
import com.bochicapp.gym.data.model.TomaDatosFisicos
import com.bochicapp.gym.data.model.comparaObjetos
import com.bochicapp.gym.data.model.firstEmpty
import com.bochicapp.gym.data.model.getProximoObjetivo
import com.bochicapp.gym.data.model.toAAMMDDHHMMSS
import com.bochicapp.gym.ui.model.Views
import com.bochicapp.gym.ui.viewmodel.GymViewModel
import com.bochicapp.gym.ui.viewmodel.GymViewModelClass

@Composable
fun ProximosObjetivosCompose (
    viewModel: GymViewModelClass = GymViewModel,
    modifier: Modifier
) {

    val info by viewModel.navInfo.collectAsStateWithLifecycle()
    var listId by rememberSaveable { mutableStateOf( "" ) }
    var objetivos by rememberSaveable { mutableStateOf( listOf<ProximoObjetivo>() ) }
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

    LaunchedEffect( info ) {
        info.targetId?.let {
            viewModel.getProximosObjetivos( info.targetId!! ){ items ->
                listId = info.targetId!!
                objetivos = items
            }
        }
    }

    BackHandler {
        if ( info.launcherView == null ){
            viewModel.goTo( Views.TomaDatosFisicosView )
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

        AnimatedContent(
            targetState = editObj,
            modifier = Modifier
                .zIndex(1f)
                .padding(25.dp)
                .align(Alignment.BottomStart)
                .size(50.dp)
                .clickable(
                    onClick = {
                        andtesDe = ProximoObjetivo().toDataList()
                        objToEdit = ProximoObjetivo().toDataList()
                        validaCambios()
                        editObj = !editObj
                    }
                ),
            transitionSpec = {
                slideInHorizontally( tween( 300, easing = EaseOut) ).togetherWith(
                    slideOutHorizontally( tween( 300, easing = EaseOut ) )
                )
            }
        ) { edit ->

            if ( edit ){

                Icon(
                    modifier = Modifier
                        .shadow(
                            elevation = 5.dp,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clip(RoundedCornerShape(20.dp))
                        .background( Color( 0xFFCC9999 ) )
                        .padding( 5.dp ),
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = ""
                )

            } else {

                Icon(
                    modifier = Modifier
                        .shadow(
                            elevation = 5.dp,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clip(RoundedCornerShape(20.dp))
                        .background( Color( 0xFF99CC99 ) )
                        .padding( 5.dp ),
                    imageVector = Icons.Default.Add,
                    contentDescription = ""
                )

            }

        }

        if ( changes && editObj ){
            Box(
                modifier = Modifier
                    .zIndex( 2f )
                    .padding( 10.dp )
                    .align( Alignment.BottomCenter )
                    .shadow(
                        elevation = 5.dp,
                        shape = RoundedCornerShape( 20.dp )
                    )
                    .clip( RoundedCornerShape( 20.dp ) )
                    .background( Color( 0xFF5555CC ) )
                    .clickable(
                        onClick = {
                            val rellenar = firstEmpty( objToEdit )
                            if ( rellenar == null ){
                                viewModel.updateProximoObjetivo ( getProximoObjetivo( objToEdit ), listId ){ objetivo ->
                                    val new = objetivos.toMutableList()
                                    val i = new.indexOfFirst { it.id == objetivo.id }
                                    if ( i != -1 ) {
                                        new[i] = objetivo
                                    } else {
                                        new.add(objetivo)
                                    }
                                    objToEdit = ProximoObjetivo().toDataList()
                                    objetivos = new.toList()
                                    editObj = false
                                }
                            } else {
                                viewModel.sendSnackMessage("Debe rellenar el campo ${ rellenar.name }.")
                            }
                        }
                    ),
                contentAlignment = Alignment.Center
            ){
                Text(
                    modifier = Modifier
                        .padding( 10.dp ),
                    text = "Crear/Modificar Objetivo"
                )
            }
        }

        Column (
            modifier = modifier
                .fillMaxSize()
                .background( fondo ),
        ){

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .shadow( 5.dp )
                    .background( MaterialTheme.colorScheme.background ),
                contentAlignment = Alignment.Center
            ){
                Text("Objetivos")
            }

            if ( !editObj ){

                if ( objetivos.isEmpty()  ){

                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        Text("No hay objetivos")
                    }

                } else {

                    LazyColumn (
                        modifier = Modifier
                            .padding( 10.dp )
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){

                        items ( objetivos ){ objetivo ->

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
                                    text = objetivo.fechamodificacion.toAAMMDDHHMMSS()
                                )
                                Icon(
                                    modifier = Modifier
                                        .clickable(
                                            onClick = {
                                                andtesDe = objetivo.toDataList()
                                                objToEdit = objetivo.toDataList()
                                                validaCambios()
                                                editObj = true
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

                                val tomaDetails = objetivo.toDataList()

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
                                                            .background( if ( editObj ) Color( 0xCCFFFFFF ) else Color.Transparent ),
                                                        value = campo.value.value.toString(),
                                                        onValueChange = {
                                                            campo.value.value = it
                                                        },
                                                        enabled = editObj
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
                                                            .background( if ( editObj ) Color( 0xCCFFFFFF ) else Color.Transparent ),
                                                        value = campo.value.value.toString(),
                                                        onValueChange = {
                                                            campo.value.value = if ( Regex("^$|^\\d+|\\d+\\.|\\d+\\.\\d+$").matches( it ) ) it else "0"
                                                        },
                                                        enabled = editObj,
                                                        keyboardOptions = KeyboardOptions( keyboardType = KeyboardType.Number )
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

            } else {

                LazyColumn (
                    modifier = Modifier
                        .padding( 10.dp )
                        .fillMaxHeight(0.8f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    items ( objToEdit ){ toma ->

                        when ( toma.type ){

                            Dat.Txt -> {

                                Column (
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ){
                                    HorizontalDivider()
                                    Text(
                                        text = toma.name,
                                        fontWeight = FontWeight.Bold,
                                        color = labelColor
                                    )
                                    BasicTextField(
                                        modifier = Modifier
                                            .padding(5.dp)
                                            .fillMaxWidth()
                                            .clip( RoundedCornerShape( 5.dp ) )
                                            .background( if ( editObj ) Color( 0xCCFFFFFF ) else Color.Transparent ),
                                        value = toma.value.value.toString(),
                                        onValueChange = {
                                            toma.value.value = it
                                            validaCambios()
                                        },
                                        enabled = editObj
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
                                        text = toma.name,
                                        fontWeight = FontWeight.Bold,
                                        color = labelColor
                                    )
                                    BasicTextField(
                                        modifier = Modifier
                                            .padding(5.dp)
                                            .fillMaxWidth()
                                            .clip( RoundedCornerShape( 5.dp ) )
                                            .background( if ( editObj ) Color( 0xCCFFFFFF ) else Color.Transparent ),
                                        value = toma.value.value.toString(),
                                        onValueChange = {
                                            toma.value.value = if ( Regex("^$|^\\d+|\\d+\\.|\\d+\\.\\d+$").matches( it ) ) it else "0"
                                            validaCambios()
                                        },
                                        enabled = editObj,
                                        keyboardOptions = KeyboardOptions( keyboardType = KeyboardType.Number )
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