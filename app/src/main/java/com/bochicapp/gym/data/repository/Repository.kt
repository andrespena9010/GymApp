package com.bochicapp.gym.data.repository

import android.content.Context
import androidx.compose.ui.graphics.ImageBitmap
import com.bochicapp.gym.data.local.DataBase
import com.bochicapp.gym.data.model.ProximoObjetivo
import com.bochicapp.gym.data.model.TomaDatosFisicos
import com.bochicapp.gym.data.model.Usuario

object Repository {

    lateinit var database: DataBase

    suspend fun init(context: Context ){
        database = DataBase(
            context = context
        )
        database.copyFiles()
    }

    suspend fun loadUser(): Usuario {
        return database.getUser()
    }

    suspend fun updateUser(user: Usuario ): Boolean {
        return database.updateUser( user )
    }

    suspend fun loadTomasDeDatosFisicos(): Map<String, TomaDatosFisicos> {
        return database.loadTomasDeDatos()
    }

    suspend fun updateTomaDatosFisicos( tomaDatosFisicos: TomaDatosFisicos ): String? {
        return database.updateTomaDatosFisicos( tomaDatosFisicos )
    }

    suspend fun loadProximosObjetivos(): Map<String, ProximoObjetivo> {
        return database.loadProximosObjetivos()
    }

    suspend fun updateProximoObjetivo( proximoObjetivo: ProximoObjetivo ): String? {
        return database.updateProximoObjetivo( proximoObjetivo )
    }

    suspend fun getPng( id: String ): ImageBitmap {
        return database.getPng( id = id )
    }

}