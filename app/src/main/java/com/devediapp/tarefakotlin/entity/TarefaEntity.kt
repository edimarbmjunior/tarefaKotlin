package com.devediapp.tarefakotlin.entity

data class TarefaEntity(var id: Int,
                        val fkIdUser: Int,
                        val fkIdPrioridade: Int,
                        var descricao: String,
                        var dataVencimento: String,
                        var status: Boolean)
