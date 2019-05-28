package com.devediapp.tarefakotlin.business

import android.content.Context
import com.devediapp.tarefakotlin.entity.PrioridadeEntity
import com.devediapp.tarefakotlin.repository.PrioridadeRepository

class PrioridadeBusiness (context: Context){

    private val mPrioridadeRepository : PrioridadeRepository = PrioridadeRepository.getInstance(context)

    fun getList() : MutableList<PrioridadeEntity> = mPrioridadeRepository.getList()
}