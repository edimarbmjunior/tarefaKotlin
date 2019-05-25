package com.devediapp.tarefakotlin.util

import android.content.Context
import android.content.SharedPreferences

class SecurityPreferences(context: Context) {

    private val mSharedPreferences : SharedPreferences = context.getSharedPreferences("tarefas", Context.MODE_PRIVATE)

    fun storeString(key: String, value: String){
        mSharedPreferences.edit().putString(key, value).apply()
    }

    fun getStoredString(key: String) : String{
        return mSharedPreferences.getString(key, "")
    }

    fun removerStoredString(key: String){
        mSharedPreferences.edit().remove(key).apply()
    }
}