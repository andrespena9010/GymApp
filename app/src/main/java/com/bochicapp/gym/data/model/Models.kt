package com.bochicapp.gym.data.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.bochicapp.gym.ui.model.GymView
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
    object Img: Type
    object Id: Type
    object Dobl: Type
    object Entero: Type
    object AutoFecha: Type
    object Compose: Type
    object Obj: Type
    object Ls: Type
}

data class Info(
    val launcherView: GymView? = null,
    val launcherViewId: String? = null,
    val launcherId: String? = null,
    val targetId: String? = null,
    val responseToId: String? = null,
    val selection: Boolean = false,
    val viewObject: Boolean = false
)

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

interface Option

sealed class Options {
    object GoTo: Option
    object Select: Option
    object See: Option
}

data class DataElement<Any>(
    val name: String = "",
    var value: MutableState<Any>,
    val type: Type = Dat.None,
    var action: ( GymViewModelClass, Option, String ) -> Unit = {_,_,_->},
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
    var idfotoperfil: String = "png_1",
    var tomadatosfisicos: String = "",
    var historial: String = "",
    var rutinas: String = ""
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
                type = Dat.Img
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
                action = { vm, opt, stringInfo ->
                    vm.goTo(
                        view = Views.TomaDatosFisicosView,
                        navInfo = Info(
                            launcherView = Views.UsuarioView,
                            launcherId = id,
                            targetId = tomadatosfisicos
                        )
                    )
                }
            ),
            DataElement(
                name = "Historial",
                value = mutableStateOf( historial ),
                type = Dat.Ls,
                action = { vm, opt, stringInfo ->
                    vm.goTo(
                        view = Views.TomaDatosFisicosView,
                        navInfo = Info(
                            launcherView = Views.UsuarioView,
                            launcherId = id,
                            targetId = historial
                        )
                    )
                }
            ),
            DataElement(
                name = "Rutinas",
                value = mutableStateOf( rutinas ),
                type = Dat.Ls,
                action = { vm, opt, stringInfo ->
                    vm.goTo(
                        view = Views.RutinasView,
                        navInfo = Info(
                            launcherView = Views.UsuarioView,
                            launcherId = id,
                            targetId = rutinas
                        )
                    )
                }
            )
        )
    }

}

@Suppress("UNCHECKED_CAST")
fun getUsuario( info: List<DataElement<Any>> ): Usuario {
    return Usuario(
        id = info.find { it.name == "Id" }?.value?.value.toString(),
        idfotoperfil = info.find { it.name == "Foto de perfil" }?.value?.value.toString(),
        nombreusuario = info.find { it.name == "Nombre" }?.value?.value.toString(),
        apellidos = info.find { it.name == "Apellidos" }?.value?.value.toString(),
        correo = info.find { it.name == "Correo electronico" }?.value?.value.toString(),
        telefonoUsuario = info.find { it.name == "Numero de telefono" }?.value?.value.toString(),
        contrasena = info.find { it.name == "Contraseña" }?.value?.value.toString(),
        genero = info.find { it.name == "Genero" }?.value?.value.toString(),
        tomadatosfisicos = info.find { it.name == "Tomas de datos fisicos" }?.value?.value.toString(),
        historial = info.find { it.name == "Historial" }?.value?.value.toString(),
        rutinas = info.find { it.name == "Rutinas" }?.value?.value.toString()
    )
}

data class TomaDatosFisicos(
    var id: String = "",
    var fechamodificacion: String = "",
    var altura: Double = 0.0,
    var condicionesmedicas: String = "",
    var alergias: String = "",
    var imc: Double = 0.0,
    var indicegrasacorporal: Double = 0.0,
    var peso: Double = 0.0,
    var idrutinaenejecucion: String = "",
    var proximosobjetivos: String = ""
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
                name = "Ultima Modificacion",
                value = mutableStateOf( fechamodificacion ),
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
                type = Dat.Obj,
                action = { vm, opt, stringInfo ->
                    when ( opt ){
                        Options.Select ->{
                            vm.goTo(
                                view = Views.RutinasView,
                                navInfo = Info(
                                    launcherViewId = stringInfo,
                                    launcherView = Views.TomaDatosFisicosView,
                                    launcherId = id,
                                    targetId = idrutinaenejecucion,
                                    selection = true
                                )
                            )
                        }
                        Options.See -> {
                            vm.goTo(
                                view = Views.RutinasView,
                                navInfo = Info(
                                    launcherViewId = stringInfo,
                                    launcherView = Views.TomaDatosFisicosView,
                                    launcherId = id,
                                    targetId = idrutinaenejecucion,
                                    viewObject = true
                                )
                            )
                        }
                    }
                }
            ),
            DataElement(
                name = "Objetivos planteados",
                value = mutableStateOf( proximosobjetivos ),
                type = Dat.Ls,
                action = { vm, opt, stringInfo ->
                    vm.goTo(
                        view = Views.ProximosObjetivosView,
                        navInfo = Info(
                            launcherViewId = stringInfo,
                            launcherView = Views.TomaDatosFisicosView,
                            launcherId = id,
                            targetId = proximosobjetivos
                        )
                    )
                }
            )
        )
    }

}

