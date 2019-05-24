package com.devediapp.tarefakotlin.views

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.devediapp.tarefakotlin.R
import com.devediapp.tarefakotlin.repository.UserRepository
import kotlinx.android.synthetic.main.activity_cadastro_usuario.*

class CadastroUsuarioActivity : AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_usuario)

        setLiners()
        UserRepository.getInstance(this)
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

    }
}
