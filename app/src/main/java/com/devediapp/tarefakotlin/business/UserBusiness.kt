package com.devediapp.tarefakotlin.business

import android.content.Context
import com.devediapp.tarefakotlin.R
import com.devediapp.tarefakotlin.contants.TarefasConstants
import com.devediapp.tarefakotlin.entity.UserEntity
import com.devediapp.tarefakotlin.repository.UserRepository
import com.devediapp.tarefakotlin.util.SecurityPreferences
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

            if(mUserRepository.buscaEmail(userEntity.email)){
                throw ValidationException(context.getString(R.string.email_existe))
            }

            userEntity.idUser = mUserRepository.insertUser(userEntity)

            //Salvar dados do usuario na shared
            mSecurityPreferences.storeString(TarefasConstants.KEY.USER_ID, userEntity.idUser.toString())
            mSecurityPreferences.storeString(TarefasConstants.KEY.USER_EMAIL, userEntity.email)
            mSecurityPreferences.storeString(TarefasConstants.KEY.USER_NOME, userEntity.nome)
        }catch (e: Exception){
            throw e
        }
        return userEntity.idUser
    }

    fun login(email: String, senha: String){

        //Ponto de interrogação para dizer que na variavel é permitido nulo.
        val mUserEntity : UserEntity? = mUserRepository.get(email, senha)
        if(mUserEntity != null){
            //Salvar dados do usuario na shared
            mSecurityPreferences.storeString(TarefasConstants.KEY.USER_ID, mUserEntity.idUser.toString())
            mSecurityPreferences.storeString(TarefasConstants.KEY.USER_EMAIL, mUserEntity.email)
            mSecurityPreferences.storeString(TarefasConstants.KEY.USER_NOME, mUserEntity.nome)
        }else{
            throw ValidationException(context.getString(R.string.usuario_invalido))
        }

    }
}