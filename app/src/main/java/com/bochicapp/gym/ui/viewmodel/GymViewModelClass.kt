package com.bochicapp.gym.ui.viewmodel

import android.content.Context
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bochicapp.gym.data.model.DataElement
import com.bochicapp.gym.data.model.TomaDatosFisicos
import com.bochicapp.gym.data.model.Usuario
import com.bochicapp.gym.data.repository.Repository
import com.bochicapp.gym.ui.model.GymView
import com.bochicapp.gym.ui.model.Views
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

open class GymViewModelClass (): ViewModel() {

    private val repository = Repository

    private val _selectedView = MutableStateFlow<GymView>( Views.PrincipalV )
    val selectedView: StateFlow<GymView> = _selectedView.asStateFlow()

    private val _usuario = MutableStateFlow( Usuario( "" ) )
    val usuario: StateFlow<Usuario> = _usuario.asStateFlow()

    private val _datosList = MutableStateFlow<List<TomaDatosFisicos>>( listOf() )
    val datosList: StateFlow<List<TomaDatosFisicos>> = _datosList.asStateFlow()

    fun load( context: Context ){
        viewModelScope.launch {
            repository.init( context = context )
            val user = repository.loadUser()
            if ( user.tomadatosfisicos.isNotEmpty() ){
                val tomas = mutableListOf<TomaDatosFisicos>()
                user.tomadatosfisicos.forEach { id ->
                    val toma = repository.getTomaDatos( id )
                    toma?.let {
                        tomas.add( toma )
                    }
                }
                _datosList.update { tomas }
            }
            _usuario.update { user }
        }
    }

    fun goTo( view: GymView ){
        _selectedView.update { view }
    }

    fun update( info: List<DataElement>, type: KClass<*> ){

        when ( type ){

            Usuario::class -> {
                var newUser = Usuario("")
                _usuario.update { current ->
                    newUser = current.updatedCopy( info )
                    newUser
                }
                viewModelScope.launch { repository.saveUser( newUser ) }
            }

        }

    }

    fun creaTomaDeDatos( onLoad: ( TomaDatosFisicos ) -> Unit ) {
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