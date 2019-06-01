package com.devediapp.tarefakotlin.viewHolder

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.devediapp.tarefakotlin.R
import com.devediapp.tarefakotlin.entity.OnListaTarefaFragmentInteractionListener
import com.devediapp.tarefakotlin.entity.TarefaEntity
import com.devediapp.tarefakotlin.repository.PrioridadeCacheConstantes

class TarefaViewHolder(itemView: View, val mContext: Context,  val listenerFragment: OnListaTarefaFragmentInteractionListener) : RecyclerView.ViewHolder(itemView) {

    private val mDescricao : TextView = itemView.findViewById(R.id.textRowListaDescricao)
    private val mPrioridade : TextView = itemView.findViewById(R.id.textRowListaPrioridade)
    private val mImagemStatus : ImageView = itemView.findViewById(R.id.imageRowLista)
    private val mDataVencimento : TextView = itemView.findViewById(R.id.textRowListaDataVencimento)
    private val mFoto : TextView = itemView.findViewById(R.id.textRowListaImagem)

    fun bindDados(tarefa: TarefaEntity){
        mDescricao.text = tarefa.descricao
        mPrioridade.text = PrioridadeCacheConstantes.getPrioridadeDescricao(tarefa.fkIdPrioridade)
        mDataVencimento.text = tarefa.dataVencimento
        mFoto.text = "Foto da tarefa ${tarefa.id}"

        if(tarefa.status){
            mImagemStatus.setImageResource(R.drawable.ic_concluido)
        }else{
            mImagemStatus.setImageResource(R.drawable.ic_nao_concluido)
        }

        //Evento de click
        /*mDescricao.setOnClickListener(View.OnClickListener {
            listenerFragment.onListaClick(tarefa.id)
        })*/
        //OU
        mDescricao.setOnClickListener {
            listenerFragment.onListaClick(tarefa.id)
        }

        mDescricao.setOnLongClickListener {
            mostrarConfirmacaoDelecao(tarefa)
            //fixo
            true
        }

        mImagemStatus.setOnClickListener {
            if(tarefa.status){
                listenerFragment.onStatusIncompletoClick(tarefa.id)
            }else{
                listenerFragment.onStatusCompletoClick(tarefa.id)
            }
        }

        mFoto.setOnClickListener{
            listenerFragment.OnPopUpImagem(tarefa.id)
        }
    }

    private fun mostrarConfirmacaoDelecao(tarefaId: TarefaEntity){

        AlertDialog.Builder(mContext)
            .setTitle("Remoção de tarefa")
            .setMessage("Deseja remover a tarefa '${tarefaId.descricao}' ?")
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton("Remover", {DialogInterface, i -> listenerFragment.onDeleteClick(tarefaId.id)})
            .setNegativeButton("Cancelar", null).show()
    }
}