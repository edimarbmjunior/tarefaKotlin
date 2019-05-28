package com.devediapp.tarefakotlin.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.devediapp.tarefakotlin.R
import com.devediapp.tarefakotlin.entity.TarefaEntity
import com.devediapp.tarefakotlin.viewHolder.TarefaViewHolder

class ListaTarefaAdapter (val listaTarefas: List<TarefaEntity>) : RecyclerView.Adapter<TarefaViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TarefaViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.row_tarefa_lista, parent, false)

        return TarefaViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listaTarefas.count()
    }

    override fun onBindViewHolder(holder: TarefaViewHolder, position: Int) {
        val tarefa = listaTarefas[position]
        holder.bindDados(tarefa)
    }
}