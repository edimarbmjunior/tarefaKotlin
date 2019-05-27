package com.devediapp.tarefakotlin.views

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.devediapp.tarefakotlin.R
import com.devediapp.tarefakotlin.business.UserBusiness
import com.devediapp.tarefakotlin.entity.UserEntity
import com.devediapp.tarefakotlin.util.ValidationException
import kotlinx.android.synthetic.main.activity_cadastro_usuario.*
import java.lang.Exception

class CadastroUsuarioActivity : AppCompatActivity(), View.OnClickListener {

    //Não pode ser instanciado antes da execução do "onCreate()" da activity
    private lateinit var mUserBusiness : UserBusiness

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_usuario)

        setLiners()
        mUserBusiness = UserBusiness(application)
    }

    private fun setLiners(){
        buttonSaveCadastroUsuario.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.buttonSaveCadastroUsuario -> {
                handleSave()
            }
        }
    }

    private fun handleSave(){
        try {
            val userEntity : UserEntity = UserEntity(0,
                editNomeCadastroUsuario.text.toString(),
                editEmailCadastroUsuario.text.toString(),
                editPasswordCadastroUsuario.text.toString())

            userEntity.idUser = mUserBusiness.insertUser(userEntity)
            Toast.makeText(applicationContext, getString(R.string.cadastro_sucesso), Toast.LENGTH_LONG).show()

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }catch (e: ValidationException){
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
        }catch (e: Exception){
            Toast.makeText(applicationContext, getString(R.string.erro_generico), Toast.LENGTH_LONG).show()
        }
    }
}
