package com.example.miagendacsvext

import android.content.Context
import android.os.Environment
import java.io.*

class Agenda(private val context: Context) {

    var contactos = mutableListOf<Contacto>()
    private val nombreArchivo = "contactos.csv"

    init {
        cargarContactos()
    }


    fun agregarContacto(contacto: Contacto) {
        contactos.add(contacto)
        guardarContactos()
    }

    fun borrarContacto(nombre: String): Boolean {
        val eliminado = contactos.removeIf { it.nombre.equals(nombre, ignoreCase = true) }
        if (eliminado) guardarContactos()
        return eliminado
    }

    fun editarContacto(nombreAntiguo: String, nuevoContacto: Contacto): Boolean {
        val index = contactos.indexOfFirst { it.nombre.equals(nombreAntiguo, ignoreCase = true) }
        if (index >= 0) {
            contactos[index] = nuevoContacto
            guardarContactos()
            return true
        }
        return false
    }
    private fun obtenerArchivo(): File {
        val carpetaDescargas = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        return File(carpetaDescargas, nombreArchivo)
    }
    fun guardarContactos() {
        try {
            val archivo = obtenerArchivo()
            val writer = BufferedWriter(FileWriter(archivo))
            for (contacto in contactos) {
                writer.write("${contacto.nombre},${contacto.telefono}")
                writer.newLine()
            }
            writer.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun cargarContactos() {
        contactos.clear()
        try {
            val archivo = obtenerArchivo()
            if (!archivo.exists()) return

            val reader = BufferedReader(FileReader(archivo))
            var linea: String?
            while (reader.readLine().also { linea = it } != null) {
                val partes = linea!!.split(",")
                if (partes.size == 2) {
                    val nombre = partes[0]
                    val telefono = partes[1].toIntOrNull()
                    if (telefono != null) {
                        contactos.add(Contacto(nombre, telefono))
                    }
                }
            }
            reader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}


