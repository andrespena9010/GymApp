package com.bochicapp.gym.data.model

import com.google.gson.Gson

fun String.toUser(): Usuario {
    return Gson().fromJson( this, Usuario::class.java )
}

fun String.toTomaDeDatos(): TomaDatosFisicos {
    return Gson().fromJson( this, TomaDatosFisicos::class.java )
}

fun String.toRutina(): Rutina {
    return Gson().fromJson( this, Rutina::class.java )
}