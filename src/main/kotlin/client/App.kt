package client

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import models.Alumno
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.InetAddress
import java.net.Socket

private val json = Json
val a: HashMap<Int, Int> = HashMap()
fun main() {
    val direccion: InetAddress
    val server: Socket
    val puerto = 8081
    val listaAlumnos = mutableListOf<Alumno>()
    val alumno = Alumno()
    //val alumno: Alumno = Alumno(nombre = "1", notas = a , clase = "Clase 1")

    println("Conectando...")
    try {
        direccion = InetAddress.getLocalHost()
        server = Socket(direccion, puerto)
        println("Qué acción desea realizar? ")
        a.clear()
        val content = DataInputStream(server.getInputStream())
        while (true){
            val data = DataOutputStream(server.getOutputStream())
            var input: Int
            do {
                println("1.Añadir alumno: \n2.Actualizar alumno: \n3.Borrar alumno ")
                input = readln().toInt()
            }while (input < 1 || input > 3)

            when (input) {
                1 -> {
                    println("Introduzca las notas del alumno (número de examen: , nota: )")
                    introducirNotas()
                    println("Introduzca los datos del alumno (nombre, clase)")
                    val nombre = readln()
                    val clase = readln()
                    val new = Alumno(nombre, notas = a, clase)
                    data.writeUTF(json.encodeToString(new))
                    println("Nuevo alumno introducido...")
                }
                2 -> {
                    listaAlumnos.add(json.decodeFromString(content.readUTF()))
                    var acAlumno: String
                    var contiene= false
                    do{
                        println("Introduzca el nombre del alumno a actualizar")
                        acAlumno = readln()
                        alumno.nombre = acAlumno
                        for (i in 0 until listaAlumnos.size){
                            if (listaAlumnos[i].nombre == acAlumno){
                                contiene = true
                            }
                        }
                    }while (!contiene)
                    introducirNotas()
                    println("Introduzca los nuevos valores: (nombre: ,clase: )")
                    val actAlumno = Alumno(nombre = readln(), notas = a, clase = readln())
                    for(i in 0 until listaAlumnos.size){
                        if(listaAlumnos[i].nombre == alumno.nombre){
                            listaAlumnos.removeAt(i)
                            listaAlumnos.add(actAlumno)
                        }
                        data.writeUTF(json.encodeToString(listaAlumnos[i]))
                    }
                }
                3 -> {
                    listaAlumnos.add(json.decodeFromString(content.readUTF()))
                    var delAlumno: String
                    var contiene = false
                    do {
                        println("Introduzca el nombre del alumno que desee eliminar")
                        delAlumno = readln()
                        alumno.nombre = delAlumno
                        for (i in 0 until listaAlumnos.size){
                            if (listaAlumnos[i].nombre == delAlumno){
                                contiene = true
                            }
                        }
                    }while (!contiene)
                    for(i in 0 until listaAlumnos.size){
                        if(listaAlumnos[i].nombre == delAlumno){
                            listaAlumnos.removeAt(i)
                        }
                        data.writeUTF(json.encodeToString(listaAlumnos[i]))
                    }
                }
            }
            if(listaAlumnos.size != 0){
                for(i in 0 until listaAlumnos.size){
                    println(listaAlumnos[i].nombre)
                }
            }
            data.flush()
        }

    }catch(e: Exception) {
        println("Servidor desconectado: ${e.message}")
    }
}

fun introducirNotas() {
    var listo = false
    while (!listo){
        a[readln().toInt()] = readln().toInt()
        println("desea añadir más notas?")
        val confirm = readln()
        if(!confirm.equals("Si", true)) listo = true
    }
}
