package com.devediapp.tarefakotlin.business

import android.content.Context
import com.devediapp.tarefakotlin.R
import com.devediapp.tarefakotlin.contants.TarefasConstants
import com.devediapp.tarefakotlin.entity.UserEntity
import com.devediapp.tarefakotlin.repository.UserRepository
import com.devediapp.tarefakotlin.util.SecurityPreferences
import com.devediapp.tarefakotlin.util.UtilGenerico
import com.devediapp.tarefakotlin.util.ValidationException
import java.lang.Exception

class UserBusiness (private val context: Context){

    private val mUserRepository : UserRepository = UserRepository.getInstance(context)
    private val mSecurityPreferences: SecurityPreferences = SecurityPreferences(context)

    fun insertUser(userEntity: UserEntity) : Int{

        try {
            if(userEntity.nome.isNullOrBlank() || userEntity.email.isNullOrBlank() || userEntity.senha.isNullOrBlank()){
                throw ValidationException(context.getString(R.string.informe_dados_corretamente))
            }

            if(userEntity.email.isEmpty() || !UtilGenerico.isEmailValid(userEntity.email)){
                throw ValidationException(context.getString(R.string.email_nao_valido))
            }

            if(mUserRepository.buscaEmail(userEntity.email)){
                throw ValidationException(context.getString(R.string.email_existe))
            }

            if(userEntity.senha.count() < 5){
                throw ValidationException(context.getString(R.string.senha_mais_5_caracteres))
            }

            userEntity.idUser = mUserRepository.insertUser(userEntity)

            //Salvar dados do usuario na shared
            mSecurityPreferences.salvarString(TarefasConstants.KEY.USER_ID, userEntity.idUser.toString())
            mSecurityPreferences.salvarString(TarefasConstants.KEY.USER_EMAIL, userEntity.email)
            mSecurityPreferences.salvarString(TarefasConstants.KEY.USER_NOME, userEntity.nome)
        }catch (e: Exception){
            throw e
        }
        return userEntity.idUser
    }

    fun login(email: String, senha: String):Boolean{

        //Ponto de interrogação para dizer que na variavel é permitido nulo.
        val mUserEntity : UserEntity? = mUserRepository.get(email, senha)
        return if(mUserEntity != null){
            //Salvar dados do usuario na shared
            mSecurityPreferences.salvarString(TarefasConstants.KEY.USER_ID, mUserEntity.idUser.toString())
            mSecurityPreferences.salvarString(TarefasConstants.KEY.USER_EMAIL, mUserEntity.email)
            mSecurityPreferences.salvarString(TarefasConstants.KEY.USER_NOME, mUserEntity.nome)
            //Retorna true
            true
        }else{
            //Retorna false
            false
        }

    }
}