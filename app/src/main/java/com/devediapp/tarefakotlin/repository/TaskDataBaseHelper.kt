package com.devediapp.tarefakotlin.repository

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.devediapp.tarefakotlin.contants.DataBaseContants

class TaskDataBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    //SQLite
    //Integer, Real, Text, Blob

    companion object{//Dessa forma coloco as variaveis estaticas
        private val DATABASE_VERSION: Int = 1
        private val DATABASE_NAME: String = "tarefas.db"
    }

    private val createTableUser = """
       CREATE TABLE ${DataBaseContants.USER.TABLE_NAME} (
            ${DataBaseContants.USER.COLUMNS.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${DataBaseContants.USER.COLUMNS.NOME} TEXT,
            ${DataBaseContants.USER.COLUMNS.EMAIL} TEXT,
            ${DataBaseContants.USER.COLUMNS.SENHA} TEXT
       );
    """

    private val deleteTableUser = """drop table if exists ${DataBaseContants.USER.TABLE_NAME};"""

    override fun onCreate(sqlLite: SQLiteDatabase) {
        //Roda essa função na instalção do aplicativo
        sqlLite.execSQL(createTableUser)
    }

    override fun onUpgrade(sqlLite: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //Aqui quando tem uma atualização no aplicativo roda essa função
        /*sqlLite.execSQL(deleteTableUser)
        sqlLite.execSQL(createTableUser)*/

        when(oldVersion){
            1 -> {
                println("Rodar atualização da 1 para 2")
                println("Rodar atualização da 2 para 3")
                println("Rodar atualização da 3 para 4")
            }
            2 -> {
                println("Rodar atualização da 2 para 3")
                println("Rodar atualização da 3 para 4")
            }
            3 -> {
                println("Rodar atualização da 3 para 4")
            }
        }
    }
}