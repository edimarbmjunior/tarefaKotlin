package com.devediapp.tarefakotlin.business

import android.content.Context
import com.devediapp.tarefakotlin.R
import com.devediapp.tarefakotlin.entity.User
import com.devediapp.tarefakotlin.repository.UserRepository
import com.devediapp.tarefakotlin.util.ValidationException
import java.lang.Exception

class UserBusiness (private val context: Context){

    private val mUserRepository : UserRepository = UserRepository.getInstance(context)

    fun insertUser(user: User) : Int{

        try {
            if(user.nome.isNullOrBlank() || user.email.isNullOrBlank() || user.senha.isNullOrBlank()){
                throw ValidationException(context.getString(R.string.informe_dados_corretamente))
            }

            user.idUser = mUserRepository.insertUser(user)
        }catch (e: Exception){
            throw e
        }
        return user.idUser
    }
}