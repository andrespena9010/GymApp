package com.bochicapp.gym.data.local

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.bochicapp.gym.R
import com.bochicapp.gym.data.model.*
import com.google.gson.Gson
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.time.Instant
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

const val TDF = "TDF"

class DataBase(
    context: Context
) {

    private val raiz = context.applicationContext.filesDir
    private val dataDirectory = File( raiz, "Data" )
    private var dataList = listOf<String>()
    private val mutexMap = ConcurrentHashMap<String, Mutex>()
    private val filesToCopy = mutableMapOf<String, ByteArray>()
    private val userFile = File( dataDirectory, "6d8a8e8e-3b4a-4c5f-9c3d-7e6f5d4c3b2a" )
    private var userId = ""

    init {
        if ( !dataDirectory.exists() ){
            try {
                dataDirectory.mkdir()
            } catch (e: Exception) {
                Log.e("Database.init.userDirectory", "Exception: $e")
            }
        }
        dataDirectory.list()?.let { array ->
            dataList = array.toList()
        }
        filesToCopy.put( "png_1", context.resources.openRawResource( R.raw.png_1 ).readBytes() )
        if ( userFile.exists() ){
            userId = Gson().fromJson( userFile.readBytes().toString( Charsets.UTF_8 ), String::class.java )
        } else {
            val id = UUID.randomUUID().toString()
            userFile.writeBytes( Gson().toJson( id ).toByteArray() )
            userId = id
        }
    }
    private fun getMutex( id: String ): Mutex {
        return mutexMap.getOrPut( id ) { Mutex() }
    }

    private fun remMutex( id: String ){
        mutexMap.remove( id )
    }

    suspend fun copyFiles(){
        filesToCopy.forEach { file ->
            updateObject( file.key, file.value )
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

    private suspend fun updateObject( id: String, bytes: ByteArray ): Boolean {
        var res = false
        getMutex( id ).withLock {
            try {
                File( dataDirectory, id ).writeBytes( bytes )
                res = true
            } catch ( e: Exception ){
                val sw = StringWriter()
                e.printStackTrace( PrintWriter( sw ) )
                // Pendiente usar los sw para documentos de log con los valores correspondientes.
                Log.e("DataBase.updateObject() -> ", e.message.toString() )
            }
        }
        remMutex( id )
        return res
    }

    suspend fun getUser(): Usuario {

        return try {

            val userResponse = getObject( userId ).toString( Charsets.UTF_8 )
            var user = Usuario("")

            if ( userResponse.isEmpty() ){
                val tomaDeDatosId = UUID.randomUUID().toString()
                val historialId = UUID.randomUUID().toString()
                val rutinasId = UUID.randomUUID().toString()
                user.id = userId
                user.idfotoperfil = "png_1"
                if ( updateObject( tomaDeDatosId, "[]".toByteArray() ) ) user.tomadatosfisicos = tomaDeDatosId
                if ( updateObject( historialId, "[]".toByteArray() ) ) user.historial = historialId
                if ( updateObject( rutinasId, "[]".toByteArray() ) ) user.rutinas = rutinasId
                updateUser( user )
            } else {
                user = userResponse.toUser()
            }

            user
        } catch ( e: Exception ){
            val sw = StringWriter()
            e.printStackTrace( PrintWriter( sw ) )
            // Pendiente usar los sw para documentos de log con los valores correspondientes.
            Log.e("DataBase.getUser() -> ", e.message.toString() )
            Usuario("")
        }
    }

    suspend fun updateUser( user: Usuario ): Boolean {
        var res = false
        try {
            updateObject( user.id, user.toJson().toByteArray() )
            res = true
        } catch ( e: Exception ){
            val sw = StringWriter()
            e.printStackTrace( PrintWriter( sw ) )
            // Pendiente usar los sw para documentos de log con los valores correspondientes.
            Log.e("DataBase.updateUser() -> ", e.message.toString() )
        }
        return res
    }

    suspend fun loadTomasDeDatos( id: String ): List<TomaDatosFisicos> {
        val tomaDeDatos = mutableListOf<TomaDatosFisicos>()
        if ( id.isNotEmpty() ){
            val ids = getObject( id ).toString( Charsets.UTF_8 )
            if ( ids.isNotEmpty() ){
                try {
                    ids.toList().forEach { idToma ->
                        tomaDeDatos.add( getObject( idToma ).toString( Charsets.UTF_8 ).toTomaDeDatos() )
                    }
                } catch ( e: Exception ){
                    val sw = StringWriter()
                    e.printStackTrace( PrintWriter( sw ) )
                    // Pendiente usar los sw para documentos de log con los valores correspondientes.
                    Log.e("DataBase.loadTomasDeDatos() -> ", e.message.toString() )
                }
            }
        }
        return tomaDeDatos.toList()
    }

    suspend fun updateTomaDatosFisicos( tomaDeDatos: TomaDatosFisicos, idList: String ): String? {
        try {
            if ( tomaDeDatos.id.isEmpty() ) tomaDeDatos.id = UUID.randomUUID().toString()
            if ( idList.isNotEmpty() ){
                val res = getObject( idList ).toString( Charsets.UTF_8 )
                if ( res.isNotEmpty() ){
                    val list = res.toList().toMutableList()
                    tomaDeDatos.fechatoma = Instant.now().toString()
                    updateObject( tomaDeDatos.id ,tomaDeDatos.toJson().toByteArray() )
                    list.add( tomaDeDatos.id )
                    updateObject( idList, list.toList().toJson().toByteArray() )
                }
            }
            return tomaDeDatos.id
        } catch ( e: Exception ){
            val sw = StringWriter()
            e.printStackTrace( PrintWriter( sw ) )
            // Pendiente usar los sw para documentos de log con los valores correspondientes.
            Log.e("DataBase.updateTomaDatosFisicos() -> ", e.message.toString() )
        }
        return null
    }

    suspend fun loadProximosObjetivos( id: String ): List<ProximoObjetivo> {
        val objetivos = mutableListOf<ProximoObjetivo>()
        val ids = getObject( id ).toString( Charsets.UTF_8 )
        if ( ids.isNotEmpty() ){
            try {
                ids.toList().forEach { idToma ->
                    objetivos.add( getObject( idToma ).toString( Charsets.UTF_8 ).toProximoObjetivo() )
                }
            } catch ( e: Exception ){
                val sw = StringWriter()
                e.printStackTrace( PrintWriter( sw ) )
                // Pendiente usar los sw para documentos de log con los valores correspondientes.
                Log.e("DataBase.loadProximosObjetivos() -> ", e.message.toString() )
            }
        }
        return objetivos.toList()
    }

    suspend fun updateProximoObjetivo( proximoObjetivo: ProximoObjetivo ): String? {
        try {
            if ( proximoObjetivo.id.isEmpty() ) proximoObjetivo.id = UUID.randomUUID().toString()
            updateObject( proximoObjetivo.id ,proximoObjetivo.toJson().toByteArray() )
            return proximoObjetivo.id
        } catch ( e: Exception ){
            val sw = StringWriter()
            e.printStackTrace( PrintWriter( sw ) )
            // Pendiente usar los sw para documentos de log con los valores correspondientes.
            Log.e("DataBase.updateProximoObjetivo() -> ", e.message.toString() )
        }
        return null
    }

    suspend fun getPng(id: String ): ImageBitmap {
        val bytes = getObject( id )
        return BitmapFactory.decodeByteArray( bytes, 0, bytes.size ).asImageBitmap()
    }



}