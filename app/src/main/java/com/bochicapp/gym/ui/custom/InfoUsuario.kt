package com.bochicapp.gym.ui.custom

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.graphics.createBitmap
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bochicapp.gym.data.model.Data
import com.bochicapp.gym.data.model.Usuario
import com.bochicapp.gym.data.model.comparaObjetos
import com.bochicapp.gym.ui.model.Views
import com.bochicapp.gym.ui.viewmodel.GymViewModel
import com.bochicapp.gym.ui.viewmodel.GymViewModelClass

@Composable
fun InfoUsuario(
    viewModel: GymViewModelClass = GymViewModel,
    modifier: Modifier
) {

    val usuario by viewModel.usuario.collectAsStateWithLifecycle()
    val info by rememberSaveable { mutableStateOf( usuario.toDataList() ) }
    var edit by rememberSaveable { mutableStateOf( false ) }
    var changes by rememberSaveable { mutableStateOf( false ) }
    val fondo by animateColorAsState(
        targetValue = if (edit) Color( 0x55000000 ) else Color( 0xCCFFFFFF ),
        animationSpec = tween(durationMillis = 1000)
    )

    LaunchedEffect( usuario, edit ) {
        changes = !comparaObjetos( info, usuario.toDataList() )
    }

    BackHandler {
        viewModel.goTo( Views.PrincipalV )
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
                        edit = !edit
                    }
                ),
            contentAlignment = Alignment.Center
        ){
            Icon(
                imageVector = Icons.Default.Create,
                contentDescription = ""
            )
        }

        if ( changes ){
            Box(
                modifier = Modifier
                    .zIndex( 2f )
                    .padding( 10.dp )
                    .align( Alignment.BottomCenter )
                    .width( 200.dp )
                    .height( 40.dp )
                    .shadow(
                        elevation = 5.dp,
                        shape = RoundedCornerShape( 20.dp )
                    )
                    .clip( RoundedCornerShape( 20.dp ) )
                    .background( Color( 0xFF5555CC ) )
                    .clickable(
                        onClick = {
                            viewModel.update( info, Usuario::class )
                        }
                    ),
                contentAlignment = Alignment.Center
            ){
                Text("Guardar Cambios")
            }
        }

        LazyColumn (
            modifier = Modifier
                .padding( 15.dp )
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            items ( info ){ elemenet ->

                when ( elemenet.type ){

                    Data.Png -> {

                        var image by remember { mutableStateOf( createBitmap(100, 100).asImageBitmap() ) }
                        viewModel.getPng(
                            id = elemenet.value?.value.toString(),
                            onLoad = { bitmap ->
                                image = bitmap
                            }
                        )

                        Image(
                            modifier = Modifier
                                .size( 200.dp ),
                            bitmap = image,
                            contentDescription = elemenet.name
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
                                text = elemenet.name,
                                fontWeight = FontWeight.Bold
                            )
                            BasicTextField(
                                modifier = Modifier
                                    .padding(5.dp)
                                    .fillMaxWidth()
                                    .clip( RoundedCornerShape( 5.dp ) )
                                    .background( if ( edit ) Color( 0xCCFFFFFF ) else Color.Transparent ),
                                value = elemenet.value?.value.toString(),
                                onValueChange = { elemenet.value?.value = it },
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
                                text = elemenet.name,
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
                                    .background( Color( 0xFFCCCCCC ) )
                                    .clickable(
                                        onClick = {
                                            elemenet.onClick( viewModel )
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ){
                                Text("Detalles")
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
    Views.InfoUsuarioV.Content(Modifier)
}

@Preview(
    device = "spec:width=640dp,height=360dp,dpi=320"
)
@Composable
private fun Horizontal(){
    Views.InfoUsuarioV.Content(Modifier)
}