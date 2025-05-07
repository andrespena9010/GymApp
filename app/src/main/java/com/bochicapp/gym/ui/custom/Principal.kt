package com.bochicapp.gym.ui.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bochicapp.gym.data.model.DataElement
import com.bochicapp.gym.ui.model.Views
import com.bochicapp.gym.ui.viewmodel.GymViewModel
import com.bochicapp.gym.ui.viewmodel.GymViewModelClass

@Composable
fun Principal (
    viewModel: GymViewModelClass = GymViewModel,
    modifier: Modifier
){

    val options = listOf(
        DataElement(
            name = "Iniciar rutina",
            onClick = { vm ->
                vm.goTo( Views.EjecucionV )
            }
        ),
        DataElement(
            name = "Informacion de usuario",
            onClick = { vm ->
                vm.goTo( Views.InfoUsuarioV )
            }
        )
    )

    LazyColumn (
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

        items( options ){ element ->

            Box(
                modifier = Modifier
                    .size(220.dp)
                    .padding( 10.dp )
                    .shadow(
                        elevation = 15.dp,
                        shape = RoundedCornerShape( 25.dp )
                    )
                    .background( Color.White )
                    .clickable(
                        onClick = {
                            element.onClick( viewModel )
                        }
                    ),
                contentAlignment = Alignment.Center
            ){

                Text(
                    modifier = Modifier
                        .padding(10.dp),
                    text = element.name,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center
                )

            }

        }

    }

}

@Preview(
    device = "spec:width=360dp,height=640dp,dpi=320"
)
@Composable
private fun Vertical(){
    Views.PrincipalV.Content(Modifier)
}

@Preview(
    device = "spec:width=640dp,height=360dp,dpi=320"
)
@Composable
private fun Horizontal(){
    Views.PrincipalV.Content(Modifier)
}