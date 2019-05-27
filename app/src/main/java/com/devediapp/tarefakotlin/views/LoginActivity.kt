package com.devediapp.tarefakotlin.views

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.devediapp.tarefakotlin.R
import com.devediapp.tarefakotlin.business.UserBusiness
import com.devediapp.tarefakotlin.contants.TarefasConstants
import com.devediapp.tarefakotlin.util.SecurityPreferences
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mUserBusiness: UserBusiness
    private lateinit var mSecurityPreferences: SecurityPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mUserBusiness = UserBusiness(this)
        mSecurityPreferences = SecurityPreferences(this)

        setListeners()
        validaUsuarioLogado()
    }

    private fun setListeners(){
        buttonLoginUsuario.setOnClickListener(this)
        textViewLoginCadastrarUsuario.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.buttonLoginUsuario ->{
                handleLogin()
            }
            R.id.textViewLoginCadastrarUsuario -> {
                chamarTelaCadastro()
            }
        }
    }

    private fun handleLogin(){
        val email = editLoginEmail.text.toString()
        val senha = editLoginSenha.text.toString()

        if(mUserBusiness.login(email, senha)){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }else{
            Toast.makeText(this, getString(R.string.usuario_senha_incorreto), Toast.LENGTH_LONG).show()
        }
    }

    private fun chamarTelaCadastro(){
        startActivity(Intent(this, CadastroUsuarioActivity::class.java))
    }

    private fun validaUsuarioLogado(){

        val idUser = mSecurityPreferences.getRecuperarStringString(TarefasConstants.KEY.USER_ID)
        val senha = mSecurityPreferences.getRecuperarStringString(TarefasConstants.KEY.USER_NOME)
        if(!idUser.isNullOrEmpty() && !senha.isNullOrEmpty()){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }
}
