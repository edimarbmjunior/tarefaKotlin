package com.devediapp.tarefakotlin.business

import android.content.Context
import com.devediapp.tarefakotlin.R
import com.devediapp.tarefakotlin.contants.TarefasConstants
import com.devediapp.tarefakotlin.entity.TarefaEntity
import com.devediapp.tarefakotlin.repository.TarefaRepository
import com.devediapp.tarefakotlin.util.ValidationException

class TarefaBusiness (private val context: Context) {

    private val mTarefaRepository: TarefaRepository = TarefaRepository.getInstance(context)

    fun getList(userId: Int) : MutableList<TarefaEntity> = mTarefaRepository.getList(userId)

    fun insertTarefa(tarefaEntity: TarefaEntity) : TarefaEntity {

        try {
            if(tarefaEntity.descricao.isNullOrBlank()
                || (tarefaEntity.status && !tarefaEntity.status)
                || tarefaEntity.dataVencimento.isNullOrBlank()
                || tarefaEntity.fkIdPrioridade==0
                || tarefaEntity.fkIdUser==0){
                throw ValidationException(context.getString(R.string.dados_invalidos))
            }

            tarefaEntity.id = mTarefaRepository.insertTarefa(tarefaEntity)
        }catch (e: Exception){
            throw e
        }
        return tarefaEntity
    }
}