package com.devediapp.tarefakotlin.views

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import com.devediapp.tarefakotlin.R
import com.devediapp.tarefakotlin.business.PrioridadeBusiness
import com.devediapp.tarefakotlin.business.TarefaBusiness
import com.devediapp.tarefakotlin.contants.TarefasConstants
import com.devediapp.tarefakotlin.entity.PrioridadeEntity
import com.devediapp.tarefakotlin.entity.TarefaEntity
import com.devediapp.tarefakotlin.util.SecurityPreferences
import kotlinx.android.synthetic.main.activity_formulario_tarefa_inclusao.*
import java.text.SimpleDateFormat
import java.util.*

class FormularioTarefaInclusaoActivity : AppCompatActivity(), View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private lateinit var mPrioridadeBusiness : PrioridadeBusiness
    private lateinit var mTarefaBusiness : TarefaBusiness
    private lateinit var mSecurityPreferences : SecurityPreferences
    private val mSimpleDateFormat : SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")

    private var mListPrioridadeEntity = mutableListOf<PrioridadeEntity>()
    private var mListPrioridadeId = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_tarefa_inclusao)

        mPrioridadeBusiness = PrioridadeBusiness(this)
        mTarefaBusiness = TarefaBusiness(this)
        mSecurityPreferences = SecurityPreferences(this)

        carregaListaPrioridades()
        setListeners()
    }

    private fun setListeners(){
        buttonIncluirTarefaData.setOnClickListener(this)
        buttonIncluirTarefaSalvar.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.buttonIncluirTarefaData -> {
                abrirDatePickerDialog()
            }
            R.id.buttonIncluirTarefaSalvar -> {
                fazerInclusao()
            }
        }
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        val cal = Calendar.getInstance()
        cal.set(year, month, dayOfMonth)

        buttonIncluirTarefaData.text = mSimpleDateFormat.format(cal.time)
    }

    private fun carregaListaPrioridades(){
        mListPrioridadeEntity = mPrioridadeBusiness.getList()

        /*listPrioridades lst : MutableList<String> = mutableListOf()
        for(i in 0..listPrioridadeEntity.size){
            lst.add(listPrioridadeEntity[i].descricao)
        }*/
        //OU
        val listPrioridades = mListPrioridadeEntity.map { it.descricao }
        mListPrioridadeId = mListPrioridadeEntity.map { it.id }.toMutableList()

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listPrioridades)
        spinnerIncluirTarefaDescricao.adapter = adapter
    }

    private fun abrirDatePickerDialog(){

        val cal = Calendar.getInstance()
        val ano = cal.get(Calendar.YEAR)
        val mes = cal.get(Calendar.MONTH)
        val dia = cal.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, this, ano, mes, dia).show()
    }

    private fun fazerInclusao(){

        try {

            val descricao = editIncluirTarefaDescricao.text.toString()
            val prioridadeId =  mListPrioridadeId[spinnerIncluirTarefaDescricao.selectedItemPosition]
            val status = checkIncluirTarefaCompleto.isChecked
            val dataVencimento = buttonIncluirTarefaData.text.toString()
            val usuarioId = mSecurityPreferences.getRecuperarString(TarefasConstants.KEY.USER_ID).toInt()

            var tarefaEntity = TarefaEntity(0, usuarioId, prioridadeId, descricao, dataVencimento, status)

            tarefaEntity = mTarefaBusiness.insertTarefa(tarefaEntity)

            Toast.makeText(this, getString(R.string.cadastro_sucesso), Toast.LENGTH_LONG).show()
            finish()

        }catch (e : Exception){
            Toast.makeText(this, getString(R.string.erro_generico), Toast.LENGTH_LONG).show()
        }

    }
}
