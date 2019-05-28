package com.devediapp.tarefakotlin.viewHolder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.devediapp.tarefakotlin.R
import com.devediapp.tarefakotlin.entity.TarefaEntity

class TarefaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val mDescricao : TextView = itemView.findViewById(R.id.textRowListaDescricao)
    private val mPrioridade : TextView = itemView.findViewById(R.id.textRowListaPrioridade)
    private val mImagemStatus : ImageView = itemView.findViewById(R.id.imageRowLista)
    private val mDataVencimento : TextView = itemView.findViewById(R.id.textRowListaDataVencimento)

    fun bindDados(tarefa: TarefaEntity){
        mDescricao.text = tarefa.descricao
        mPrioridade.text = ""
        mDataVencimento.text = tarefa.dataVencimento

        if(tarefa.status){
            mImagemStatus.setImageResource(R.drawable.ic_concluido)
        }else{
            mImagemStatus.setImageResource(R.drawable.ic_nao_concluido)
        }
    }
}