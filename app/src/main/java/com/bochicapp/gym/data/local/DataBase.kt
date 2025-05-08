package com.bochicapp.gym.data.local

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.bochicapp.gym.R
import com.bochicapp.gym.data.model.*
import com.bochicapp.gym.data.model.toUserData
import com.bochicapp.gym.data.repository.Repository.database
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.util.concurrent.ConcurrentHashMap

class DataBase(
    context: Context
) {
    private val raiz = context.applicationContext.filesDir
    private val dataDirectory = File( raiz, "Data" )
    private val mutexMap = ConcurrentHashMap<String, Mutex>()
    private val filesToCopy = mutableMapOf<String, ByteArray>()

    init {
        if ( !dataDirectory.exists() ){
            try {
                dataDirectory.mkdir()
            } catch (e: Exception) {
                Log.e("Database.init.userDirectory", "Exception: $e")
            }
        }
        filesToCopy.put( "png_1", context.resources.openRawResource( R.raw.png_1 ).readBytes() )
    }
    private fun getMutex( id: String ): Mutex {
        return mutexMap.getOrPut( id ) { Mutex() }
    }

    private fun remMutex( id: String ){
        mutexMap.remove( id )
    }

    suspend fun copyFiles(){
        filesToCopy.forEach { file ->
            saveObject( file.key, file.value )
        }
    }

    private suspend fun getObject( id: String ): ByteArray {
        var response = ByteArray(0)
        getMutex( id ).withLock {
            try {
                response = File( dataDirectory, id ).readBytes()
            } catch ( e: Exception ){
                val sw = StringWriter()
                e.printStackTrace( PrintWriter( sw ) )
                // Pendiente usar los sw para documentos de log con los valores correspondientes.
                Log.i("DataBase.getObject() -> ", e.message.toString() )
            }
        }
        remMutex( id )
        return response
    }

    private suspend fun saveObject( id: String, bytes: ByteArray ): Boolean {
        var res = false
        getMutex( id ).withLock {
            try {
                File( dataDirectory, id ).writeBytes( bytes )
                res = true
            } catch ( e: Exception ){
                val sw = StringWriter()
                e.printStackTrace( PrintWriter( sw ) )
                // Pendiente usar los sw para documentos de log con los valores correspondientes.
                Log.e("DataBase.saveObject() -> ", e.message.toString() )
            }
        }
        remMutex( id )
        return res
    }

    suspend fun getUser( id: String ): Usuario {

        return try {

            val userResponse = getObject( id ).toString( Charsets.UTF_8 )
            var userData = UsuarioData("")

            if ( userResponse.isEmpty() ){
                userData = UsuarioData( id = "u_1", idfotoperfil = "png_1" )
                saveUser(
                    userData.toObj(
                        tomaDatos = listOf(),
                        ejecuciones = listOf(),
                        rutinas = listOf()
                    )
                )
            } else {
                userData = userResponse.toUserData()
            }

            val tomaDatosList = mutableListOf<TomaDatosFisicos>()
            val ejecucionesList = mutableListOf<Ejecucion>()
            val rutinasList = mutableListOf<Rutina>()

            userData.tomadatosfisicos.forEach { tomaId ->
                val tomaData = getObject( tomaId ).toString( Charsets.UTF_8 ).toTomaDeDatosData()
                val objetivosList = mutableListOf<ProximoObjetivo>()
                tomaData.proximosobjetivos.forEach { objetivoId ->
                    objetivosList.add( getObject( objetivoId ).toString( Charsets.UTF_8 ).toProximoObjetivoData().toObj() )
                }
                tomaDatosList.add( tomaData.toObj( objetivos = objetivosList.toList() ) )
            }

            //

            userData.toObj(
                tomaDatos = tomaDatosList.toList(),
                ejecuciones = ejecucionesList.toList(),
                rutinas = rutinasList.toList()
            )
        } catch ( e: Exception ){
            val sw = StringWriter()
            e.printStackTrace( PrintWriter( sw ) )
            // Pendiente usar los sw para documentos de log con los valores correspondientes.
            Log.e("DataBase.getUser() -> ", e.message.toString() )
            Usuario("")
        }
    }

    suspend fun saveUser( user: Usuario ): Boolean {
        var res = false
        try {
            saveObject( user.id, user.toUserData().toJson().toByteArray() )
            res = true
        } catch ( e: Exception ){
            val sw = StringWriter()
            e.printStackTrace( PrintWriter( sw ) )
            // Pendiente usar los sw para documentos de log con los valores correspondientes.
            Log.e("DataBase.saveUser() -> ", e.message.toString() )
        }
        return res
    }

    suspend fun getPng(id: String ): ImageBitmap {
        val bytes = getObject( id )
        return BitmapFactory.decodeByteArray( bytes, 0, bytes.size ).asImageBitmap()
    }



}