package com.devediapp.tarefakotlin.repository

import com.devediapp.tarefakotlin.entity.PrioridadeEntity

class PrioridadeCacheConstantes private constructor(){

    companion object{
        fun setCahe(lista: List<PrioridadeEntity>){
            for (item in lista){
                mPrioridadeCache.put(item.id, item.descricao)
            }
        }

        fun getPrioridadeDescricao(id: Int) : String{
            if(mPrioridadeCache[id] == null){
                return ""
            }

            return mPrioridadeCache[id].toString()
        }

        private val mPrioridadeCache = hashMapOf<Int, String>()
    }
}