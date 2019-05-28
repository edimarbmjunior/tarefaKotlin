package com.devediapp.tarefakotlin.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devediapp.tarefakotlin.R

class ListaTarefaFragment : Fragment(), View.OnClickListener {

    private lateinit var mContext : Context

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
