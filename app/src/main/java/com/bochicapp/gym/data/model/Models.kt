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

fun allComplete( obj: List<DataElement<Any>> ): Boolean {
    return !obj.any { ( it.type == Dat.Txt || it.type == Dat.Dobl ) && it.value.value.toString().isEmpty() }
}

interface Type

sealed class Dat {
    object None: Type
    object Txt: Type
    object Png: Type
    object Ls: Type
    object Id: Type
    object Dobl: Type
    object Lnk: Type
    object AutoFecha: Type
}

data class DataElement<V>(
    val name: String,
    var value: MutableState<V>,
    val type: Type = Dat.None,
    var onClick: (GymViewModelClass) -> Unit = {}
)

data class Usuario(
    var id: String = "",
    var nombreusuario: String = "",
    var apellidos: String = "",
    var correo: String = "",
    var telefonoUsuario: String = "",
    var contrasena: String = "",
    var genero: String = "",
    var idfotoperfil: String = "",
    var tomadatosfisicos: List<TomaDatosFisicos> = listOf(),
    var ejecuciones: List<Ejecucion> = listOf(),
    var rutinas: List<Rutina> = listOf()
){

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
                value = mutableStateOf( tomadatosfisicos ),
                type = Dat.Ls,
                onClick = { vm ->
                    vm.goTo( Views.TomaDatosV )
                }
            ),
            DataElement(
                name = "Historial",
                value = mutableStateOf( ejecuciones ),
                type = Dat.Ls,
                onClick = { vm ->
                    vm.goTo( Views.TomaDatosV )
                }
            ),
            DataElement(
                name = "Rutinas",
                value = mutableStateOf( rutinas ),
                type = Dat.Ls,
                onClick = { vm ->
                    vm.goTo( Views.TomaDatosV )
                }
            )
        )
    }

    @Suppress("UNCHECKED_CAST")
    fun updatedCopy( info: List<DataElement<Any>> ): Usuario {
        return Usuario(
            id = info[0].value.value as String,
            idfotoperfil = info[1].value.value as String,
            nombreusuario = info[2].value.value as String,
            apellidos = info[3].value.value as String,
            correo = info[4].value.value as String,
            telefonoUsuario = info[5].value.value as String,
            contrasena = info[6].value.value as String,
            genero = info[7].value.value as String,
            tomadatosfisicos = info[8].value.value as List<TomaDatosFisicos>,
            ejecuciones = info[9].value.value as List<Ejecucion>,
            rutinas = info[10].value.value as List<Rutina>,
        )
    }

    fun toUserData(): UsuarioData {
        return UsuarioData(
            id = id,
            nombreusuario = nombreusuario,
            apellidos = apellidos,
            correo = apellidos,
            telefonoUsuario = telefonoUsuario,
            contrasena = contrasena,
            genero = genero,
            idfotoperfil = idfotoperfil,
            tomadatosfisicos = tomadatosfisicos.map { it.id },
            ejecuciones = ejecuciones.map { it.id },
            rutinas = rutinas.map { it.id }
        )
    }

}

data class UsuarioData(
    val id: String = "",
    val nombreusuario: String = "",
    val apellidos: String = "",
    val correo: String = "",
    val telefonoUsuario: String = "",
    val contrasena: String = "",
    val genero: String = "",
    val idfotoperfil: String = "",
    val tomadatosfisicos: List<String> = listOf(),
    val ejecuciones: List<String> = listOf(),
    val rutinas: List<String> = listOf()
){

    fun toObj(
        tomaDatos: List<TomaDatosFisicos>,
        ejecuciones: List<Ejecucion>,
        rutinas: List<Rutina>
    ): Usuario {
        return Usuario(
            id = id,
            nombreusuario = nombreusuario,
            apellidos = apellidos,
            correo = apellidos,
            telefonoUsuario = telefonoUsuario,
            contrasena = contrasena,
            genero = genero,
            idfotoperfil = idfotoperfil,
            tomadatosfisicos = tomaDatos,
            ejecuciones = ejecuciones,
            rutinas = rutinas
        )
    }

    fun toJson():String{
        return Gson().toJson(this)
    }

}

data class TomaDatosFisicos(
    val id: String = "",
    var fechatoma: String = "",
    var altura: Double = 0.0,
    var condicionesmedicas: String = "",
    var alergias: String = "",
    var imc: Double = 0.0,
    var indicegrasacorporal: Double = 0.0,
    var peso: Double = 0.0,
    var idrutinaenejecucion: String = "",
    var proximosobjetivos: List<ProximoObjetivo> = listOf()
){

    fun toDataList(): List<DataElement<Any>> {
        return listOf(
            DataElement(
                name = "Id",
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
                type = Dat.Dobl
            ),
            DataElement(
                name = "Condiciones medicas",
                value = mutableStateOf( condicionesmedicas ),
                type = Dat.Txt
            ),
            DataElement(
                name = "Alergias",
                value = mutableStateOf( alergias ),
                type = Dat.Txt
            ),
            DataElement(
                name = "Indice de Masa Corporal",
                value = mutableStateOf( imc ),
                type = Dat.Dobl
            ),
            DataElement(
                name = "Indice de Grasa Corporal",
                value = mutableStateOf( indicegrasacorporal ),
                type = Dat.Dobl
            ),
            DataElement(
                name = "Peso",
                value = mutableStateOf( peso ),
                type = Dat.Dobl
            ),
            DataElement(
                name = "Rutina relacionada con la toma",
                value = mutableStateOf( idrutinaenejecucion ),
                type = Dat.Lnk
            ),
            DataElement(
                name = "Objetivos planteados",
                value = mutableStateOf( proximosobjetivos ),
                type = Dat.Ls
            )
        )
    }

    @Suppress("UNCHECKED_CAST")
    fun createNewCopy( info: List<DataElement<Any>> ): TomaDatosFisicosData {
        return TomaDatosFisicosData(
            id = info[0].value.value.toString(), // TODO: crear copia con id segun datos
            fechatoma = info[1].value.value.toString(),
            altura = info[2].value.value.toString().toDouble(),
            condicionesmedicas = info[3].value.value.toString(),
            alergias = info[4].value.value.toString(),
            imc = info[5].value.value.toString().toDouble(),
            indicegrasacorporal = info[6].value.value.toString().toDouble(),
            peso = info[7].value.value.toString().toDouble(),
            idrutinaenejecucion = info[8].value.value.toString(),
            proximosobjetivos = info[9].value.value as List<String>,
        )
    }

    fun toTomaDatosFisicosData(): TomaDatosFisicosData {
        return TomaDatosFisicosData(
            id = id,
            fechatoma = fechatoma,
            altura = altura,
            condicionesmedicas = condicionesmedicas,
            alergias = alergias,
            imc = imc,
            indicegrasacorporal = indicegrasacorporal,
            peso = peso,
            idrutinaenejecucion = idrutinaenejecucion,
            proximosobjetivos = proximosobjetivos.map { it.id }
        )
    }

}

