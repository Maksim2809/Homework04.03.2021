package ru.smak.io

import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import java.lang.Exception

object Loader {
    fun load(filename: String): MutableList<MutableList<Double>>{
        val res = mutableListOf<MutableList<Double>>()
        try{
            val br = BufferedReader(
                InputStreamReader(
                    FileInputStream(filename), "UTF-8"
                )
            )
            br.readLines().forEach{
                res.add(it.split(';').map { it.toDouble() }.toMutableList())
            }
        } catch (e: Exception){
            println("Ошибка при чтении из файла: ${e.message}")
        }
        return res
    }
}