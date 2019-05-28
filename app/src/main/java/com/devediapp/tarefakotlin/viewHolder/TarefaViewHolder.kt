package com.devediapp.tarefakotlin.viewHolder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.devediapp.tarefakotlin.R
import com.devediapp.tarefakotlin.entity.TarefaEntity

class TarefaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val mDescricao : TextView = itemView.findViewById(R.id.textRowLista)

    fun bindDados(tarefa: TarefaEntity){
        mDescricao.text = tarefa.descricao
    }
}