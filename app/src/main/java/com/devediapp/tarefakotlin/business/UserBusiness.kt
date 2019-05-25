package com.devediapp.tarefakotlin.business

import android.content.Context
import com.devediapp.tarefakotlin.R
import com.devediapp.tarefakotlin.contants.TarefasConstants
import com.devediapp.tarefakotlin.entity.User
import com.devediapp.tarefakotlin.repository.UserRepository
import com.devediapp.tarefakotlin.util.SecurityPreferences
import com.devediapp.tarefakotlin.util.ValidationException
import java.lang.Exception

class UserBusiness (private val context: Context){

    private val mUserRepository : UserRepository = UserRepository.getInstance(context)
    private val mSecurityPreferences: SecurityPreferences = SecurityPreferences(context)

    fun insertUser(user: User) : Int{

        try {
            if(user.nome.isNullOrBlank() || user.email.isNullOrBlank() || user.senha.isNullOrBlank()){
                throw ValidationException(context.getString(R.string.informe_dados_corretamente))
            }

            if(mUserRepository.buscaEmail(user.email)){
                throw ValidationException(context.getString(R.string.email_existe))
            }

            user.idUser = mUserRepository.insertUser(user)

            //Salvar dados do usuario na shared
            mSecurityPreferences.storeString(TarefasConstants.KEY.USER_ID, user.idUser.toString())
            mSecurityPreferences.storeString(TarefasConstants.KEY.USER_EMAIL, user.email)
            mSecurityPreferences.storeString(TarefasConstants.KEY.USER_NOME, user.nome)
        }catch (e: Exception){
            throw e
        }
        return user.idUser
    }
}