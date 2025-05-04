package com.bochicapp.gym.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.bochicapp.gym.data.model.Usuario
import com.bochicapp.gym.data.repository.Repository
import com.bochicapp.gym.ui.model.GymView
import com.bochicapp.gym.ui.model.Views
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

open class GymViewModelClass (): ViewModel() {

    private val repository = Repository
    private val userId = "1"

    private val _selectedView = MutableStateFlow<GymView>( Views.PrincipalV )
    val selectedView: StateFlow<GymView> = _selectedView.asStateFlow()

    private val _usuario = MutableStateFlow( Usuario("") )
    val usuario: StateFlow<Usuario> = _usuario.asStateFlow()

    fun load( context: Context ){
        repository.init( context = context )
    }

    fun goToPrincipal(){
        _selectedView.update { Views.PrincipalV }
    }

    fun iniciarRutina(){
        _selectedView.update { Views.EjecucionV }
    }

    fun gestionarRutinas(){
        _selectedView.update { Views.EjecucionV }
    }

}

object GymViewModel: GymViewModelClass()