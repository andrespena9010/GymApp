package com.bochicapp.gym.ui.custom

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bochicapp.gym.data.model.Dat
import com.bochicapp.gym.data.model.Dia
import com.bochicapp.gym.data.model.Info
import com.bochicapp.gym.data.model.comparaObjetos
import com.bochicapp.gym.data.model.firstEmpty
import com.bochicapp.gym.data.model.getDia
import com.bochicapp.gym.ui.model.Views
import com.bochicapp.gym.ui.viewmodel.GymViewModel
import com.bochicapp.gym.ui.viewmodel.GymViewModelClass

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun DiasCompose (
    viewModel: GymViewModelClass = GymViewModel,
    modifier: Modifier
) {

    val info by viewModel.navInfo.collectAsStateWithLifecycle()
    var listId by rememberSaveable { mutableStateOf( "" ) }
    var dias by rememberSaveable { mutableStateOf( listOf<Dia>() ) }
    var diaToEdit by rememberSaveable { mutableStateOf( Dia().toDataList() ) }
    var andtesDe by rememberSaveable { mutableStateOf( Dia().toDataList() ) }
    var changes by rememberSaveable { mutableStateOf( false ) }
    var edit by rememberSaveable { mutableStateOf( false ) }
    var dragging by rememberSaveable { mutableStateOf( false ) }
    val fondo by animateColorAsState(
        targetValue = if ( edit || dragging ) Color( 0x55000000 ) else Color( 0xCCFFFFFF ),
        animationSpec = tween(durationMillis = 1000)
    )
    var labelColor = if ( edit ) Color.White else Color.Black
    var draggingIndex by remember { mutableIntStateOf( -1 ) }
    val heightDayPx = with( LocalDensity.current ) { 50.dp.toPx() }
    var currentPxY by remember { mutableFloatStateOf( 0f ) }

    fun validaCambios () {
        changes = !comparaObjetos( diaToEdit, andtesDe )
    }

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

        AnimatedContent(
            targetState = edit,
            modifier = Modifier
                .zIndex(1f)
                .padding(25.dp)
                .align(Alignment.BottomStart)
                .size(50.dp)
                .clickable(
                    onClick = {
                        andtesDe = Dia().toDataList()
                        diaToEdit = Dia().toDataList()
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
                            val rellenar = firstEmpty( diaToEdit )
                            if ( rellenar == null ){
                                viewModel.updateDia ( getDia( diaToEdit ), listId ){ dia ->
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
                    text = "Crear/Modificar Dia"
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
                Text("Dias")
            }

            if ( !edit ){

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

                        itemsIndexed ( dias ){ index, dia ->

                            var detalle by remember { mutableStateOf(false) }
                            val dragged = draggingIndex == index

                            Column (
                                modifier = Modifier
                                    .heightIn( max = if ( !detalle ) 50.dp else Dp.Unspecified)
                                    .then(
                                        if ( !detalle ){
                                            Modifier.pointerInput(Unit) {
                                                detectDragGestures(
                                                    onDragStart = { offset ->
                                                        dragging = true
                                                        currentPxY = offset.y + ( index * heightDayPx )
                                                        draggingIndex = index
                                                    },
                                                    onDragEnd = {
                                                        dragging = false
                                                        draggingIndex = -1
                                                        viewModel.updateListOrder(
                                                            idList = listId,
                                                            list = dias.map { it.id }
                                                        )
                                                    },
                                                    onDragCancel = {
                                                        dragging = false
                                                        draggingIndex = -1
                                                    },
                                                    onDrag = { change, offset ->
                                                        change.consume()
                                                        currentPxY += offset.y
                                                        val newIndex = ( currentPxY / heightDayPx ).toInt().coerceAtMost( dias.size - 1 )
                                                        if ( newIndex != draggingIndex ) {
                                                            val new = dias.toMutableList()
                                                            val temp = new[newIndex]
                                                            new[newIndex] = new[draggingIndex]
                                                            new[draggingIndex] = temp
                                                            draggingIndex = newIndex
                                                            dias = new.toList()
                                                        }
                                                    }
                                                )
                                            }
                                        } else {
                                            Modifier
                                        }
                                    )
                            ){

                                HorizontalDivider()
                                Row(
                                    modifier = Modifier
                                        .background( if ( dragged ) Color.White else if ( dragging ) fondo else Color.White )
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
                                        text = "${ index + 1 } -> ${ dia.guposmusculares }"
                                    )
                                    Icon(
                                        modifier = Modifier
                                            .clickable(
                                                onClick = {
                                                    andtesDe = dia.toDataList()
                                                    diaToEdit = dia.toDataList()
                                                    validaCambios()
                                                    edit = true
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

                                    val diaDetails = dia.toDataList()

                                    LazyColumn (
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height( 100.dp )
                                            .background( Color(0x11000000) ),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ){
                                        items ( diaDetails ){ campo ->

                                            when ( campo.type ){

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
                                                                            //campo.action( viewModel, Options.GoTo, listId )
                                                                        }
                                                                    ),
                                                                contentAlignment = Alignment.Center
                                                            ){
                                                                Text(
                                                                    modifier = Modifier
                                                                        .padding( 10.dp ),
                                                                    text = "Ver ejercicios"
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

            } else {

                LazyColumn (
                    modifier = Modifier
                        .padding( 10.dp )
                        .fillMaxHeight(0.8f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    items ( diaToEdit ){ dia ->

                        when ( dia.type ){

                            Dat.Txt -> {

                                Column (
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ){
                                    HorizontalDivider()
                                    Text(
                                        text = dia.name,
                                        fontWeight = FontWeight.Bold,
                                        color = labelColor
                                    )
                                    BasicTextField(
                                        modifier = Modifier
                                            .padding(5.dp)
                                            .fillMaxWidth()
                                            .clip( RoundedCornerShape( 5.dp ) )
                                            .background( if ( edit ) Color( 0xCCFFFFFF ) else Color.Transparent ),
                                        value = dia.value.value.toString(),
                                        onValueChange = {
                                            dia.value.value = it
                                            validaCambios()
                                        },
                                        enabled = edit
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