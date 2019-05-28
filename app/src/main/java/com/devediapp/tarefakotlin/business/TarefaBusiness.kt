package com.devediapp.tarefakotlin.business

import android.content.Context
import com.devediapp.tarefakotlin.R
import com.devediapp.tarefakotlin.contants.TarefasConstants
import com.devediapp.tarefakotlin.entity.TarefaEntity
import com.devediapp.tarefakotlin.repository.TarefaRepository
import com.devediapp.tarefakotlin.util.SecurityPreferences
import com.devediapp.tarefakotlin.util.ValidationException

class TarefaBusiness (private val context: Context) {

    private val mTarefaRepository: TarefaRepository = TarefaRepository.getInstance(context)
    private val mSecurityPreferences : SecurityPreferences = SecurityPreferences(context)

    fun getList() : MutableList<TarefaEntity> {
        val fkIdUser = mSecurityPreferences.getRecuperarString(TarefasConstants.KEY.USER_ID).toInt()
        return mTarefaRepository.getList(fkIdUser)
    }

    fun insertTarefa(tarefaEntity: TarefaEntity) : TarefaEntity {

        try {
            var temErro = false
            val msg : StringBuilder = java.lang.StringBuilder(context.getString(R.string.dados_invalidos).toString() + " Corrija o(s) seguinte(s) dado(s) ''")
            if(tarefaEntity.descricao.isNullOrBlank() || tarefaEntity.descricao.isNullOrEmpty()){
                msg.append("descrição")
                temErro = true
            }

            if(tarefaEntity.status && !tarefaEntity.status){
                if(temErro){
                    msg.append(" / ")
                }
                msg.append("status")
            }

            if(tarefaEntity.dataVencimento.toString().contains("Selecionar")){
                if(temErro){
                    msg.append(" / ")
                }
                msg.append("data de vencimento")
            }

            if(tarefaEntity.fkIdPrioridade==0 || tarefaEntity.fkIdPrioridade.toString().isNullOrBlank()){
                if(temErro){
                    msg.append(" / não foi escolhida uma prioridade")
                }
                msg.append("status")
            }
            if(tarefaEntity.fkIdUser==0 || tarefaEntity.fkIdUser.toString().isNullOrBlank()){
                if(temErro){
                    msg.append(" / status")
                }
                msg.append("status")
            }

            if(temErro){
                msg.append("''.")
                throw ValidationException(msg.toString())
            }

            tarefaEntity.id = mTarefaRepository.insertTarefa(tarefaEntity)
        }catch (mValidationException: ValidationException){
            throw mValidationException
        }catch (e: Exception){
            throw e
        }
        return tarefaEntity
    }
}