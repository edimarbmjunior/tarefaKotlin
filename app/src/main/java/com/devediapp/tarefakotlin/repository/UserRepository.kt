package com.devediapp.tarefakotlin.repository

import android.content.ContentValues
import android.content.Context
import com.devediapp.tarefakotlin.contants.DataBaseContants
import com.devediapp.tarefakotlin.entity.User

class UserRepository private constructor(context: Context){

    private var mTaskDataBaseHelper : TaskDataBaseHelper = TaskDataBaseHelper(context)

    companion object{
        //Singleton - Para inibir a criação de varias instancias da classe, assim tirando o risco de concorrência ao banco
        fun getInstance(context: Context) : UserRepository{
            if(INSTANCE == null){
                INSTANCE = UserRepository(context)
            }

            return INSTANCE as UserRepository
        }

        private var INSTANCE : UserRepository? = null
    }

    fun insertUser(user: User) : Int {
        // Inseri e retorna o identificador da linha inserida
        // Select - ReadableDatabase
        // update, insert, delete - WritableDatabase
        val db = mTaskDataBaseHelper.writableDatabase

        val insertValues = ContentValues()
        insertValues.put(DataBaseContants.USER.COLUMNS.NOME, user.nome)
        insertValues.put(DataBaseContants.USER.COLUMNS.EMAIL, user.email)
        insertValues.put(DataBaseContants.USER.COLUMNS.SENHA, user.senha)

        return db.insert(DataBaseContants.USER.TABLE_NAME, null, insertValues).toInt()
    }
}