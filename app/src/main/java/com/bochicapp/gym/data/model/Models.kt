package com.bochicapp.gym.data.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.bochicapp.gym.ui.model.Views
import com.bochicapp.gym.ui.viewmodel.GymViewModelClass
import com.google.gson.Gson

fun comparaObjetos(l1: List<DataElement<Any>>, l2: List<DataElement<Any>> ): Boolean {
    l1.forEachIndexed { i, el ->
        if ( el.value.value != l2[i].value.value ) return false
    }
    return true
}

fun comparaListas( l1: List<List<DataElement<Any>>>, l2: List<List<DataElement<Any>>> ): Boolean {
    l1.forEachIndexed { i, el ->
        if ( el != l2[i] ) return false
    }
    return true
}

fun firstEmpty(obj: List<DataElement<Any>> ): DataElement<Any>? {
    return obj.firstOrNull { it.validate && it.value.value.toString().isEmpty() }
}

interface Type

sealed class Dat {
    object None: Type
    object Txt: Type
    object Png: Type
    object Id: Type
    object Dobl: Type
    object Lnk: Type
    object AutoFecha: Type
    object Compose: Type
}

data class LazyElements(
    val dataElements: List<DataElement<Any>> = listOf(),
    var lazyElements: List<DataElement<Any>> = dataElements
){
    fun addElement( element: DataElement<Any>, i: Int ){
        val tmp = lazyElements.toMutableList()
        tmp.add( i, element )
        lazyElements = tmp.toList()
    }

    fun addElement( element: DataElement<Any> ){
        val tmp = lazyElements.toMutableList()
        tmp.add( element )
        lazyElements = tmp.toList()
    }
}

data class DataElement<Any>(
    val name: String = "",
    var value: MutableState<Any>,
    val type: Type = Dat.None,
    var onClick: (GymViewModelClass) -> Unit = {},
    var validate: Boolean = false
)

data class Usuario(
    var id: String = "",
    var nombreusuario: String = "",
    var apellidos: String = "",
    var correo: String = "",
    var telefonoUsuario: String = "",
    var contrasena: String = "",
    var genero: String = "",
    var idfotoperfil: String = ""
){

    fun toJson():String{
        return Gson().toJson(this)
    }

    fun toDataList(): List<DataElement<Any>> {
        return listOf(
            DataElement(
                name = "Id",
                value = mutableStateOf( id ),
                type = Dat.Id
            ),
            DataElement(
                name = "Foto de perfil",
                value = mutableStateOf( idfotoperfil ),
                type = Dat.Png
            ),
            DataElement(
                name = "Nombre",
                value = mutableStateOf( nombreusuario ),
                type = Dat.Txt
            ),
            DataElement(
                name = "Apellidos",
                value = mutableStateOf( apellidos ),
                type = Dat.Txt
            ),
            DataElement(
                name = "Correo electronico",
                value = mutableStateOf( correo ),
                type = Dat.Txt
            ),
            DataElement(
                name = "Numero de telefono",
                value = mutableStateOf( telefonoUsuario ),
                type = Dat.Txt
            ),
            DataElement(
                name = "Contraseña",
                value = mutableStateOf( contrasena ),
                type = Dat.Txt
            ),
            DataElement(
                name = "Genero",
                value = mutableStateOf( genero ),
                type = Dat.Txt
            ),
            DataElement(
                name = "Tomas de datos fisicos",
                value = mutableStateOf( "" ),
                type = Dat.Lnk,
                onClick = { vm ->
                    vm.goTo( Views.TomaDatosV, id )
                }
            ),
            DataElement(
                name = "Historial",
                value = mutableStateOf( "" ),
                type = Dat.Lnk,
                onClick = { vm ->
                    vm.goTo( Views.TomaDatosV, id )
                }
            ),
            DataElement(
                name = "Rutinas",
                value = mutableStateOf( "" ),
                type = Dat.Lnk,
                onClick = { vm ->
                    vm.goTo( Views.TomaDatosV, id )
                }
            )
        )
    }

}

@Suppress("UNCHECKED_CAST")
fun getUsuario( info: List<DataElement<Any>> ): Usuario {
    return Usuario(
        id = info.find { it.name == "Id" }?.value?.value as String,
        idfotoperfil = info.find { it.name == "Foto de perfil" }?.value?.value as String,
        nombreusuario = info.find { it.name == "Nombre" }?.value?.value as String,
        apellidos = info.find { it.name == "Apellidos" }?.value?.value as String,
        correo = info.find { it.name == "Correo electronico" }?.value?.value as String,
        telefonoUsuario = info.find { it.name == "Numero de telefono" }?.value?.value as String,
        contrasena = info.find { it.name == "Contraseña" }?.value?.value as String,
        genero = info.find { it.name == "Genero" }?.value?.value as String
    )
}

