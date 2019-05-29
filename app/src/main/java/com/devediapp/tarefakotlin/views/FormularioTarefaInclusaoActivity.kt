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
import com.devediapp.tarefakotlin.util.ValidationException
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
    private var mTarefasId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_tarefa_inclusao)

        mPrioridadeBusiness = PrioridadeBusiness(this)
        mTarefaBusiness = TarefaBusiness(this)
        mSecurityPreferences = SecurityPreferences(this)

        carregaListaPrioridades()
        //inicializarCampos()
        setListeners()
        carregarDadosDeActivity()
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
                fazerInclusaoAlteracao()
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

    private fun getIndexSpinner(fkIdPrioridade: Int): Int {
        var index = 0
        for(i in 0..mListPrioridadeEntity.size){
            if(mListPrioridadeEntity[i].id == fkIdPrioridade){
                index = i
                break
            }
        }
        return index
    }

    private fun abrirDatePickerDialog(){

        val cal = Calendar.getInstance()
        val ano = cal.get(Calendar.YEAR)
        val mes = cal.get(Calendar.MONTH)
        val dia = cal.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, this, ano, mes, dia).show()
    }

    private fun fazerInclusaoAlteracao(){

        try {

            val descricao = editIncluirTarefaDescricao.text.toString()
            val prioridadeId =  mListPrioridadeId[spinnerIncluirTarefaDescricao.selectedItemPosition]
            val status = checkIncluirTarefaCompleto.isChecked
            val dataVencimento = buttonIncluirTarefaData.text.toString()
            val usuarioId = mSecurityPreferences.getRecuperarString(TarefasConstants.KEY.USER_ID).toInt()

            var tarefaEntity = TarefaEntity(mTarefasId, usuarioId, prioridadeId, descricao, dataVencimento, status)

            val msg :StringBuilder = java.lang.StringBuilder(250)
            if(mTarefasId == 0){
                tarefaEntity = mTarefaBusiness.insertTarefa(tarefaEntity)
                msg.append(getString(R.string.cadastro_sucesso) + " Tarefa de número ${tarefaEntity.id} realizada!")
            }else{
                tarefaEntity.id = mTarefasId
                mTarefaBusiness.updateTarefa(tarefaEntity)
                msg.append(getString(R.string.atualizacao_sucesso) + " Tarefa de número ${tarefaEntity.id} realizada!")
            }


            Toast.makeText(this, msg.toString(), Toast.LENGTH_LONG).show()
            finish()

        }catch (mValidationException : ValidationException){
            Toast.makeText(this, mValidationException.message, Toast.LENGTH_LONG).show()
        }catch (e : Exception){
            Toast.makeText(this, getString(R.string.erro_generico), Toast.LENGTH_LONG).show()
        }
    }

    private fun inicializarCampos(){
        val cal = Calendar.getInstance()
        val ano = cal.get(Calendar.YEAR)
        val mes = cal.get(Calendar.MONTH)
        val dia = cal.get(Calendar.DAY_OF_MONTH)

        val mesAjustado = if(mes.toString().toInt() < 10) "0"+mes.toString() else mes.toString()

        val incializarData = dia.toString() + "/" + mesAjustado + "/" + ano.toString()

        buttonIncluirTarefaData.text = incializarData
    }

    private fun carregarDadosDeActivity(){
        val bundle = intent.extras
        if(bundle != null){
            mTarefasId = bundle.getInt(TarefasConstants.BUNDLE.TAREFAID)

            val tarefa = mTarefaBusiness.get(mTarefasId)

            if (tarefa != null) {
                editIncluirTarefaDescricao.setText(tarefa.descricao)
                buttonIncluirTarefaData.setText(tarefa.dataVencimento)
                checkIncluirTarefaCompleto.isChecked = tarefa.status
                spinnerIncluirTarefaDescricao.setSelection(getIndexSpinner(tarefa.fkIdPrioridade))
            }
        }
    }
}
