package com.bochicapp.gym.data.repository

import android.content.Context
import androidx.compose.ui.graphics.ImageBitmap
import com.bochicapp.gym.data.local.DataBase
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
        var user = database.getUser( id = "u_1" )
        if ( user.id.isEmpty() ){
            user = Usuario( id = "u_1", idfotoperfil = "png_1" )
            if ( !database.saveUser( user ) ){
                user = Usuario("")
            }
        }
        return user
    }

    suspend fun saveUser( user: Usuario ): Boolean {
        return database.saveUser( user )
    }

    suspend fun getTomaDatos( id: String ): TomaDatosFisicos? {
        return database.getTomaDatos( id = id )
    }

    suspend fun getPng(id: String ): ImageBitmap {
        return database.getPng( id = id )
    }

}