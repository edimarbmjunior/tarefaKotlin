package com.devediapp.tarefakotlin.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.devediapp.tarefakotlin.contants.DataBaseContants
import com.devediapp.tarefakotlin.entity.User
import java.lang.Exception

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

    fun buscaEmail(email:String) : Boolean{

        var ret = false
        try{
            val cursor: Cursor
            val db = mTaskDataBaseHelper.readableDatabase

            val projection = arrayOf(DataBaseContants.USER.COLUMNS.ID)
            val selection = "${DataBaseContants.USER.COLUMNS.EMAIL} = ?"
            val selectionArgs = arrayOf(email)

            cursor = db.query(DataBaseContants.USER.TABLE_NAME, projection, selection, selectionArgs, null, null, null)
            //db.rawQuery("select * from user where email = ${email}", null)
            ret = cursor.count > 0
            cursor.close()
        }catch (e: Exception){
            throw e
        }
        return ret
    }
}