data class TomaDatosFisicosData(
    val id: String = "",
    var fechatoma: String = "",
    var altura: Double = 0.0,
    var condicionesmedicas: String = "",
    var alergias: String = "",
    var imc: Double = 0.0,
    var indicegrasacorporal: Double = 0.0,
    var peso: Double = 0.0,
    var idrutinaenejecucion: String = "",
    var proximosobjetivos: List<String> = listOf()
){

    fun toObj( objetivos: List<ProximoObjetivo> ): TomaDatosFisicos {
        return TomaDatosFisicos(
            id = id,
            fechatoma = fechatoma,
            altura = altura,
            condicionesmedicas = condicionesmedicas,
            alergias = alergias,
            imc = imc,
            indicegrasacorporal = indicegrasacorporal,
            peso = peso,
            idrutinaenejecucion = idrutinaenejecucion,
            proximosobjetivos = objetivos
        )
    }

    fun toJson():String{
        return Gson().toJson(this)
    }

}

data class ProximoObjetivo(
    val id: String = "",
    val idtomadedatos: String = "",
    var descripcionobjetivo: String = "",
    var unidaddemedida: String = "",
    var cantidadmedidaactual: Double = 0.0,
    var cantidadmedidadeseada: Double = 0.0,
    var fechacreacion: String = "",
    var fechacumplimiento: String = "",
){

    fun toDataList(): List<DataElement<Any>> {
        return listOf(
            DataElement(
                name = "Id",
                value = mutableStateOf( id ),
                type = Dat.Id
            ),
            DataElement(
                name = "Id toma de datos asociada",
                value = mutableStateOf( idtomadedatos ),
                type = Dat.Id
            ),
            DataElement(
                name = "Descripcion del objetivo",
                value = mutableStateOf( descripcionobjetivo ),
                type = Dat.Txt
            ),
            DataElement(
                name = "Unidad de medida del objetivo",
                value = mutableStateOf( unidaddemedida ),
                type = Dat.Txt
            ),
            DataElement(
                name = "Medida actual",
                value = mutableStateOf( cantidadmedidaactual ),
                type = Dat.Dobl
            ),
            DataElement(
                name = "Medida deseada",
                value = mutableStateOf( cantidadmedidadeseada ),
                type = Dat.Dobl
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

    fun createNewCopy( info: List<DataElement<Any>> ): ProximoObjetivoData {
        return ProximoObjetivoData(
            id = info[0].value.value.toString(), // TODO: crear copia con id segun datos
            idtomadedatos = info[1].value.value.toString(),
            descripcionobjetivo = info[2].value.value.toString(),
            unidaddemedida = info[3].value.value.toString(),
            cantidadmedidaactual = info[4].value.value.toString().toDouble(),
            cantidadmedidadeseada = info[5].value.value.toString().toDouble(),
            fechacreacion = info[6].value.value.toString(),
            fechacumplimiento = info[7].value.value.toString()
        )
    }

    fun toProximoObjetivoData(): ProximoObjetivoData {
        return ProximoObjetivoData(
            id = id,
            idtomadedatos = idtomadedatos,
            descripcionobjetivo = descripcionobjetivo,
            unidaddemedida = unidaddemedida,
            cantidadmedidaactual = cantidadmedidaactual,
            cantidadmedidadeseada = cantidadmedidadeseada,
            fechacreacion = fechacreacion,
            fechacumplimiento = fechacumplimiento
        )
    }

}

data class ProximoObjetivoData(
    val id: String,
    val idtomadedatos: String = "",
    var descripcionobjetivo: String = "",
    var unidaddemedida: String = "",
    var cantidadmedidaactual: Double = 0.0,
    var cantidadmedidadeseada: Double = 0.0,
    var fechacreacion: String = "",
    var fechacumplimiento: String = "",
){
    fun toObj(): ProximoObjetivo {
        return ProximoObjetivo(
            id = id,
            idtomadedatos = idtomadedatos,
            descripcionobjetivo = descripcionobjetivo,
            unidaddemedida = unidaddemedida,
            cantidadmedidaactual = cantidadmedidaactual,
            cantidadmedidadeseada = cantidadmedidadeseada,
            fechacreacion = fechacreacion,
            fechacumplimiento = fechacumplimiento
        )
    }

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