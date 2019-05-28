package com.devediapp.tarefakotlin.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devediapp.tarefakotlin.R
import com.devediapp.tarefakotlin.adapter.ListaTarefaAdapter
import com.devediapp.tarefakotlin.business.TarefaBusiness
import com.devediapp.tarefakotlin.contants.TarefasConstants
import com.devediapp.tarefakotlin.entity.TarefaEntity
import com.devediapp.tarefakotlin.util.SecurityPreferences

class ListaTarefaFragment : Fragment(), View.OnClickListener {

    private lateinit var mContext : Context
    private lateinit var mRecyclerView : RecyclerView
    private lateinit var mTarefaBusiness : TarefaBusiness
    private lateinit var mSecurityPreferences : SecurityPreferences

    companion object {

        fun newInstance(): ListaTarefaFragment {
            /*Bundle args = new Bundle();
            args.putString(ARG_PARAM1, param1);
            args.putString(ARG_PARAM2, param2);
            fragment.setArguments(args);*/
            return ListaTarefaFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View? {
        val rootView = inflater.inflate(R.layout.fragment_lista_tarefa, container, false)

        rootView.findViewById<FloatingActionButton>(R.id.floatingNovaTarefa).setOnClickListener(this)
        mContext = rootView.context

        mTarefaBusiness = TarefaBusiness(mContext)
        mSecurityPreferences = SecurityPreferences(mContext)

        //São neessários três coisas para usar uma RecyclerView
        // 1 - Obter o elemento
        // 2 - Definir um adapter com os itens de listagem
        // 3 - Definir um layout

        //Primeiro passo do RecyclerView
        mRecyclerView = rootView.findViewById(R.id.recycleListaTarefa)

        val listaTarefas = mTarefaBusiness.getList(mSecurityPreferences.getRecuperarString(TarefasConstants.KEY.USER_ID).toInt())
        /*for(i in 0..50){
            if(listaTarefas.size==0){
                listaTarefas.add(TarefaEntity(0, 0, 1, "Sem descrição", "XX/XX/XXXX", false))
            }
            listaTarefas.add(listaTarefas[0].copy(descricao = listaTarefas[0].descricao + " - $i", id = i+2))
        }*/
        //Segundo passo
        mRecyclerView.adapter = ListaTarefaAdapter(listaTarefas)

        //Terceiro passo
        mRecyclerView.layoutManager = LinearLayoutManager(mContext)

        return rootView
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.floatingNovaTarefa -> {
                startActivity(Intent(mContext, FormularioTarefaInclusaoActivity::class.java))
            }
        }
    }
}
