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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

open class GymViewModelClass (): ViewModel() {

    private val repository = Repository

    private lateinit var uiScope: CoroutineScope
    private lateinit var snackBarState: SnackbarHostState

    private val _selectedView = MutableStateFlow<GymView>( Views.PrincipalV )
    val selectedView: StateFlow<GymView> = _selectedView.asStateFlow()

    private val _navIds = MutableStateFlow( listOf<String>() )
    val navIds: StateFlow<List<String>> = _navIds.asStateFlow()

    private val _usuario = MutableStateFlow( Usuario( "" ) )
    val usuario: StateFlow<Usuario> = _usuario.asStateFlow()

    /*private val _lastUpdates = MutableStateFlow( listOf<String>() )
    val lastUpdates: StateFlow<List<String>> = _lastUpdates.asStateFlow()*/

    fun load( context: Context ){
        viewModelScope.launch ( Dispatchers.IO ){
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

    fun goTo(view: GymView, idToAdd: String? = null, idToRemove: String? = null ){
        _navIds.update { current ->
            val new = current.toMutableList()
            idToAdd?.let { new.add( idToAdd ) }
            idToRemove?.let { new.remove( idToRemove ) }
            new.toList()
        }
        _selectedView.update { view }
    }

    fun updateUser( user: Usuario ){
        viewModelScope.launch ( Dispatchers.IO ){
            if ( repository.updateUser( user ) ){
                _usuario.update { user }
                sendSnackMessage( "Usuario actualizado" )
            } else {
                sendSnackMessage( "Error al actualizar el usuario." )
            }
        }
    }

    fun getTomasDeDatos( id: String, onLoad: ( List<TomaDatosFisicos> ) -> Unit ) {
        viewModelScope.launch ( Dispatchers.IO ){
            onLoad( repository.loadTomasDeDatosFisicos( id ) )
        }
    }

    fun updateTomaDatosFisicos( toma: TomaDatosFisicos, idList: String, onUpdate: ( TomaDatosFisicos ) -> Unit ){
        viewModelScope.launch ( Dispatchers.IO ){
            val tomaId = repository.updateTomaDatosFisicos(
                tomaDatosFisicos = toma,
                idList = idList
            )
            if ( tomaId != null ){
                toma.id = tomaId
                sendSnackMessage( "Toma de datos actualizada." )
                onUpdate( toma )
            } else {
                sendSnackMessage( "Error al crear la toma de datos." )
            }
        }
    }

    fun getProximosObjetivos( id: String, onLoad: ( List<ProximoObjetivo> ) -> Unit ) {
        viewModelScope.launch ( Dispatchers.IO ){
            onLoad( repository.loadProximosObjetivos( id ) )
        }
    }

    fun updateProximoObjetivo( objetivo: ProximoObjetivo, onUpdate: ( ProximoObjetivo ) -> Unit ){
        viewModelScope.launch ( Dispatchers.IO ){
            val objetivoId = repository.updateProximoObjetivo( objetivo )
            if ( objetivoId != null ){
                objetivo.id = objetivoId
                sendSnackMessage( "Toma de datos agregada." )
                onUpdate( objetivo )
            } else {
                sendSnackMessage( "Error al crear la toma de datos." )
            }
        }
    }

    fun getPng( id: String , onLoad: ( ImageBitmap ) -> Unit ) {
        viewModelScope.launch ( Dispatchers.IO ){
             onLoad( repository.getPng( id = id ) )
        }
    }

}

object GymViewModel: GymViewModelClass()