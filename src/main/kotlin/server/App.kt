package server

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import models.Alumno
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.ObjectOutputStream
import java.lang.Exception
import java.net.ServerSocket
import java.net.Socket

private val json = Json

fun main() {
    val server: ServerSocket
    val cliente: Socket
    val puerto = 8081
    val alumnos = mutableListOf<Alumno>()

    println("Esperando conexiones...")
    try {
        server = ServerSocket(puerto)
        cliente = server.accept()
        while(cliente.isConnected) {
            val content = DataInputStream(cliente.getInputStream())
            val data = DataOutputStream(cliente.getOutputStream())
            val selec = content.readUTF().toInt()
            println("Opción elegida: $selec")
            when (selec) {
                1 -> {
                    alumnos.add(json.decodeFromString(content.readUTF()))
                }

                2 -> {
                    println("Pasando los alumnos")
                    /*for (i in 0 until alumnos.size){
                        println(alumnos[i].nombre)
                        data.writeUTF(json.encodeToString(alumnos.))
                    }*/
                    var numList = alumnos.size -1
                    /*alumnos.forEach { _ ->
                        data.writeUTF(json.encodeToString(alumnos.last()))
                        println(alumnos.removeLast().nombre)
                    }*/
                    ObjectOutputStream(cliente.getOutputStream()).writeUTF(json.encodeToString(alumnos))
                    println("Alumnos pasados")
                    while (alumnos.size > 0){
                        println("Añadiendo alumnos...")
                        alumnos.add(json.decodeFromString(content.readUTF()))
                        numList--
                    }
                    println("Lista de alumnos actualizada")
                    for (i in 0 until alumnos.size){
                        println(alumnos[i].nombre)
                    }

                }

                3 -> {
                    println("Pasando lista los alumnos")
                    for (i in 0 until alumnos.size){
                        println(alumnos[i].nombre)
                        data.writeUTF(json.encodeToString(alumnos[i]))
                    }
                    println("Alumnos pasados")
                    alumnos.clear()
                    alumnos.add(json.decodeFromString(content.readUTF()))
                    println("Lista de alumnos actualizada")
                    for (i in 0 until alumnos.size){
                        println(alumnos[i].nombre)
                    }
                }
            }
        }
        println("El cliente se ha desconectado")
    }catch (e: Exception) {
        println("Error en el server: ${e.message}")
    }

}