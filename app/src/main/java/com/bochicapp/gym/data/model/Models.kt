package com.bochicapp.gym.data.model

import com.google.gson.Gson

data class Usuario(
    val id: String,
    var nombreusuario: String = "",
    var apellidos: String = "",
    var correo: String = "",
    var telefonoUsuario: String = "",
    var contrasena: String = "",
    var genero: String = "",
    var tomadatosfisicos: MutableList<String> = mutableListOf(),
    var idfotoperfil: String = "",
    var historial: MutableList<String> = mutableListOf(),
    var rutinas: MutableList<String> = mutableListOf(),
){
    fun toJson():String{
        return Gson().toJson(this)
    }
}

data class TomaDatosFisicos(
    val id: String,
    var fechatoma: String = "",
    var altura: Double = 0.0,
    var condicionesmedicas: String = "",
    var alergias: String = "",
    var proximosobjetivos: MutableList<String> = mutableListOf(),
    var fechacumplimiento: String = "",
    var imc: Double = 0.0,
    var indicegrasacorporal: Double = 0.0,
    var peso: Double = 0.0,
    var idrutinaseleccionada: Int
){
    fun toJson():String{
        return Gson().toJson(this)
    }
}

data class ProximoObjetivo(
    val id: String,
    var descripcionobjetivo: String = "",
    var unidaddemedida: String = "",
    var cantidadmedida: Double = 0.0
){
    fun toJson():String{
        return Gson().toJson(this)
    }
}

data class Ejecucion(
    val id: String,
    var idrutinaejecucion: String,
    var dia: Int = 0,
    var serie: Int = 0,
    var fechainicioserie: String = "",
    var fechafinalserie: String = "",
    var dificultadpercibida: Int = 0,
    var cantreps: Int = 0,
    var observacionesejecucion: String = ""
){
    fun toJson():String{
        return Gson().toJson(this)
    }
}

data class Rutina(
    val id: String,
    var dias: MutableList<String> = mutableListOf(),
    var descripcionrutina: String = "",
    var recomendacionesrutina: String = "",
    var idimagenrutina: String = ""
){
    fun toJson():String{
        return Gson().toJson(this)
    }
}

data class Dia(
    val id: String,
    var numerodia: Int = 0,
    var ejercicios: MutableList<String> = mutableListOf(),
){
    fun toJson():String{
        return Gson().toJson(this)
    }
}

data class Ejercicio(
    val id: String,
    var nombreejercicio: String = "",
    var descripcionejercicio: String = "",
    var maquina: String = "",
    var idvideoejercicio: String = "",
    var animacionejercicio: String = "",
    var series: MutableList<String> = mutableListOf()
){
    fun toJson():String{
        return Gson().toJson(this)
    }
}

data class Serie(
    val id: String,
    var repeticiones: Int = 0,
    var tejecucionund: String,
    var patronejecucion: String
){
    fun toJson():String{
        return Gson().toJson(this)
    }
}

data class TEjecucionUnd(
    val id: String,
    var tmin: Double = 0.0,
    var tmax: Double = 0.0
){
    fun toJson():String{
        return Gson().toJson(this)
    }
}

data class PatronEjecucion(
    val id: String,
    var tipopatron: String = "",
    var patron: MutableList<Int> = mutableListOf(0,0,0,0,0,0,0,0,0,0)
){
    fun toJson():String{
        return Gson().toJson(this)
    }
}