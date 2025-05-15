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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.graphics.createBitmap
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bochicapp.gym.data.model.Dat
import com.bochicapp.gym.data.model.Info
import com.bochicapp.gym.data.model.Options
import com.bochicapp.gym.data.model.Rutina
import com.bochicapp.gym.data.model.comparaObjetos
import com.bochicapp.gym.data.model.firstEmpty
import com.bochicapp.gym.data.model.getRutina
import com.bochicapp.gym.data.model.toAAMMDDHHMMSS
import com.bochicapp.gym.ui.model.GymView
import com.bochicapp.gym.ui.model.Views
import com.bochicapp.gym.ui.viewmodel.GymViewModel
import com.bochicapp.gym.ui.viewmodel.GymViewModelClass

@Composable
fun RutinasCompose(
    viewModel: GymViewModelClass = GymViewModel,
    modifier: Modifier
){

    val user by viewModel.usuario.collectAsStateWithLifecycle()
    val info by viewModel.navInfo.collectAsStateWithLifecycle()
    var listId by rememberSaveable { mutableStateOf( "" ) }
    var rutinas by rememberSaveable { mutableStateOf( listOf<Rutina>() ) }
    var rutinaToEdit by rememberSaveable { mutableStateOf( Rutina().toDataList() ) }
    var andtesDe by rememberSaveable { mutableStateOf( Rutina().toDataList() ) }
    var changes by rememberSaveable { mutableStateOf( false ) }
    var edit by rememberSaveable { mutableStateOf( false ) }
    val fondo by animateColorAsState(
        targetValue = if (edit) Color( 0x55000000 ) else Color( 0xCCFFFFFF ),
        animationSpec = tween(durationMillis = 1000)
    )
    var labelColor = if ( edit ) Color.White else Color.Black

    fun validaCambios () {
        changes = !comparaObjetos( rutinaToEdit, andtesDe )
    }

    LaunchedEffect( info ) {
        listId = user.rutinas
        viewModel.getRutinas( listId ){ items ->
            if ( info.viewObject ){
                items.find { it.id == info.targetId }?.let { rutina ->
                    rutinas = listOf( rutina )
                }
            } else {
                rutinas = items
            }
        }
    }

    BackHandler {
        if ( info.launcherView == null ){
            viewModel.goTo( Views.UsuarioView )
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

        if ( !info.selection && !info.viewObject){

            AnimatedContent(
                targetState = edit,
                modifier = Modifier
                    .zIndex(1f)
                    .padding(25.dp)
                    .align(Alignment.BottomStart)
                    .size(50.dp)
                    .clickable(
                        onClick = {
                            andtesDe = Rutina().toDataList()
                            rutinaToEdit = Rutina().toDataList()
                            validaCambios()
                            edit = !edit
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

        }

        if ( changes && edit ){

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
                            val rellenar = firstEmpty( rutinaToEdit )
                            if ( rellenar == null ){
                                viewModel.updateRutina ( getRutina( rutinaToEdit ), listId ){ rutina ->
                                    val new = rutinas.toMutableList()
                                    val i = new.indexOfFirst { it.id == rutina.id }
                                    if ( i != -1 ) {
                                        new[i] = rutina
                                    } else {
                                        new.add(rutina)
                                    }
                                    rutinaToEdit = Rutina().toDataList()
                                    rutinas = new.toList()
                                    edit = false
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
                    text = "Crear/Modificar Rutina"
                )
            }
        }

        Column (
            modifier = modifier
                .fillMaxSize()
                .background( fondo ),
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .shadow(5.dp)
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Text("Rutinas")
            }

            if ( !edit ){

                if ( rutinas.isEmpty()  ){

                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        Text("No hay rutinas")
                    }

                } else {

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
                                if ( info.selection ){

                                    Box(
                                        modifier = Modifier
                                            .padding( 10.dp )
                                            .shadow(
                                                elevation = 5.dp,
                                                shape = RoundedCornerShape( 20.dp )
                                            )
                                            .clip( RoundedCornerShape( 20.dp ) )
                                            .background( Color( 0xFFCCCCCC ) )
                                            .clickable(
                                                onClick = {
                                                    viewModel.goTo(
                                                        view = info.launcherView!!,
                                                        Info(
                                                            launcherId = rutina.id,
                                                            targetId = info.launcherViewId,
                                                            responseToId = info.launcherId
                                                        )
                                                    )
                                                }
                                            ),
                                        contentAlignment = Alignment.Center
                                    ){
                                        Text(
                                            modifier = Modifier
                                                .padding( 10.dp ),
                                            text = "Seleccionar"
                                        )
                                    }

                                } else {

                                    if ( !info.viewObject ){

                                        Icon(
                                            modifier = Modifier
                                                .clickable(
                                                    onClick = {
                                                        andtesDe = rutina.toDataList()
                                                        rutinaToEdit = rutina.toDataList()
                                                        validaCambios()
                                                        edit = true
                                                    }
                                                )
                                                .padding( 5.dp ),
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = ""
                                        )

                                    }

                                }
                            }

                            AnimatedVisibility(
                                visible = detalle,
                                enter = expandVertically() + fadeIn(),
                                exit = shrinkVertically() + fadeOut()
                            ) {

                                val rutinaData by rememberSaveable { mutableStateOf( rutina.toDataList() ) }

                                LazyColumn (
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height( 600.dp )
                                        .background( Color(0x11000000) ),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ){
                                    items ( rutinaData ){ campo ->

                                        when ( campo.type ){

                                            Dat.Img -> {

                                                var image by remember { mutableStateOf( createBitmap(100, 100).asImageBitmap() ) }
                                                viewModel.getPng(
                                                    id = campo.value.value.toString(),
                                                    onLoad = { bitmap ->
                                                        image = bitmap
                                                    }
                                                )

                                                Image(
                                                    bitmap = image,
                                                    contentDescription = campo.name
                                                )

                                                Spacer( modifier = Modifier.height(15.dp) )

                                            }

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
                                                            .background( if ( edit ) Color( 0xCCFFFFFF ) else Color.Transparent ),
                                                        value = campo.value.value.toString(),
                                                        onValueChange = {},
                                                        enabled = edit
                                                    )
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
                                                                        campo.action( viewModel, Options.GoTo, listId )
                                                                    }
                                                                ),
                                                            contentAlignment = Alignment.Center
                                                        ){
                                                            Text(
                                                                modifier = Modifier
                                                                    .padding( 10.dp ),
                                                                text = "Ver dias"
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

            } else {

                LazyColumn (
                    modifier = Modifier
                        .padding( 10.dp )
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    items ( rutinaToEdit ){ campo ->

                        when ( campo.type ){

                            Dat.Img -> {

                                var image by remember { mutableStateOf( createBitmap(100, 100).asImageBitmap() ) }
                                viewModel.getPng(
                                    id = campo.value.value.toString(),
                                    onLoad = { bitmap ->
                                        image = bitmap
                                    }
                                )

                                Image(
                                    bitmap = image,
                                    contentDescription = campo.name
                                )

                                Spacer( modifier = Modifier.height(15.dp) )

                            }

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
                                            .background( if ( edit ) Color( 0xCCFFFFFF ) else Color.Transparent ),
                                        value = campo.value.value.toString(),
                                        onValueChange = {
                                            campo.value.value = it
                                            validaCambios()
                                        },
                                        enabled = edit
                                    )
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
                                                        campo.action( viewModel, Options.GoTo, listId )
                                                    }
                                                ),
                                            contentAlignment = Alignment.Center
                                        ){
                                            Text(
                                                modifier = Modifier
                                                    .padding( 10.dp ),
                                                text = "Ver dias"
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