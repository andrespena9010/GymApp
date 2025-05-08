package com.bochicapp.gym.data.model

import com.google.gson.Gson

fun String.toUserData(): UsuarioData {
    return Gson().fromJson( this, UsuarioData::class.java )
}

fun String.toTomaDeDatosData(): TomaDatosFisicosData {
    return Gson().fromJson( this, TomaDatosFisicosData::class.java )
}

fun String.toProximoObjetivoData(): ProximoObjetivoData {
    return Gson().fromJson( this, ProximoObjetivoData::class.java )
}

fun String.toRutinaData(): RutinaData {
    return Gson().fromJson( this, RutinaData::class.java )
}