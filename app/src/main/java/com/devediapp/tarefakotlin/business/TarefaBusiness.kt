package com.devediapp.tarefakotlin.business

import android.content.Context
import com.devediapp.tarefakotlin.R
import com.devediapp.tarefakotlin.contants.TarefasConstants
import com.devediapp.tarefakotlin.entity.TarefaEntity
import com.devediapp.tarefakotlin.repository.TarefaRepository
import com.devediapp.tarefakotlin.util.SecurityPreferences
import com.devediapp.tarefakotlin.util.ValidationException
import kotlinx.android.synthetic.main.activity_formulario_tarefa_inclusao.*
import java.text.SimpleDateFormat
import java.util.*

class TarefaBusiness (private val context: Context) {

    private val mTarefaRepository: TarefaRepository = TarefaRepository.getInstance(context)
    private val mSecurityPreferences : SecurityPreferences = SecurityPreferences(context)
    private val mSimpleDateFormat : SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")

    fun getList(filtraTarefa: Int) : MutableList<TarefaEntity> {
        val fkIdUser = mSecurityPreferences.getRecuperarString(TarefasConstants.KEY.USER_ID).toInt()
        return mTarefaRepository.getList(fkIdUser, filtraTarefa)
    }

    fun get(mTarefaId: Int) = mTarefaRepository.get(mTarefaId)

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
                temErro = true
            }

            if(tarefaEntity.dataVencimento.contains("para")){
                tarefaEntity.dataVencimento = ""
                //val cal = Calendar.getInstance()
                //tarefaEntity.dataVencimento = mSimpleDateFormat.format(cal.time)
            }else{
                if(temErro){
                    msg.append(" / ")
                }
                val cal = Calendar.getInstance()
                val ano = cal.get(Calendar.YEAR)
                val mes = cal.get(Calendar.MONTH) + 1
                val dia = cal.get(Calendar.DAY_OF_MONTH)

                val dataSeparada = arrayListOf(tarefaEntity.dataVencimento.substring(0, 2).toInt(), tarefaEntity.dataVencimento.substring(3, 5).toInt(), tarefaEntity.dataVencimento.substring(6, 10).toInt())

                if(dataSeparada[2] < ano ||
                    (dataSeparada[2] == ano && dataSeparada[1] < mes) ||
                    (dataSeparada[2] == cal.get(Calendar.YEAR) && dataSeparada[1] == mes && dataSeparada[0] <= dia)){
                    msg.append("data de vencimento")
                    temErro = true
                }
            }

            if(tarefaEntity.fkIdPrioridade==0 || tarefaEntity.fkIdPrioridade.toString().isNullOrBlank()){
                if(temErro){
                    msg.append(" / não foi escolhida uma prioridade")
                }
                msg.append("status")
                temErro = true
            }
            if(tarefaEntity.fkIdUser==0 || tarefaEntity.fkIdUser.toString().isNullOrBlank()){
                if(temErro){
                    msg.append(" / status")
                }
                msg.append("status")
                temErro = true
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

    fun updateTarefa(tarefaEntity: TarefaEntity) {

        try {
            var temErro = false
            val msg : StringBuilder = java.lang.StringBuilder(context.getString(R.string.dados_invalidos).toString() + " Corrija o(s) seguinte(s) dado(s) ''")
            if(tarefaEntity.id == null || tarefaEntity.id <= 0){
                msg.append("Identificador da tarefa")
                temErro = true
            }

            if(tarefaEntity.descricao.isNullOrBlank() || tarefaEntity.descricao.isNullOrEmpty()){
                if(temErro){
                    msg.append(" / ")
                }
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

            mTarefaRepository.updateTarefa(tarefaEntity)
        }catch (mValidationException: ValidationException){
            throw mValidationException
        }catch (e: Exception){
            throw e
        }
    }

    fun deleteTarefa(tarefaId: Int) {

        try {
            var temErro = false
            val msg : StringBuilder = java.lang.StringBuilder(context.getString(R.string.dados_invalidos).toString() + " Corrija o seguinte dado ''")
            if(tarefaId == null || tarefaId <= 0){
                msg.append("Identificador da tarefa")
                temErro = true
            }

            if(temErro){
                msg.append("''.")
                throw ValidationException(msg.toString())
            }

            mTarefaRepository.deleteTarefa(tarefaId)
        }catch (mValidationException: ValidationException){
            throw mValidationException
        }catch (e: Exception){
            throw e
        }
    }

    fun atualizaStatus(mTarefaId: Int, situacaoAtualizada: Boolean){
        val tarefa = mTarefaRepository.get(mTarefaId)
        if(tarefa != null){
            tarefa.status = situacaoAtualizada
            mTarefaRepository.updateTarefa(tarefa)
        }
    }
}