package com.devediapp.tarefakotlin.entity

data class UserEntity constructor(var idUser: Int, var nome: String, var email: String, var senha: String = "")