package com.bochicapp.gym.ui.custom

import android.annotation.SuppressLint
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
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.bochicapp.gym.data.model.*
import com.bochicapp.gym.ui.model.Views
import com.bochicapp.gym.ui.viewmodel.GymViewModel
import com.bochicapp.gym.ui.viewmodel.GymViewModelClass

@SuppressLint("UnrememberedMutableState")
@Composable
fun TomaDatosFisicosCompose (
    viewModel: GymViewModelClass = GymViewModel,
    modifier: Modifier
) {

    val navIds = viewModel.navIds.collectAsStateWithLifecycle()
    var listId by rememberSaveable { mutableStateOf( "" ) }
    var tomasDeDatos by rememberSaveable { mutableStateOf( listOf<TomaDatosFisicos>() /*List(15){ TomaDatosFisicos() }*/ ) }
    var tomaToEdit by rememberSaveable { mutableStateOf( TomaDatosFisicos().toDataList() ) }
    var andtesDe by rememberSaveable { mutableStateOf( TomaDatosFisicos().toDataList() ) }
    var changes by rememberSaveable { mutableStateOf( false ) }
    var editToma by rememberSaveable { mutableStateOf( false ) }
    val fondo by animateColorAsState(
        targetValue = if (editToma) Color( 0x55000000 ) else Color( 0xCCFFFFFF ),
        animationSpec = tween(durationMillis = 1000)
    )
    var labelColor = if ( editToma ) Color.White else Color.Black

    fun validaCambios () {
        changes = !comparaObjetos( tomaToEdit, andtesDe )
    }

    LaunchedEffect( navIds ) {
        val newId = navIds.value.lastOrNull()
        newId?.let {
            listId = newId
            viewModel.getTomasDeDatos( newId ){ tomas ->
                tomasDeDatos = tomas
            }
        }
    }

    BackHandler {
        viewModel.goTo(
            view = Views.InfoUsuarioV,
            idToRemove = listId
        )
    }

    Box(
        modifier = modifier
            .background( fondo ),
        contentAlignment = Alignment.Center
    ){

        AnimatedContent(
            targetState = editToma,
            modifier = Modifier
                .zIndex(1f)
                .padding(25.dp)
                .align(Alignment.BottomStart)
                .size(50.dp)
                .clickable(
                    onClick = {
                        andtesDe = TomaDatosFisicos().toDataList()
                        editToma = !editToma
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

        if ( changes && editToma ){
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
                            val rellenar = firstEmpty( tomaToEdit )
                            if ( rellenar == null ){
                                viewModel.updateTomaDatosFisicos ( getTomaDatosFisicos( tomaToEdit ), listId ){ toma ->
                                    val new = tomasDeDatos.toMutableList()
                                    val i = new.indexOfFirst { it.id == toma.id }
                                    if ( i != -1 ) {
                                        new[i] = toma // TODO: Arreglar!!! algo pasa se duplican
                                    } else {
                                        new.add(toma)
                                    }
                                    tomaToEdit = TomaDatosFisicos().toDataList()
                                    tomasDeDatos = new.toList()
                                    editToma = false
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
                    text = "Crear/Modificar Toma"
                )
            }
        }

        if ( !editToma ){

            if ( tomasDeDatos.isEmpty()  ){

                Text("No hay tomas de datos")

            } else {

                LazyColumn (
                    modifier = Modifier
                        .padding( 10.dp )
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){

                    items ( tomasDeDatos ){ toma ->

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
                                text = toma.fechatoma.toAAMMDDHHMMSS()
                            )
                            Icon(
                                modifier = Modifier
                                    .clickable(
                                        onClick = {
                                            andtesDe = toma.toDataList()
                                            tomaToEdit = toma.toDataList()
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

                            val tomaDetails = toma.toDataList()

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
                                                    onValueChange = {
                                                        campo.value.value = it
                                                        //validaCambios()
                                                    },
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
                                                    onValueChange = {
                                                        campo.value.value = if ( Regex("^$|^\\d+|\\d+\\.|\\d+\\.\\d+$").matches( it ) ) it else "0"
                                                        //validaCambios()
                                                    },
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
                                                            .background( Color( 0xFFCCCCCC ) ),
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
                                                            .background( Color( 0xFFCCCCCC ) ),
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

        if ( editToma ){

            LazyColumn (
                modifier = Modifier
                    .padding( 10.dp )
                    .fillMaxHeight(0.8f),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                items ( tomaToEdit ){ toma ->

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
                                        .background( if ( editToma ) Color( 0xCCFFFFFF ) else Color.Transparent ),
                                    value = toma.value.value.toString(),
                                    onValueChange = {
                                        toma.value.value = it
                                        validaCambios()
                                    },
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
                                    text = toma.name,
                                    fontWeight = FontWeight.Bold,
                                    color = labelColor
                                )
                                BasicTextField(
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .fillMaxWidth()
                                        .clip( RoundedCornerShape( 5.dp ) )
                                        .background( if ( editToma ) Color( 0xCCFFFFFF ) else Color.Transparent ),
                                    value = toma.value.value.toString(),
                                    onValueChange = {
                                        toma.value.value = if ( Regex("^$|^\\d+|\\d+\\.|\\d+\\.\\d+$").matches( it ) ) it else "0"
                                        validaCambios()
                                    },
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
                                    text = toma.name,
                                    fontWeight = FontWeight.Bold,
                                    color = labelColor
                                )
                                Text(
                                    text = toma.value.value.toString()
                                )
                                Box(
                                    modifier = Modifier
                                        .padding( 10.dp )
                                        .align( Alignment.CenterHorizontally )  // TODO: si hay rutina relacionada, poder dar link para ver detalles sino permitir seleccionar
                                        .shadow(
                                            elevation = 5.dp,
                                            shape = RoundedCornerShape( 20.dp )
                                        )
                                        .clip( RoundedCornerShape( 20.dp ) )
                                        .background( Color( 0xFFCCCCCC ) ),
                                    contentAlignment = Alignment.Center
                                ){
                                    Text(
                                        modifier = Modifier
                                            .padding( 10.dp ),
                                        text = "Seleccionar rutina"
                                    )
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
                                    text = toma.name,
                                    fontWeight = FontWeight.Bold,
                                    color = labelColor
                                )
                                Box(
                                    modifier = Modifier
                                        .padding( 10.dp )
                                        .align( Alignment.CenterHorizontally )
                                        .shadow(
                                            elevation = 5.dp,
                                            shape = RoundedCornerShape( 20.dp )
                                        )
                                        .clip( RoundedCornerShape( 20.dp ) )
                                        .background( Color( 0xFFCCCCCC ) ),
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

            /*if ( editObjetivo ){

                LazyColumn (
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxHeight(0.8f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    items ( objetivoToAdd ){ objetivo ->

                        when ( objetivo.type ){

                            Dat.Txt -> {

                                Column (
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ){
                                    HorizontalDivider()
                                    Text(
                                        text = objetivo.name,
                                        fontWeight = FontWeight.Bold,
                                        color = labelColor
                                    )
                                    BasicTextField(
                                        modifier = Modifier
                                            .padding(5.dp)
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(5.dp))
                                            .background(if (editToma) Color(0xCCFFFFFF) else Color.Transparent),
                                        value = objetivo.value.value.toString(),
                                        onValueChange = { objetivo.value.value = it },
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
                                        text = objetivo.name,
                                        fontWeight = FontWeight.Bold,
                                        color = labelColor
                                    )
                                    BasicTextField(
                                        modifier = Modifier
                                            .padding(5.dp)
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(5.dp))
                                            .background(if (editToma) Color(0xCCFFFFFF) else Color.Transparent),
                                        value = objetivo.value.value.toString(),
                                        onValueChange = {
                                            objetivo.value.value = if ( Regex("^$|^\\d+|\\d+\\.|\\d+\\.\\d+$").matches( it ) ) it else "0"
                                        },
                                        enabled = editToma,
                                        keyboardOptions = KeyboardOptions( keyboardType = KeyboardType.Number )
                                    )
                                }

                            }

                            Dat.Compose -> {

                                Row(
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ){
                                    // agregar botones de crear objetivo y cancelar. con advertencia de que ningun dato puede estar vacio.
                                    Box(
                                        modifier = Modifier
                                            .shadow(
                                                elevation = 5.dp,
                                                shape = RoundedCornerShape( 20.dp )
                                            )
                                            .clip( RoundedCornerShape( 20.dp ) )
                                            .background( Color( 0xFFCCCCCC ) )
                                            .clickable(
                                                onClick = {
                                                    editObjetivo = false
                                                }
                                            ),
                                        contentAlignment = Alignment.Center
                                    ){
                                        Text(
                                            modifier = Modifier
                                                .padding(10.dp),
                                            text = "Cancelar"
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .shadow(
                                                elevation = 5.dp,
                                                shape = RoundedCornerShape( 20.dp )
                                            )
                                            .clip( RoundedCornerShape( 20.dp ) )
                                            .background( Color( 0xFFCCCCCC ) )
                                            .clickable(
                                                onClick = {
                                                    val objetivoPendiente = firstEmpty( objetivoToAdd )
                                                    if ( objetivoPendiente == null ){
                                                        val objToAdd = getProximoObjetivo( objetivoToAdd )
                                                        objToAdd.fechacreacion = LocalDateTime.now().format( DateTimeFormatter.ISO_DATE_TIME )
                                                        viewModel.updateProximoObjetivo( objToAdd ){ objetivo ->

                                                        }
                                                    } else {
                                                        viewModel.sendSnackMessage("El espacio ${ objetivoPendiente.name } no puede estar vacio.")
                                                    }
                                                }
                                            ),
                                        contentAlignment = Alignment.Center
                                    ){
                                        Text(
                                            modifier = Modifier
                                                .padding(10.dp),
                                            text = "Crear objetivo"
                                        )
                                    }

                                }

                            }

                        }

                    }
                }

            } else {


            }*/

        }

    }

}

@Preview(
    device = "spec:width=360dp,height=640dp,dpi=320"
)
@Composable
private fun Vertical(){
    Views.TomaDatosFisicosView.Content(Modifier)
}

@Preview(
    device = "spec:width=640dp,height=360dp,dpi=320"
)
@Composable
private fun Horizontal(){
    Views.TomaDatosFisicosView.Content(Modifier)
}