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

    suspend fun updateProximoObjetivo( proximoObjetivo: ProximoObjetivo ): String? {
        return database.updateProximoObjetivo( proximoObjetivo = proximoObjetivo )
    }

    suspend fun getPng( id: String ): ImageBitmap {
        return database.getPng( id = id )
    }

}