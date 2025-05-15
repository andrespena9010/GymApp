package com.bochicapp.gym.data.model

import com.google.gson.Gson
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun String.toUser(): Usuario {
    return Gson().fromJson( this, Usuario::class.java )
}

fun String.toTomaDeDatos(): TomaDatosFisicos {
    return Gson().fromJson( this, TomaDatosFisicos::class.java )
}

fun String.toProximoObjetivo(): ProximoObjetivo {
    return Gson().fromJson( this, ProximoObjetivo::class.java )
}

fun String.toRutina(): Rutina {
    return Gson().fromJson( this, Rutina::class.java )
}

fun String.toDia(): Dia {
    return Gson().fromJson( this, Dia::class.java )
}

@Suppress("UNCHECKED_CAST")
fun String.toList(): List<String> {
    return Gson().fromJson( this, List::class.java ) as List<String>
}

fun String.toAAMMDDHHMMSS(): String {
    return LocalDateTime.ofInstant( Instant.parse( this ), ZoneId.systemDefault() ).format( DateTimeFormatter.ofPattern( "yyyy-MM-dd'         'HH:mm:ss" ) )
}

fun List<String>.toJson(): String {
    return Gson().toJson( this )
}