@Suppress("UNCHECKED_CAST")
fun getTomaDatosFisicos( info: List<DataElement<Any>> ): TomaDatosFisicos {
    return TomaDatosFisicos(
        id = info.find { it.name == "Id" }?.value?.value.toString(),
        fechamodificacion = info.find { it.name == "Ultima Modificacion" }?.value?.value.toString(),
        altura = info.find { it.name == "Altura" }?.value?.value.toString().toDouble(),
        condicionesmedicas = info.find { it.name == "Condiciones medicas" }?.value?.value.toString(),
        alergias = info.find { it.name == "Alergias" }?.value?.value.toString(),
        imc = info.find { it.name == "Indice de Masa Corporal" }?.value?.value.toString().toDouble(),
        indicegrasacorporal = info.find { it.name == "Indice de Grasa Corporal" }?.value?.value.toString().toDouble(),
        peso = info.find { it.name == "Peso" }?.value?.value.toString().toDouble(),
        idrutinaenejecucion = info.find { it.name == "Rutina relacionada con la toma" }?.value?.value.toString(),
        proximosobjetivos = info.find { it.name == "Objetivos planteados" }?.value?.value.toString()
    )
}

data class ProximoObjetivo(
    var id: String = "",
    var descripcionobjetivo: String = "",
    var unidaddemedida: String = "",
    var cantidadmedidaactual: Double = 0.0,
    var cantidadmedidadeseada: Double = 0.0,
    var fechamodificacion: String = "",
    var fechacumplimiento: String = "",
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
                name = "Ultima Modificacion",
                value = mutableStateOf( fechamodificacion ),
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
        id = info.find { it.name == "Id" }?.value?.value.toString(),
        descripcionobjetivo = info.find { it.name == "Descripcion del objetivo" }?.value?.value.toString(),
        unidaddemedida = info.find { it.name == "Unidad de medida del objetivo" }?.value?.value.toString(),
        cantidadmedidaactual = info.find { it.name == "Medida actual" }?.value?.value.toString().toDouble(),
        cantidadmedidadeseada = info.find { it.name == "Medida deseada" }?.value?.value.toString().toDouble(),
        fechamodificacion = info.find { it.name == "Ultima Modificacion" }?.value?.value.toString(),
        fechacumplimiento = info.find { it.name == "Fecha de cumplimiento" }?.value?.value.toString()
    )
}

data class Rutina(
    var id: String = "",
    var fechamodificacion: String = "",
    var descripcionrutina: String = "",
    var recomendacionesrutina: String = "",
    var idimagenrutina: String = "png_2",
    var dias: String = ""
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
                name = "Ultima Modificacion",
                value = mutableStateOf( fechamodificacion ),
                type = Dat.AutoFecha
            ),
            DataElement(
                name = "Imagen rutina",
                value = mutableStateOf( idimagenrutina ),
                type = Dat.Img
            ),
            DataElement(
                name = "Descripcion de la rutina",
                value = mutableStateOf( descripcionrutina ),
                type = Dat.Txt,
                validate = true
            ),
            DataElement(
                name = "Recomendaciones",
                value = mutableStateOf( recomendacionesrutina ),
                type = Dat.Txt,
                validate = true
            ),
            DataElement(
                name = "Dias",
                value = mutableStateOf( dias ),
                type = Dat.Ls,
                action = { vm, opt, stringInfo ->
                    /*vm.goTo(
                        view = Views.ProximosObjetivosView,
                        id = dias
                    )*/
                }
            )
        )
    }

}

fun getRutina( info: List<DataElement<Any>> ): Rutina {
    return Rutina(
        id = info.find { it.name == "Id" }?.value?.value.toString(),
        fechamodificacion = info.find { it.name == "Ultima Modificacion" }?.value?.value.toString(),
        descripcionrutina = info.find { it.name == "Descripcion de la rutina" }?.value?.value.toString(),
        recomendacionesrutina = info.find { it.name == "Recomendaciones" }?.value?.value.toString(),
        idimagenrutina = info.find { it.name == "Imagen rutina" }?.value?.value.toString(),
        dias = info.find { it.name == "Dias" }?.value?.value.toString(),
    )
}

data class Dia(
    var id: String = "",
    var numerodia: Int = 0,
    var ejercicios: String = "",
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
                name = "Numero Dia",
                value = mutableStateOf( numerodia ),
                type = Dat.Entero
            ),
            DataElement(
                name = "Ejercicios",
                value = mutableStateOf( ejercicios ),
                type = Dat.Ls,
                action = { vm, opt, stringInfo ->
                    /*vm.goTo(
                        view = Views.ProximosObjetivosView,
                        id = dias
                    )*/
                }
            )
        )
    }
}

fun getDia( info: List<DataElement<Any>> ): Dia {
    return Dia(
        id = info.find { it.name == "Id" }?.value?.value.toString(),
        numerodia = info.find { it.name == "Numero Dia" }?.value?.value.toString().toInt(),
        ejercicios = info.find { it.name == "Ejercicios" }?.value?.value.toString(),
    )
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

data class Ejecucion(
    val id: String,
    var idusuario: String = "",
    var idrutinaejecucion: String,
    var dia: Int = 0,
    var serie: Int = 0,
    var fechainicioserie: String = "",
    var fechafinalserie: String = "",
    var dificultadpercibida: Int = 0, // TODO: Incluir serie de descanso
    var cantreps: Int = 0,
    var observacionesejecucion: String = ""
){
    fun toJson():String{
        return Gson().toJson(this)
    }
}