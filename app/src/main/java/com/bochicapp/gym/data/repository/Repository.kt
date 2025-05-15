package com.bochicapp.gym.data.repository

import android.content.Context
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.viewModelScope
import com.bochicapp.gym.data.local.DataBase
import com.bochicapp.gym.data.model.Dia
import com.bochicapp.gym.data.model.ProximoObjetivo
import com.bochicapp.gym.data.model.Rutina
import com.bochicapp.gym.data.model.TomaDatosFisicos
import com.bochicapp.gym.data.model.Usuario
import kotlinx.coroutines.launch

object Repository {

    lateinit var database: DataBase

    fun init( context: Context ){
        database = DataBase(
            context = context
        )
    }

    suspend fun updateListOrder(idList: String, list: List<String> ){
        database.updateListOrder(
            idList = idList,
            list = list
        )
    }

    suspend fun loadUser(): Usuario {
        return database.getUser()
    }

    suspend fun updateUser( user: Usuario ): Boolean {
        return database.updateUser( user = user )
    }

    suspend fun loadTomasDeDatosFisicos( id: String ): List<TomaDatosFisicos> {
        return database.loadTomasDeDatos( id = id )
    }

    suspend fun updateTomaDatosFisicos(
        tomaDatosFisicos: TomaDatosFisicos,
        idList: String
    ): String? {
        return database.updateTomaDatosFisicos(
            tomaDeDatos = tomaDatosFisicos,
            idList = idList
        )
    }

    suspend fun loadProximosObjetivos( id: String ): List<ProximoObjetivo> {
        return database.loadProximosObjetivos( id = id )
    }

    suspend fun updateProximoObjetivo(
        proximoObjetivo: ProximoObjetivo,
        idList: String
    ): String? {
        return database.updateProximoObjetivo(
            proximoObjetivo = proximoObjetivo,
            idList = idList
        )
    }

    suspend fun loadRutinas( id: String ): List<Rutina> {
        return database.loadRutinas( id = id )
    }

    suspend fun updateRutina(
        rutina: Rutina,
        idList: String
    ): String? {
        return database.updateRutina(
            rutina = rutina,
            idList = idList
        )
    }

    suspend fun loadDias( id: String ): List<Dia> {
        return database.loadDias( id = id )
    }

    suspend fun updateDia(
        dia: Dia,
        idList: String
    ): String? {
        return database.updateDia(
            dia = dia,
            idList = idList
        )
    }

    suspend fun getPng( id: String ): ImageBitmap {
        return database.getPng( id = id )
    }

}