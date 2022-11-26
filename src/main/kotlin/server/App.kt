package server

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import models.Alumno
import java.io.DataInputStream
import java.io.DataOutputStream
import java.lang.Exception
import java.net.ServerSocket
import java.net.Socket

private val json = Json

fun main() {
    val server: ServerSocket
    var cliente: Socket
    val puerto = 8081
    val alumnos = mutableListOf<Alumno>()

    println("Esperando conexiones...")
    try {
        server = ServerSocket(puerto)
        cliente = server.accept()
        while(cliente.isConnected){
            val content = DataInputStream(cliente.getInputStream())
            alumnos.add(json.decodeFromString(content.readUTF()))
            for (i in 0 until alumnos.size){
                println(alumnos[i].nombre)
            }
            val data = DataOutputStream(cliente.getOutputStream())
            println("Pasando los alumnos")
            for (i in 0 until alumnos.size){
                println(alumnos[i].nombre)
                data.writeUTF(json.encodeToString(alumnos.last()))
            }
            println("Alumnos pasados")
        }
        println("El cliente se ha desconectado")
    }catch (e: Exception) {
        println("Error en el server: ${e.message}")
    }

}
