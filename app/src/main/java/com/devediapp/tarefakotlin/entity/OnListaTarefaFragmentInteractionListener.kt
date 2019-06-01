package com.devediapp.tarefakotlin.entity

interface OnListaTarefaFragmentInteractionListener {

    fun onListaClick(tarefaId: Int)

    fun onDeleteClick(tarefaId: Int)

    fun onStatusIncompletoClick(tarefaId: Int)

    fun onStatusCompletoClick(tarefaId: Int)

    fun OnPopUpImagem(tarefaId: Int)
}