package com.devediapp.tarefakotlin.repository

import android.content.Context

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
}