package com.devediapp.tarefakotlin.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.devediapp.tarefakotlin.contants.DataBaseContants
import com.devediapp.tarefakotlin.entity.PrioridadeEntity
import com.devediapp.tarefakotlin.entity.TarefaEntity
import com.devediapp.tarefakotlin.entity.UserEntity
import com.devediapp.tarefakotlin.repository.PrioridadeRepository
import com.devediapp.tarefakotlin.util.UtilGenerico
import com.devediapp.tarefakotlin.util.ValidationException
import java.lang.Exception

class TarefaRepository private constructor(context: Context){

    private var mTaskDataBaseHelper : TaskDataBaseHelper = TaskDataBaseHelper(context)

    companion object{
        //Singleton - Para inibir a criação de varias instancias da classe, assim tirando o risco de concorrência ao banco
        fun getInstance(context: Context) : TarefaRepository {
            if(INSTANCE == null){
                INSTANCE = TarefaRepository(context)
            }

            return INSTANCE as TarefaRepository
        }

        private var INSTANCE : TarefaRepository? = null
    }

    fun getList(userId: Int) : MutableCollection<TarefaEntity> {
        val list = mutableListOf<TarefaEntity>()
        try {

            val cursor: Cursor
            val db = mTaskDataBaseHelper.readableDatabase

            cursor = db.rawQuery(
                "SELECT * FROM ${DataBaseContants.TAREFA.TABLE_NAME}" +
                        "WHERE ${DataBaseContants.TAREFA.COLUMNS.FK_USER_ID} = $userId" +
                        "ORDER BY ${DataBaseContants.TAREFA.COLUMNS.ID} ", null
            )

            if (cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val id = cursor.getInt(cursor.getColumnIndex(DataBaseContants.TAREFA.COLUMNS.ID))
                    val fkIdUser = cursor.getInt(cursor.getColumnIndex(DataBaseContants.TAREFA.COLUMNS.FK_USER_ID))
                    val fkIdPrioridade = cursor.getInt(cursor.getColumnIndex(DataBaseContants.TAREFA.COLUMNS.FK_PRIORIDADE_ID))
                    val descricao = cursor.getString(cursor.getColumnIndex(DataBaseContants.TAREFA.COLUMNS.DESCRICAO))
                    val dataVencimento = cursor.getString(cursor.getColumnIndex(DataBaseContants.TAREFA.COLUMNS.DATA_VENCIMENTO))
                    val status = UtilGenerico.getBooleanUmIsTrue(cursor.getInt(cursor.getColumnIndex(DataBaseContants.TAREFA.COLUMNS.STATUS)))

                    list.add(TarefaEntity(id, fkIdUser, fkIdPrioridade, descricao, dataVencimento, status))
                }
            }

            cursor.close()

        } catch (e: Exception) {
            return list
        }
        return list
    }

    fun get(id: Int) : TarefaEntity?{

        //Ponto de interrogação para dizer que na variavel é permitido nulo.
        var mTarefaEntity: TarefaEntity? = null
        try{
            val cursor: Cursor
            val db = mTaskDataBaseHelper.readableDatabase

            val projection = arrayOf(DataBaseContants.TAREFA.COLUMNS.FK_USER_ID,
                        DataBaseContants.TAREFA.COLUMNS.FK_PRIORIDADE_ID,
                        DataBaseContants.TAREFA.COLUMNS.DESCRICAO,
                        DataBaseContants.TAREFA.COLUMNS.DATA_VENCIMENTO,
                        DataBaseContants.TAREFA.COLUMNS.STATUS)

            val selection = "${DataBaseContants.TAREFA.COLUMNS.ID} = ?"
            val selectionArgs = arrayOf(id.toString())

            cursor = db.query(DataBaseContants.TAREFA.TABLE_NAME, projection, selection, selectionArgs, null, null, null)

            if(cursor.count > 0){
                cursor.moveToFirst()
                val fkIdUser = cursor.getInt(cursor.getColumnIndex(DataBaseContants.TAREFA.COLUMNS.FK_USER_ID))
                val fkIdPrioridade = cursor.getInt(cursor.getColumnIndex(DataBaseContants.TAREFA.COLUMNS.FK_PRIORIDADE_ID))
                val descricao = cursor.getString(cursor.getColumnIndex(DataBaseContants.TAREFA.COLUMNS.DESCRICAO))
                val dataVencimento = cursor.getString(cursor.getColumnIndex(DataBaseContants.TAREFA.COLUMNS.DATA_VENCIMENTO))
                val status = UtilGenerico.getBooleanUmIsTrue(cursor.getInt(cursor.getColumnIndex(DataBaseContants.TAREFA.COLUMNS.STATUS)))

                mTarefaEntity = TarefaEntity(id, fkIdUser, fkIdPrioridade, descricao, dataVencimento, status)
            }
            cursor.close()
        }catch (e: Exception){
            mTarefaEntity
        }

        return mTarefaEntity
    }

    fun insertTarefa(tarefa: TarefaEntity) : Int {
        // Inseri e retorna o identificador da linha inserida
        // Select - ReadableDatabase
        // update, insert, delete - WritableDatabase
        val db = mTaskDataBaseHelper.writableDatabase

        val insertValues = ContentValues()
        insertValues.put(DataBaseContants.TAREFA.COLUMNS.DESCRICAO, tarefa.descricao)
        insertValues.put(DataBaseContants.TAREFA.COLUMNS.FK_USER_ID, tarefa.fkIdUser)
        insertValues.put(DataBaseContants.TAREFA.COLUMNS.FK_PRIORIDADE_ID, tarefa.fkIdPrioridade)
        insertValues.put(DataBaseContants.TAREFA.COLUMNS.STATUS, UtilGenerico.getBooleanTrueIsUm(tarefa.status))
        insertValues.put(DataBaseContants.TAREFA.COLUMNS.DATA_VENCIMENTO,  tarefa.dataVencimento)
        try {
            //db.insert(DataBaseContants.TAREFA.TABLE_NAME, null, insertValues)
            return db.insert(DataBaseContants.TAREFA.TABLE_NAME, null, insertValues).toInt()
        }catch (e: ValidationException){
            throw ValidationException("Erro na inclusão dos dados. Error: ->" + e.cause + " /-/-/ " + e.message)
        }
    }

    fun updateTarefa(tarefa: TarefaEntity) {
        // Inseri e retorna o identificador da linha inserida
        // Select - ReadableDatabase
        // update, insert, delete - WritableDatabase
        val db = mTaskDataBaseHelper.writableDatabase

        val updateValues = ContentValues()
        updateValues.put(DataBaseContants.TAREFA.COLUMNS.DESCRICAO, tarefa.descricao)
        updateValues.put(DataBaseContants.TAREFA.COLUMNS.FK_USER_ID, tarefa.fkIdUser)
        updateValues.put(DataBaseContants.TAREFA.COLUMNS.FK_PRIORIDADE_ID, tarefa.fkIdPrioridade)
        updateValues.put(DataBaseContants.TAREFA.COLUMNS.STATUS, UtilGenerico.getBooleanTrueIsUm(tarefa.status))
        updateValues.put(DataBaseContants.TAREFA.COLUMNS.DATA_VENCIMENTO,  tarefa.dataVencimento)

        val selection = "${DataBaseContants.TAREFA.COLUMNS.ID} = ?"
        val selectionArgs = arrayOf(tarefa.id.toString())

        try{
            db.update(DataBaseContants.TAREFA.TABLE_NAME, updateValues, selection, selectionArgs)
        }catch (e: ValidationException){
            throw ValidationException("Erro na atualização dos dados. Error: ->" + e.cause + " /-/-/ " + e.message)
        }
    }

    fun deleteTarefa(id: Int) {
        // Inseri e retorna o identificador da linha inserida
        // Select - ReadableDatabase
        // update, insert, delete - WritableDatabase
        val db = mTaskDataBaseHelper.writableDatabase

        val selection = "${DataBaseContants.TAREFA.COLUMNS.ID} = ?"
        val selectionArgs = arrayOf(id.toString())

        try{
            db.delete(DataBaseContants.TAREFA.TABLE_NAME, selection, selectionArgs)
        }catch (e: ValidationException){
            throw ValidationException("Erro na exclusão dos dados. Error: ->" + e.cause + " /-/-/ " + e.message)
        }
    }
}