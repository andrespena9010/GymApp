package com.bochicapp.gym.data.local

import android.content.Context
import android.util.Log
import com.bochicapp.gym.data.model.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.File
import java.util.concurrent.ConcurrentHashMap

class DataBase(
    context: Context
) {
    private val raiz = context.applicationContext.filesDir
    private val dataDirectory = File( raiz, "Data" )
    private val mutexMap = ConcurrentHashMap<String, Mutex>()

    init {
        if ( !dataDirectory.exists() ){
            try {
                dataDirectory.mkdir()
            } catch (e: Exception) {
                Log.e("Database.init.userDirectory", "Exception: $e")
            }
        }
    }

    suspend fun saveUser( user: Usuario ) {
        getMutex("1").withLock {
            Log.i("Database.test", "user1: ${user.toJson()}")
        }
    }

    suspend fun getUser( id: String ): Usuario {
        return getObject( id ).toUser()
    }

    suspend fun getRutina( id: String ): Rutina {
        return getObject( id ).toRutina()
    }

    private fun getMutex(fileName: String): Mutex {
        return mutexMap.getOrPut(fileName) { Mutex() }
    }

    private suspend fun getObject( id: String ): String {
        var response = ""
        getMutex( id ).withLock {
            response = File( dataDirectory, id ).readText()
        }
        return response
    }

}