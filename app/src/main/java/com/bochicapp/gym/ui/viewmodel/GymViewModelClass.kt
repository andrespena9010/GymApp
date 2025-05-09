package com.bochicapp.gym.ui.viewmodel

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bochicapp.gym.data.model.*
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

    private val _lastId = MutableStateFlow( "" )
    val lastId: StateFlow<String> = _lastId.asStateFlow()

    private val _usuario = MutableStateFlow( Usuario( "" ) ) // Para podificar el usuario la respuesta de el guardado debe ser true, sino no se modifican los cambios de la interfaz
    val usuario: StateFlow<Usuario> = _usuario.asStateFlow()

    private val _tomasDeDatos = MutableStateFlow( mapOf<String, TomaDatosFisicos>() )
    val tomasDeDatos: StateFlow<Map<String, TomaDatosFisicos>> = _tomasDeDatos.asStateFlow()

    private val _objetivos = MutableStateFlow( mapOf<String, ProximoObjetivo>() )
    val objetivos: StateFlow<Map<String, ProximoObjetivo>> = _objetivos.asStateFlow()

    fun load( context: Context ){
        viewModelScope.launch {
            repository.init( context = context )
            _usuario.update { repository.loadUser() }
            _tomasDeDatos.update { repository.loadTomasDeDatosFisicos() }
            _objetivos.update { repository.loadProximosObjetivos() }
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

    fun goTo( view: GymView, objectId: String = "" ){
        _lastId.update { objectId }
        _selectedView.update { view }
    }

    fun update(obj: Any, type: KClass<*> ){ // TODO: !!!!!! agregar seguimiento a los cambios realizados en general

        when ( type ){

            Usuario::class -> {
                viewModelScope.launch {
                    var newUser = obj as Usuario
                    if ( repository.updateUser( newUser ) ){
                        _usuario.update { newUser }
                        sendSnackMessage( "Usuario actualizado" )
                    } else {
                        sendSnackMessage( "Error al actualizar el usuario." )
                    }
                }
            }

            TomaDatosFisicos::class -> {
                viewModelScope.launch {
                    var newToma = obj as TomaDatosFisicos
                    val tomaId = repository.updateTomaDatosFisicos( newToma )
                    if ( tomaId != null ){
                        newToma.id = tomaId
                        _tomasDeDatos.update { tomasDatos ->
                            tomasDatos.toMutableMap().apply {
                                this[ newToma.id ] = newToma
                            }.toMap()
                        }
                        sendSnackMessage( "Toma de datos agregada." )
                    } else {
                        sendSnackMessage( "Error al crear la toma de datos." )
                    }
                }
            }

            ProximoObjetivo::class ->{
                viewModelScope.launch {
                    var newObjetivo = obj as ProximoObjetivo
                    val objetivoId = repository.updateProximoObjetivo( newObjetivo )
                    if ( objetivoId != null ){
                        newObjetivo.id = objetivoId
                        _objetivos.update {  objetivos ->
                            objetivos.toMutableMap().apply {
                                this[ newObjetivo.id ] = newObjetivo
                            }.toMap()
                        }
                        sendSnackMessage( "Toma de datos agregada." )
                    } else {
                        sendSnackMessage( "Error al crear la toma de datos." )
                    }
                }
            }

        }

    }

    fun getPng( id: String , onLoad: ( ImageBitmap ) -> Unit ) {
        viewModelScope.launch {
             onLoad( repository.getPng( id = id ) )
        }
    }

}

object GymViewModel: GymViewModelClass()