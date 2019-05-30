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
import android.widget.Toast
import com.devediapp.tarefakotlin.R
import com.devediapp.tarefakotlin.adapter.ListaTarefaAdapter
import com.devediapp.tarefakotlin.business.TarefaBusiness
import com.devediapp.tarefakotlin.contants.TarefasConstants
import com.devediapp.tarefakotlin.entity.OnListaTarefaFragmentInteractionListener
import com.devediapp.tarefakotlin.entity.TarefaEntity
import com.devediapp.tarefakotlin.util.SecurityPreferences

class ListaTarefaFragment : Fragment(), View.OnClickListener {

    private lateinit var mContext : Context
    private lateinit var mRecyclerView : RecyclerView
    private lateinit var mTarefaBusiness : TarefaBusiness
    private lateinit var mSecurityPreferences : SecurityPreferences
    private lateinit var mListenerListaFragment : OnListaTarefaFragmentInteractionListener
    private var mFiltraTarefa = 2

    companion object {

        fun newInstance(filtroTarefa: Int?): ListaTarefaFragment {
            val args: Bundle = Bundle()
            args.putInt(TarefasConstants.FILTRO_TAREFAS.KEY, filtroTarefa!!)

            val fragment = ListaTarefaFragment()
            fragment.arguments = args

            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mFiltraTarefa = arguments!!.getInt(TarefasConstants.FILTRO_TAREFAS.KEY, 0)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View? {
        val rootView = inflater.inflate(R.layout.fragment_lista_tarefa, container, false)

        rootView.findViewById<FloatingActionButton>(R.id.floatingNovaTarefa).setOnClickListener(this)
        mContext = rootView.context

        mTarefaBusiness = TarefaBusiness(mContext)
        mSecurityPreferences = SecurityPreferences(mContext)

        //Por ser uma interface, quem instanciar ela deverá codificar seus metodos para que exista uma ação no metodo
        mListenerListaFragment = object : OnListaTarefaFragmentInteractionListener{

            override fun onListaClick(tarefaId: Int) {
                val bundle = Bundle()
                bundle.putInt(TarefasConstants.BUNDLE.TAREFAID, tarefaId)
                val intent = Intent(mContext, FormularioTarefaInclusaoActivity::class.java)
                //Colocando parametros na chamada da Activity
                intent.putExtras(bundle)

                startActivity(intent)
            }

            override fun onDeleteClick(tarefaId: Int) {
                mTarefaBusiness.deleteTarefa(tarefaId)
                Toast.makeText(mContext, getString(R.string.tarefa_removida_sucesso), Toast.LENGTH_LONG).show()
                carregaTarefas()
            }

            override fun onStatusIncompletoClick(tarefaId: Int) {
                mTarefaBusiness.atualizaStatus(tarefaId, false)
                carregaTarefas()
            }

            override fun onStatusCompletoClick(tarefaId: Int) {
                mTarefaBusiness.atualizaStatus(tarefaId, true)
                carregaTarefas()
            }

        }

        //São neessários três coisas para usar uma RecyclerView
        // 1 - Obter o elemento
        // 2 - Definir um adapter com os itens de listagem
        // 3 - Definir um layout

        //Primeiro passo do RecyclerView
        mRecyclerView = rootView.findViewById(R.id.recycleListaTarefa)

        //val listaTarefas = mTarefaBusiness.getList(mSecurityPreferences.getRecuperarString(TarefasConstants.KEY.USER_ID).toInt())
        //OU
        //val fkIdUser = mSecurityPreferences.getRecuperarString(TarefasConstants.KEY.USER_ID).toInt()
        //val listaTarefas = mTarefaBusiness.getList(fkIdUser)

        //Mock abaixo foi comentado
        /*for(i in 0..50){
            if(listaTarefas.size==0){
                listaTarefas.add(TarefaEntity(0, 0, 1, "Sem descrição", "XX/XX/XXXX", false))
            }
            listaTarefas.add(listaTarefas[0].copy(descricao = listaTarefas[0].descricao + " - $i", id = i+2))
        }*/

        //Segundo passo
        //mRecyclerView.adapter = ListaTarefaAdapter(listaTarefas)
        mRecyclerView.adapter = ListaTarefaAdapter(mutableListOf(), mListenerListaFragment)

        //Terceiro passo
        mRecyclerView.layoutManager = LinearLayoutManager(mContext)

        return rootView
    }

    override fun onResume() {
        super.onResume()
        carregaTarefas()
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.floatingNovaTarefa -> {
                startActivity(Intent(mContext, FormularioTarefaInclusaoActivity::class.java))
            }
        }
    }

    private fun carregaTarefas(){
        //val listaTarefas = mTarefaBusiness.getList()
        mRecyclerView.adapter = ListaTarefaAdapter(mTarefaBusiness.getList(mFiltraTarefa), mListenerListaFragment)
    }
}
