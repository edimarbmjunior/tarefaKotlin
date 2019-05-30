package com.devediapp.tarefakotlin.util

import java.util.regex.Pattern

class UtilGenerico {

    companion object {
        fun getBooleanUmIsTrue(valor: Int): Boolean {
            var retorno = false
            if (valor == 1) {
                retorno = true
            }
            return retorno
        }

        fun getBooleanTrueIsUm(valor: Boolean): Int {
            var retorno = 0
            if (valor) {
                retorno = 1
            }
            return retorno
        }

        fun isEmailValid(email: String): Boolean {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }
}