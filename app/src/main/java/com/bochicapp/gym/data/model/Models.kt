package com.bochicapp.gym.data.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.bochicapp.gym.ui.model.Views
import com.bochicapp.gym.ui.viewmodel.GymViewModelClass
import com.google.gson.Gson

fun comparaObjetos(l1: List<DataElement>, l2: List<DataElement> ): Boolean {
    l1.forEachIndexed { i, el ->
        if ( el.value?.value != l2[i].value?.value ) return false
    }
    return true
}

interface Type

sealed class Data {
    object None: Type
    object Txt: Type
    object Png: Type
    object Ls: Type
    object Id: Type
    object Dobl: Type
    object Lnk: Type
}

data class DataElement(
    val name: String,
    var value: MutableState<Any>? = null,
    val type: Type = Data.None,
    var onClick: ( GymViewModelClass ) -> Unit = {}
)

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

    fun toDataList(): List<DataElement> {
        return listOf(
            DataElement(
                name = "Id",
                value = mutableStateOf( id ),
                type = Data.Id
            ),
            DataElement(
                name = "Foto de perfil",
                value = mutableStateOf( idfotoperfil ),
                type = Data.Png
            ),
            DataElement(
                name = "Nombre",
                value = mutableStateOf( nombreusuario ),
                type = Data.Txt
            ),
            DataElement(
                name = "Apellidos",
                value = mutableStateOf( apellidos ),
                type = Data.Txt
            ),
            DataElement(
                name = "Correo electronico",
                value = mutableStateOf( correo ),
                type = Data.Txt
            ),
            DataElement(
                name = "Numero de telefono",
                value = mutableStateOf( telefonoUsuario ),
                type = Data.Txt
            ),
            DataElement(
                name = "Contraseña",
                value = mutableStateOf( contrasena ),
                type = Data.Txt
            ),
            DataElement(
                name = "Genero",
                value = mutableStateOf( genero ),
                type = Data.Txt
            ),
            DataElement(
                name = "Tomas de datos fisicos",
                value = mutableStateOf( tomadatosfisicos ),
                type = Data.Ls,
                onClick = { vm ->
                    vm.goTo( Views.TomaDatosV )
                }
            ),
            DataElement(
                name = "Historial",
                value = mutableStateOf( historial ),
                type = Data.Ls,
                onClick = { vm ->
                    vm.goTo( Views.TomaDatosV )
                }
            ),
            DataElement(
                name = "Rutinas",
                value = mutableStateOf( rutinas ),
                type = Data.Ls,
                onClick = { vm ->
                    vm.goTo( Views.TomaDatosV )
                }
            )
        )
    }

    fun updatedCopy( info: List<DataElement> ): Usuario {
        return Usuario(
            id = info[0].value?.value.toString(),
            idfotoperfil = info[1].value?.value.toString(),
            nombreusuario = info[2].value?.value.toString(),
            apellidos = info[3].value?.value.toString(),
            correo = info[4].value?.value.toString(),
            telefonoUsuario = info[5].value?.value.toString(),
            contrasena = info[6].value?.value.toString(),
            genero = info[7].value?.value.toString(),
            tomadatosfisicos = info[8].value?.value as MutableList<String>,
            historial = info[9].value?.value as MutableList<String>,
            rutinas = info[10].value?.value as MutableList<String>,
        )
    }
}

data class TomaDatosFisicos(
    val id: String,
    var fechatoma: String = "",
    var altura: Double = 0.0,
    var condicionesmedicas: String = "",
    var alergias: String = "",
    var imc: Double = 0.0,
    var indicegrasacorporal: Double = 0.0,
    var peso: Double = 0.0,
    var idrutinaenejecucion: String = "",
    var proximosobjetivos: MutableList<String> = mutableListOf()
){
    fun toJson():String{
        return Gson().toJson(this)
    }

    fun toDataList(): List<DataElement> {
        return listOf(
            DataElement(
                name = "Id",
                value = mutableStateOf( id ),
                type = Data.Id
            ),
            DataElement(
                name = "Fecha de la toma",
                value = mutableStateOf( fechatoma ),
                type = Data.Txt
            ),
            DataElement(
                name = "Altura",
                value = mutableStateOf( altura ),
                type = Data.Dobl
            ),
            DataElement(
                name = "Condiciones medicas",
                value = mutableStateOf( condicionesmedicas ),
                type = Data.Txt
            ),
            DataElement(
                name = "Alergias",
                value = mutableStateOf( alergias ),
                type = Data.Txt
            ),
            DataElement(
                name = "Indice de Masa Corporal",
                value = mutableStateOf( imc ),
                type = Data.Dobl
            ),
            DataElement(
                name = "Indice de Grasa Corporal",
                value = mutableStateOf( indicegrasacorporal ),
                type = Data.Dobl
            ),
            DataElement(
                name = "Peso",
                value = mutableStateOf( peso ),
                type = Data.Dobl
            ),
            DataElement(
                name = "Rutina relacionada con la toma",
                value = mutableStateOf( idrutinaenejecucion ),
                type = Data.Lnk
            ),
            DataElement(
                name = "Proximos objetivos",
                value = mutableStateOf( proximosobjetivos ),
                type = Data.Ls
            )
        )
    }

    fun createNewCopy( info: List<DataElement> ): TomaDatosFisicos {
        return TomaDatosFisicos(
            id = info[0].value?.value.toString(), // TODO: crear copia con id segun datos
            fechatoma = info[1].value?.value.toString(),
            altura = info[2].value?.value.toString().toDouble(),
            condicionesmedicas = info[3].value?.value.toString(),
            alergias = info[4].value?.value.toString(),
            imc = info[5].value?.value.toString().toDouble(),
            indicegrasacorporal = info[6].value?.value.toString().toDouble(),
            peso = info[7].value?.value.toString().toDouble(),
            idrutinaenejecucion = info[8].value?.value.toString(),
            proximosobjetivos = info[9].value?.value as MutableList<String>,
        )
    }

}

data class ProximoObjetivo(
    val id: String,
    var descripcionobjetivo: String = "",
    var unidaddemedida: String = "",
    var cantidadmedida: Double = 0.0,
    var fechacreacion: String = "",
    var fechacumplimiento: String = "",
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