data class TomaDatosFisicos(
    var id: String = "",
    var idusuario: String = "",
    var fechatoma: String = "",
    var altura: Double = 0.0,
    var condicionesmedicas: String = "",
    var alergias: String = "",
    var imc: Double = 0.0,
    var indicegrasacorporal: Double = 0.0,
    var peso: Double = 0.0,
    var idrutinaenejecucion: String = ""
){

    fun toJson():String{
        return Gson().toJson(this)
    }

    fun toDataList(): List<DataElement<Any>> {
        return listOf(
            DataElement(
                name = "Id",
                value = mutableStateOf( id ),
                type = Dat.Id
            ),
            DataElement(
                name = "Id usuario",
                value = mutableStateOf( id ),
                type = Dat.Id
            ),
            DataElement(
                name = "Fecha de la toma",
                value = mutableStateOf( fechatoma ),
                type = Dat.AutoFecha
            ),
            DataElement(
                name = "Altura",
                value = mutableStateOf( altura ),
                type = Dat.Dobl,
                validate = true
            ),
            DataElement(
                name = "Condiciones medicas",
                value = mutableStateOf( condicionesmedicas ),
                type = Dat.Txt,
                validate = true
            ),
            DataElement(
                name = "Alergias",
                value = mutableStateOf( alergias ),
                type = Dat.Txt,
                validate = true
            ),
            DataElement(
                name = "Indice de Masa Corporal",
                value = mutableStateOf( imc ),
                type = Dat.Dobl,
                validate = true
            ),
            DataElement(
                name = "Indice de Grasa Corporal",
                value = mutableStateOf( indicegrasacorporal ),
                type = Dat.Dobl,
                validate = true
            ),
            DataElement(
                name = "Peso",
                value = mutableStateOf( peso ),
                type = Dat.Dobl,
                validate = true
            ),
            DataElement(
                name = "Rutina relacionada con la toma",
                value = mutableStateOf( idrutinaenejecucion ),
                type = Dat.Lnk,
                validate = true
            ),
            DataElement(
                name = "Objetivos planteados",
                value = mutableStateOf( "" ),
                type = Dat.Lnk,
                validate = true
            )
        )
    }

}

@Suppress("UNCHECKED_CAST")
fun getTomaDatosFisicos( info: List<DataElement<Any>> ): TomaDatosFisicos {
    return TomaDatosFisicos(
        id = info.find { it.name == "Id" }?.value?.value as String,
        idusuario = info.find { it.name == "Id usuario" }?.value?.value as String,
        fechatoma = info.find { it.name == "Fecha de la toma" }?.value?.value as String,
        altura = info.find { it.name == "Altura" }?.value?.value as Double,
        condicionesmedicas = info.find { it.name == "Condiciones medicas" }?.value?.value as String,
        alergias = info.find { it.name == "Alergias" }?.value?.value as String,
        imc = info.find { it.name == "Indice de Masa Corporal" }?.value?.value as Double,
        indicegrasacorporal = info.find { it.name == "Indice de Grasa Corporal" }?.value?.value as Double,
        peso = info.find { it.name == "Peso" }?.value?.value as Double,
        idrutinaenejecucion = info.find { it.name == "Rutina relacionada con la toma" }?.value?.value as String
    )
}

data class ProximoObjetivo(
    var id: String = "",
    var idTomaDeDatos: String = "",
    var descripcionobjetivo: String = "",
    var unidaddemedida: String = "",
    var cantidadmedidaactual: Double = 0.0,
    var cantidadmedidadeseada: Double = 0.0,
    var fechacreacion: String = "",
    var fechacumplimiento: String = "",
){

    fun extension(): String {
        return "POB"
    }

    fun toJson():String{
        return Gson().toJson(this)
    }

    fun toDataList(): List<DataElement<Any>> {
        return listOf(
            DataElement(
                name = "Id",
                value = mutableStateOf( id ),
                type = Dat.Id
            ),
            DataElement(
                name = "Id toma de datos",
                value = mutableStateOf( id ),
                type = Dat.Id
            ),
            DataElement(
                name = "Descripcion del objetivo",
                value = mutableStateOf( descripcionobjetivo ),
                type = Dat.Txt,
                validate = true
            ),
            DataElement(
                name = "Unidad de medida del objetivo",
                value = mutableStateOf( unidaddemedida ),
                type = Dat.Txt,
                validate = true
            ),
            DataElement(
                name = "Medida actual",
                value = mutableStateOf( cantidadmedidaactual ),
                type = Dat.Dobl,
                validate = true
            ),
            DataElement(
                name = "Medida deseada",
                value = mutableStateOf( cantidadmedidadeseada ),
                type = Dat.Dobl,
                validate = true
            ),
            DataElement(
                name = "Fecha de creacion",
                value = mutableStateOf( fechacreacion ),
                type = Dat.AutoFecha
            ),
            DataElement(
                name = "Fecha de cumplimiento",
                value = mutableStateOf( fechacumplimiento ),
                type = Dat.AutoFecha
            )
        )
    }

}

fun getProximoObjetivo( info: List<DataElement<Any>> ): ProximoObjetivo {
    return ProximoObjetivo(
        id = info.find { it.name == "Id" }?.value?.value as String,
        idTomaDeDatos = info.find { it.name == "Id toma de datos" }?.value?.value as String,
        descripcionobjetivo = info.find { it.name == "Descripcion del objetivo" }?.value?.value as String,
        unidaddemedida = info.find { it.name == "Unidad de medida del objetivo" }?.value?.value as String,
        cantidadmedidaactual = info.find { it.name == "Medida actual" }?.value?.value as Double,
        cantidadmedidadeseada = info.find { it.name == "Medida deseada" }?.value?.value as Double,
        fechacreacion = info.find { it.name == "Fecha de creacion" }?.value?.value as String,
        fechacumplimiento = info.find { it.name == "Fecha de cumplimiento" }?.value?.value as String
    )
}

data class Ejecucion(
    val id: String,
    var idusuario: String = "",
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
    var idusuario: String = "",
    var dias: MutableList<String> = mutableListOf(),
    var descripcionrutina: String = "",
    var recomendacionesrutina: String = "",
    var idimagenrutina: String = ""
)

data class RutinaData(
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