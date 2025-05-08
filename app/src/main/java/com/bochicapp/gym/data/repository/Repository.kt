package com.bochicapp.gym.data.repository

import android.content.Context
import androidx.compose.ui.graphics.ImageBitmap
import com.bochicapp.gym.data.local.DataBase
import com.bochicapp.gym.data.model.ProximoObjetivoData
import com.bochicapp.gym.data.model.TomaDatosFisicosData
import com.bochicapp.gym.data.model.Usuario
import com.bochicapp.gym.data.model.UsuarioData

object Repository {

    lateinit var database: DataBase

    suspend fun init(context: Context ){
        database = DataBase(
            context = context
        )
        database.copyFiles()
    }

    suspend fun loadUser(): Usuario {
        return database.getUser( id = "u_1" )
    }

    suspend fun saveUser( user: Usuario ): Boolean {
        return database.saveUser( user )
    }

    suspend fun getPng( id: String ): ImageBitmap {
        return database.getPng( id = id )
    }

}