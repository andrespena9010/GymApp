package com.bochicapp.gym.ui.custom

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.bochicapp.gym.data.model.Dat
import com.bochicapp.gym.data.model.DataElement
import com.bochicapp.gym.data.model.ProximoObjetivo
import com.bochicapp.gym.data.model.TomaDatosFisicos
import com.bochicapp.gym.data.model.allComplete
import com.bochicapp.gym.data.model.comparaObjetos
import com.bochicapp.gym.ui.model.Views
import com.bochicapp.gym.ui.viewmodel.GymViewModel
import com.bochicapp.gym.ui.viewmodel.GymViewModelClass

@Composable
fun TomaDatos (
    viewModel: GymViewModelClass = GymViewModel,
    modifier: Modifier
) {

    val usuario by viewModel.usuario.collectAsStateWithLifecycle()
    val tomasList = mutableListOf<List<DataElement<Any>>>()
    usuario.tomadatosfisicos.forEach { toma ->
        tomasList.add( toma.toDataList() )
    }
    val tomas by rememberSaveable { mutableStateOf( tomasList.toList() ) }

    var editToma by rememberSaveable { mutableStateOf( false ) }
    var editObjetivo by rememberSaveable { mutableStateOf( false ) }
    val fondo by animateColorAsState(
        targetValue = if (editToma) Color( 0x55000000 ) else Color( 0xCCFFFFFF ),
        animationSpec = tween(durationMillis = 1000)
    )
    var tomaToAdd by rememberSaveable { mutableStateOf( TomaDatosFisicos().toDataList() ) }
    var objetivoToAdd by rememberSaveable { mutableStateOf( ProximoObjetivo().toDataList() ) }
    var labelColor = if ( editToma ) Color.White else Color.Black

    var saveToma by rememberSaveable { mutableStateOf( false ) }
    var saveObjetivo by rememberSaveable { mutableStateOf( false ) }

    LaunchedEffect( usuario, editToma ) {
        saveToma = allComplete( tomaToAdd )
    }

    LaunchedEffect( usuario, editObjetivo ) {
        saveObjetivo = allComplete( objetivoToAdd )
    }

    BackHandler {
        viewModel.goTo( Views.InfoUsuarioV )
    }

    Box(
        modifier = modifier
            .background( fondo ),
        contentAlignment = Alignment.Center
    ){

        Box(
            modifier = Modifier
                .zIndex( 1f )
                .padding( 15.dp )
                .align( Alignment.TopEnd )
                .size( 50.dp )
                .shadow(
                    elevation = 5.dp,
                    shape = RoundedCornerShape( 20.dp )
                )
                .clip( RoundedCornerShape( 20.dp ) )
                .background( Color( 0xFFCCCCCC ) )
                .clickable(
                    onClick = {
                        editToma = !editToma
                    }
                ),
            contentAlignment = Alignment.Center
        ){
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = ""
            )
        }

        if ( tomas.isEmpty() ){

            if ( !editToma ){

                Text("No hay tomas de datos")

            }

        } else {

            /*LazyColumn (
                modifier = Modifier
                    .padding( 15.dp )
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                items ( tomas ){ toma ->

                    when ( toma.type ){ // TODO: mostrar todas las tomas de datos y permitir editarlas

                        Data.Png -> {

                            var image by remember { mutableStateOf( createBitmap(100, 100).asImageBitmap() ) }
                            viewModel.getPng(
                                id = toma.value?.value.toString(),
                                onLoad = { bitmap ->
                                    image = bitmap
                                }
                            )

                            Image(
                                modifier = Modifier
                                    .size( 200.dp ),
                                bitmap = image,
                                contentDescription = toma.name
                            )

                            Spacer( modifier = Modifier.height(15.dp) )

                        }

                        Data.Txt -> {

                            Column (
                                modifier = Modifier
                                    .fillMaxWidth()
                            ){
                                HorizontalDivider()
                                Text(
                                    text = toma.name,
                                    fontWeight = FontWeight.Bold
                                )
                                BasicTextField(
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .fillMaxWidth()
                                        .clip( RoundedCornerShape( 5.dp ) )
                                        .background( if ( edit ) Color( 0xCCFFFFFF ) else Color.Transparent ),
                                    value = toma.value?.value.toString(),
                                    onValueChange = { toma.value?.value = it },
                                    enabled = edit
                                )
                            }

                        }

                        Data.Ls -> {

                            Column (
                                modifier = Modifier
                                    .fillMaxWidth()
                            ){
                                HorizontalDivider()
                                Text(
                                    text = toma.name,
                                    fontWeight = FontWeight.Bold
                                )
                                Box(
                                    modifier = Modifier
                                        .padding( 10.dp )
                                        .align( Alignment.CenterHorizontally )
                                        .width( 100.dp )
                                        .height( 40.dp )
                                        .shadow(
                                            elevation = 5.dp,
                                            shape = RoundedCornerShape( 20.dp )
                                        )
                                        .clip( RoundedCornerShape( 20.dp ) )
                                        .background( Color( 0xFFCCCCCC ) ),
                                    contentAlignment = Alignment.Center
                                ){
                                    Text("Detalles")
                                }
                            }

                        }

                    }

                }
            }*/

        }

        if ( editToma ){

            if ( editObjetivo ){

                LazyColumn (
                    modifier = Modifier
                        .padding( 15.dp )
                        .fillMaxHeight(0.8f)
                        .fillMaxWidth(0.8f),
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
                                            .clip( RoundedCornerShape( 5.dp ) )
                                            .background( if ( editToma ) Color( 0xCCFFFFFF ) else Color.Transparent ),
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
                                            .clip( RoundedCornerShape( 5.dp ) )
                                            .background( if ( editToma ) Color( 0xCCFFFFFF ) else Color.Transparent ),
                                        value = objetivo.value.value.toString(),
                                        onValueChange = {
                                            objetivo.value.value = if ( Regex("^$|^\\d+|\\d+\\.|\\d+\\.\\d+$").matches( it ) ) it else "0"
                                        },
                                        enabled = editToma,
                                        keyboardOptions = KeyboardOptions( keyboardType = KeyboardType.Number )
                                    )
                                }

                            }

                        }

                    }
                }

                Row(
                    modifier = Modifier
                    ,
                    verticalAlignment =
                ){
                    // agregar botones de crear objetivo y cancelar. con advertencia de que ningun dato puede estar vacio.
                }

            } else {

                LazyColumn (
                    modifier = Modifier
                        .padding( 15.dp )
                        .fillMaxHeight(0.8f)
                        .fillMaxWidth(0.8f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    items ( tomaToAdd ){ toma ->

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
                                        onValueChange = { toma.value.value = it },
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
                                        },
                                        enabled = editToma,
                                        keyboardOptions = KeyboardOptions( keyboardType = KeyboardType.Number )
                                    )
                                }

                            }

                            Dat.Lnk -> {

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
                                            .align( Alignment.CenterHorizontally )
                                            .width( 200.dp )
                                            .height( 40.dp )
                                            .shadow(
                                                elevation = 5.dp,
                                                shape = RoundedCornerShape( 20.dp )
                                            )
                                            .clip( RoundedCornerShape( 20.dp ) )
                                            .background( Color( 0xFFCCCCCC ) ),
                                        contentAlignment = Alignment.Center
                                    ){
                                        Text("Seleccionar rutina")
                                    }

                                }

                            }

                            Dat.Ls -> {

                                Column (
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ){

                                    if ( ( toma.value.value as List<*> ).isNotEmpty() ){

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
                                                .width( 100.dp )
                                                .height( 40.dp )
                                                .shadow(
                                                    elevation = 5.dp,
                                                    shape = RoundedCornerShape( 20.dp )
                                                )
                                                .clip( RoundedCornerShape( 20.dp ) )
                                                .background( Color( 0xFFCCCCCC ) ),
                                            contentAlignment = Alignment.Center
                                        ){
                                            Text("Ver Objetivos")
                                        }

                                    } // TODO: mostrar todos los objetivos agregados en la toma y permitir editarlos

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
                                            .width( 200.dp )
                                            .height( 40.dp )
                                            .shadow(
                                                elevation = 5.dp,
                                                shape = RoundedCornerShape( 20.dp )
                                            )
                                            .clip( RoundedCornerShape( 20.dp ) )
                                            .background( Color( 0xFFCCCCCC ) )
                                            .clickable(
                                                onClick = {
                                                    editObjetivo = true
                                                }
                                            ),
                                        contentAlignment = Alignment.Center
                                    ){
                                        Text("Agregar Objetivo")
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

@Preview(
    device = "spec:width=360dp,height=640dp,dpi=320"
)
@Composable
private fun Vertical(){
    Views.TomaDatosV.Content(Modifier)
}

@Preview(
    device = "spec:width=640dp,height=360dp,dpi=320"
)
@Composable
private fun Horizontal(){
    Views.TomaDatosV.Content(Modifier)
}