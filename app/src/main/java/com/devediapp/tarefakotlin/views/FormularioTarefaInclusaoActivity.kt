package com.devediapp.tarefakotlin.views

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.devediapp.tarefakotlin.R
import com.devediapp.tarefakotlin.business.PrioridadeBusiness
import kotlinx.android.synthetic.main.activity_formulario_tarefa_inclusao.*

class FormularioTarefaInclusaoActivity : AppCompatActivity() {

    private lateinit var mPrioridadeBusiness : PrioridadeBusiness

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_tarefa_inclusao)

        mPrioridadeBusiness = PrioridadeBusiness(this)

        carregaListaPrioridades()
    }

    private fun carregaListaPrioridades(){
        val listPrioridadeEntity = mPrioridadeBusiness.getList()

        /*listPrioridades lst : MutableList<String> = mutableListOf()
        for(i in 0..listPrioridadeEntity.size){
            lst.add(listPrioridadeEntity[i].descricao)
        }*/
        //OU
        val listPrioridades = listPrioridadeEntity.map { it.descricao }

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listPrioridades)
        spinnerIncluirTarefaDescricao.adapter = adapter
    }
}
