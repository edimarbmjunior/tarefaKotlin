package com.devediapp.tarefakotlin.repository

import android.content.Context
import android.database.Cursor
import com.devediapp.tarefakotlin.contants.DataBaseContants
import com.devediapp.tarefakotlin.entity.PrioridadeEntity
import java.lang.Exception

class PrioridadeRepository private constructor(context: Context){

    private var mTaskDataBaseHelper : TaskDataBaseHelper = TaskDataBaseHelper(context)

    companion object{
        //Singleton - Para inibir a criação de varias instancias da classe, assim tirando o risco de concorrência ao banco
        fun getInstance(context: Context) : PrioridadeRepository {
            if(INSTANCE == null){
                INSTANCE = PrioridadeRepository(context)
            }

            return INSTANCE as PrioridadeRepository
        }

        private var INSTANCE : PrioridadeRepository? = null
    }

    fun getList() : MutableList<PrioridadeEntity>{

        val list = mutableListOf<PrioridadeEntity>()
        try{

            val cursor : Cursor
            val db = mTaskDataBaseHelper.readableDatabase

            cursor = db.rawQuery("SELECT * FROM ${DataBaseContants.PRIORIDADE.TABLE_NAME} ORDER BY ${DataBaseContants.PRIORIDADE.COLUMNS.ID} ", null)

            if(cursor.count > 0){
                while (cursor.moveToNext()){
                    val id = cursor.getInt(cursor.getColumnIndex(DataBaseContants.PRIORIDADE.COLUMNS.ID))
                    val descricao = cursor.getString(cursor.getColumnIndex(DataBaseContants.PRIORIDADE.COLUMNS.DESCRICAO))

                    list.add(PrioridadeEntity(id, descricao))
                }
            }

            cursor.close()

        }catch (e: Exception){
            return list
        }

        return list
    }
}