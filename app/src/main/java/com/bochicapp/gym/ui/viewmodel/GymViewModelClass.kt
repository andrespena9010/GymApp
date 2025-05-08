package com.bochicapp.gym.ui.viewmodel

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bochicapp.gym.data.model.DataElement
import com.bochicapp.gym.data.model.ProximoObjetivoData
import com.bochicapp.gym.data.model.TomaDatosFisicosData
import com.bochicapp.gym.data.model.Usuario
import com.bochicapp.gym.data.model.UsuarioData
import com.bochicapp.gym.data.repository.Repository
import com.bochicapp.gym.ui.model.GymView
import com.bochicapp.gym.ui.model.Views
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

open class GymViewModelClass (): ViewModel() {

    private val repository = Repository

    private lateinit var uiScope: CoroutineScope
    private lateinit var snackBarState: SnackbarHostState

    private val _selectedView = MutableStateFlow<GymView>( Views.PrincipalV )
    val selectedView: StateFlow<GymView> = _selectedView.asStateFlow()

    private val _usuario = MutableStateFlow( Usuario( "" ) ) // Para podificar el usuario la respuesta de el guardado debe ser true, sino no se modifican los cambios de la interfaz
    val usuario: StateFlow<Usuario> = _usuario.asStateFlow()

    fun load( context: Context ){
        viewModelScope.launch {
            repository.init( context = context )
            _usuario.update { repository.loadUser() }
        }
    }

    fun setSnack( scope: CoroutineScope, snackState: SnackbarHostState ){
        uiScope = scope
        snackBarState = snackState
    }

    fun sendSnackMessage( msg: String ){
        uiScope.launch {
            snackBarState.showSnackbar( msg )
        }
    }

    fun goTo( view: GymView ){
        _selectedView.update { view }
    }

    fun update( info: List<DataElement<Any>>, type: KClass<*> ){ // agregar seguimiento a los cambios realizados en general

        when ( type ){

            UsuarioData::class -> {
                var newUser = Usuario("")
                _usuario.update { current ->
                    newUser = current.updatedCopy( info )
                    newUser
                }
                viewModelScope.launch {
                    repository.saveUser( newUser )
                    sendSnackMessage( "Usuario actualizado" )
                }
            }

        }

    }

    fun creaTomaDeDatos( onLoad: (TomaDatosFisicosData ) -> Unit ) {
        /*viewModelScope.launch {

            onLoad( repository )
        }*/
    }

    fun getPng( id: String , onLoad: ( ImageBitmap ) -> Unit ) {
        viewModelScope.launch {
             onLoad( repository.getPng( id = id ) )
        }
    }

}

object GymViewModel: GymViewModelClass()