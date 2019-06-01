package com.devediapp.tarefakotlin.contants

class DataBaseContants {

    object USER{
        val TABLE_NAME = "usuario"

        object COLUMNS{
            val ID = "idUsuario"
            val NOME = "nome"
            val EMAIL = "email"
            val SENHA = "senha"
        }
    }

    object PRIORIDADE{
        val TABLE_NAME = "prioridade"

        object COLUMNS{
            val ID = "idPrioridade"
            val DESCRICAO = "descricao"
        }
    }

    object TAREFA{
        val TABLE_NAME = "tarefa"

        object COLUMNS{
            val ID = "idTarefa"
            val FK_USER_ID = "fkIdUser"
            val FK_PRIORIDADE_ID = "fkIdPrioridade"
            val DESCRICAO = "descricao"
            val STATUS = "status"
            val DATA_VENCIMENTO = "dataVencimento"
            val IMAGEM = "fotoImagem"
        }
    }
}