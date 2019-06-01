package com.devediapp.tarefakotlin.views

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.SpinnerAdapter
import android.widget.Toast
import com.devediapp.tarefakotlin.R
import com.devediapp.tarefakotlin.business.PrioridadeBusiness
import com.devediapp.tarefakotlin.business.TarefaBusiness
import com.devediapp.tarefakotlin.contants.TarefasConstants
import com.devediapp.tarefakotlin.entity.PrioridadeEntity
import com.devediapp.tarefakotlin.entity.TarefaEntity
import com.devediapp.tarefakotlin.util.SecurityPreferences
import com.devediapp.tarefakotlin.util.UtilGenerico
import com.devediapp.tarefakotlin.util.ValidationException
import kotlinx.android.synthetic.main.activity_formulario_tarefa_inclusao.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.nio.file.Files
import java.text.SimpleDateFormat
import java.util.*
import java.util.stream.Stream


class FormularioTarefaInclusaoActivity : AppCompatActivity(), View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private lateinit var mPrioridadeBusiness : PrioridadeBusiness
    private lateinit var mTarefaBusiness : TarefaBusiness
    private lateinit var mSecurityPreferences : SecurityPreferences
    private val mSimpleDateFormat : SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))

    private var mListPrioridadeEntity = mutableListOf<PrioridadeEntity>()
    private var mListPrioridadeId = mutableListOf<Int>()
    private var mTarefasId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_tarefa_inclusao)

        mPrioridadeBusiness = PrioridadeBusiness(this)
        mTarefaBusiness = TarefaBusiness(this)
        mSecurityPreferences = SecurityPreferences(this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)//Mostrar botão voltar
        supportActionBar?.setHomeButtonEnabled(true) // Ativar o botão


        carregaListaPrioridades()
        //inicializarCampos()
        setListeners()
        carregarDadosDeActivity()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    return
                }else{
                    val permissionsStrings = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    ActivityCompat.requestPermissions(this, permissionsStrings, PackageManager.PERMISSION_GRANTED);
                }
            }
        } else {
            val mPackageManager : PackageManager = getPackageManager()
            val hasPermStorage = mPackageManager.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, getPackageName())


            if (hasPermStorage != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Sem permissão de acesso as fotos", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, "Obrigado pela permissão!", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {//Botão adicional na ToolBar
        super.onOptionsItemSelected(item)
        when(item?.itemId){
            android.R.id.home ->{//Id do botão voltar gerado automaticamente
                //startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
        return true
    }

    private fun setListeners(){
        buttonIncluirTarefaData.setOnClickListener(this)
        buttonIncluirTarefaSalvar.setOnClickListener(this)
        imageButtonIncluirTarefaButtonData.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.buttonIncluirTarefaData -> {
                abrirDatePickerDialog()
            }
            R.id.buttonIncluirTarefaSalvar -> {
                fazerInclusaoAlteracao()
            }
            R.id.imageButtonIncluirTarefaButtonData ->{
                abrirPastaFoto()
            }
        }
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        val cal = Calendar.getInstance()
        cal.timeZone = TimeZone.getTimeZone("GMT-03:00")
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
        spinnerIncluirTarefaDescricao.adapter = adapter as SpinnerAdapter?
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
        cal.timeZone = TimeZone.getTimeZone("GMT-03:00")
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
            val usuarioImagem = textViewIncluirTarefaValoresImagem.text

            var tarefaEntity = TarefaEntity(mTarefasId, usuarioId, prioridadeId, descricao, dataVencimento, status, usuarioImagem.toString())

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

    private fun abrirPastaFoto(){
        //val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), TarefasConstants.TAREFA.GALERIA_IMAGES)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == TarefasConstants.TAREFA.GALERIA_IMAGES){
            val selectedImage = data?.data
            val filePath = arrayOf(MediaStore.Images.Media.DATA)
            val curso : Cursor = contentResolver.query(selectedImage, filePath, null, null, null)
            curso.moveToFirst()

            val columIndex = curso.getColumnIndex(filePath[0])
            val picturePath = curso.getString(columIndex)
            curso.close()

            val imag64 = UtilGenerico.convertToBase64(picturePath)
            textViewIncluirTarefaValoresImagem.text = imag64
        }
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
                buttonIncluirTarefaSalvar.text = getString(R.string.atualizar_tarefa)
                textViewIncluirTarefaValoresImagem.text = tarefa.imagem
            }
        }
    }
}
