package com.bochicapp.gym.data.local

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.bochicapp.gym.R
import com.bochicapp.gym.data.model.*
import com.bochicapp.gym.data.repository.Repository.database
import com.google.gson.Gson
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.time.Instant
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap


class DataBase(
    context: Context
) {

    private val raiz = context.applicationContext.filesDir
    private val dataDirectory = File( raiz, "Data" )
    private var dataList = listOf<String>()
    private val mutexMap = ConcurrentHashMap<String, Mutex>()
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
        if ( userFile.exists() ){
            userId = Gson().fromJson( userFile.readBytes().toString( Charsets.UTF_8 ), String::class.java )
        } else {
            val id = UUID.randomUUID().toString()
            userFile.writeBytes( Gson().toJson( id ).toByteArray() )
            userId = id
        }

        copyFiles( context )
    }
    private fun getMutex( id: String ): Mutex {
        return mutexMap.getOrPut( id ) { Mutex() }
    }

    private fun remMutex( id: String ){
        mutexMap.remove( id )
    }

    fun copyFiles( context: Context ){
        val default1 = File( dataDirectory, "png_1" )
        if ( !default1.exists() ){ default1.writeBytes( context.resources.openRawResource( R.raw.png_1 ).readBytes() ) }
        val default2 = File( dataDirectory, "png_2" )
        if ( !default2.exists() ){ default2.writeBytes( context.resources.openRawResource( R.raw.png_2 ).readBytes() ) }
    }

    private suspend fun getObject( id: String ): ByteArray {
        var response = ByteArray(0)
        getMutex( id ).withLock {
            try {
                val file = File( dataDirectory, id )
                if ( file.exists() ){ response = file.readBytes() }
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

    suspend fun updateListOrder( idList: String, list: List<String> ){
        updateObject(
            id = idList,
            bytes = list.toJson().toByteArray()
        )
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
            if ( tomaDeDatos.id.isEmpty() ) {
                tomaDeDatos.id = UUID.randomUUID().toString()
                val objetivosId = UUID.randomUUID().toString()
                if ( updateObject( objetivosId, "[]".toByteArray() ) ) tomaDeDatos.proximosobjetivos = objetivosId
            }
            if ( idList.isNotEmpty() ){
                val res = getObject( idList ).toString( Charsets.UTF_8 )
                if ( res.isNotEmpty() ){
                    val list = res.toList().toMutableList()
                    tomaDeDatos.fechamodificacion = Instant.now().toString()
                    updateObject( tomaDeDatos.id ,tomaDeDatos.toJson().toByteArray() )
                    val i = list.indexOfFirst { it == tomaDeDatos.id }
                    if ( i != -1 ) {
                        list[i] = tomaDeDatos.id
                    } else {
                        list.add( tomaDeDatos.id )
                    }
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

    suspend fun updateProximoObjetivo( proximoObjetivo: ProximoObjetivo, idList: String ): String? {
        try {
            if ( proximoObjetivo.id.isEmpty() ) proximoObjetivo.id = UUID.randomUUID().toString()
            if ( idList.isNotEmpty() ){
                val res = getObject( idList ).toString( Charsets.UTF_8 )
                if ( res.isNotEmpty() ){
                    val list = res.toList().toMutableList()
                    proximoObjetivo.fechamodificacion = Instant.now().toString()
                    updateObject( proximoObjetivo.id ,proximoObjetivo.toJson().toByteArray() )
                    val i = list.indexOfFirst { it == proximoObjetivo.id }
                    if ( i != -1 ) {
                        list[i] = proximoObjetivo.id
                    } else {
                        list.add( proximoObjetivo.id )
                    }
                    updateObject( idList, list.toList().toJson().toByteArray() )
                }
            }
            return proximoObjetivo.id
        } catch ( e: Exception ){
            val sw = StringWriter()
            e.printStackTrace( PrintWriter( sw ) )
            // Pendiente usar los sw para documentos de log con los valores correspondientes.
            Log.e("DataBase.updateProximoObjetivo() -> ", e.message.toString() )
        }
        return null
    }

    suspend fun loadRutinas( id: String ): List<Rutina> {
        val rutinas = mutableListOf<Rutina>()
        if ( id.isNotEmpty() ){
            val ids = getObject( id ).toString( Charsets.UTF_8 )
            if ( ids.isNotEmpty() ){
                try {
                    ids.toList().forEach { idRutina ->
                        rutinas.add( getObject( idRutina ).toString( Charsets.UTF_8 ).toRutina() )
                    }
                } catch ( e: Exception ){
                    val sw = StringWriter()
                    e.printStackTrace( PrintWriter( sw ) )
                    // Pendiente usar los sw para documentos de log con los valores correspondientes.
                    Log.e("DataBase.loadRutinas() -> ", e.message.toString() )
                }
            }
        }
        return rutinas.toList()
    }

    suspend fun updateRutina( rutina: Rutina, idList: String ): String? {
        try {
            if ( rutina.id.isEmpty() ) {
                rutina.id = UUID.randomUUID().toString()
                val dias = UUID.randomUUID().toString()
                if ( updateObject( dias, "[]".toByteArray() ) ) rutina.dias = dias
            }
            if ( idList.isNotEmpty() ){
                val res = getObject( idList ).toString( Charsets.UTF_8 )
                if ( res.isNotEmpty() ){
                    val list = res.toList().toMutableList()
                    rutina.fechamodificacion = Instant.now().toString()
                    updateObject( rutina.id ,rutina.toJson().toByteArray() )
                    val i = list.indexOfFirst { it == rutina.id }
                    if ( i != -1 ) {
                        list[i] = rutina.id
                    } else {
                        list.add( rutina.id )
                    }
                    updateObject( idList, list.toList().toJson().toByteArray() )
                }
            }
            return rutina.id
        } catch ( e: Exception ){
            val sw = StringWriter()
            e.printStackTrace( PrintWriter( sw ) )
            // Pendiente usar los sw para documentos de log con los valores correspondientes.
            Log.e("DataBase.updateRutina() -> ", e.message.toString() )
        }
        return null
    }

    suspend fun loadDias( id: String ): List<Dia> {
        val dias = mutableListOf<Dia>()
        if ( id.isNotEmpty() ){
            val ids = getObject( id ).toString( Charsets.UTF_8 )
            if ( ids.isNotEmpty() ){
                try {
                    ids.toList().forEach { idDia ->
                        dias.add( getObject( idDia ).toString( Charsets.UTF_8 ).toDia() )
                    }
                } catch ( e: Exception ){
                    val sw = StringWriter()
                    e.printStackTrace( PrintWriter( sw ) )
                    // Pendiente usar los sw para documentos de log con los valores correspondientes.
                    Log.e("DataBase.loadDias() -> ", e.message.toString() )
                }
            }
        }
        return dias.toList()
    }

    suspend fun updateDia(dia: Dia, idList: String ): String? {
        try {
            if ( dia.id.isEmpty() ) {
                dia.id = UUID.randomUUID().toString()
                val ejercicios = UUID.randomUUID().toString()
                if ( updateObject( ejercicios, "[]".toByteArray() ) ) dia.ejercicios = ejercicios
            }
            if ( idList.isNotEmpty() ){
                val res = getObject( idList ).toString( Charsets.UTF_8 )
                if ( res.isNotEmpty() ){
                    val list = res.toList().toMutableList()
                    dia.fechamodificacion = Instant.now().toString()
                    updateObject( dia.id ,dia.toJson().toByteArray() )
                    val i = list.indexOfFirst { it == dia.id }
                    if ( i != -1 ) {
                        list[i] = dia.id
                    } else {
                        list.add( dia.id )
                    }
                    updateObject( idList, list.toList().toJson().toByteArray() )
                }
            }
            return dia.id
        } catch ( e: Exception ){
            val sw = StringWriter()
            e.printStackTrace( PrintWriter( sw ) )
            // Pendiente usar los sw para documentos de log con los valores correspondientes.
            Log.e("DataBase.updateDia() -> ", e.message.toString() )
        }
        return null
    }

    suspend fun getPng(id: String ): ImageBitmap {
        val bytes = getObject( id )
        return BitmapFactory.decodeByteArray( bytes, 0, bytes.size ).asImageBitmap()
    }



}