package models

import kotlinx.serialization.Serializable

@Serializable
class Alumno(){
    lateinit var nombre: String
    private lateinit var notas: Map<Int, Int> // id examen numérico y valor de la nota
    private lateinit var clase: String
    constructor(nombre:String, notas: Map<Int, Int>, clase: String): this() {
        this.nombre = nombre
        this.notas = notas
        this.clase = clase
    }
}
