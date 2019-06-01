package com.devediapp.tarefakotlin.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Patterns
import android.widget.Toast
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
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
            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun mostrarMensagemToastLong(context: Context, str: String){
            Toast.makeText(context, str, Toast.LENGTH_LONG).show()
        }

        fun mostrarMensagemToastShort(context: Context, str: String){
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
        }

        fun convertToBase64(filePath: String):String {
            val bm = BitmapFactory.decodeFile(filePath)
            val baos = ByteArrayOutputStream()
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val byteArrayImage = baos.toByteArray()
            val encodeImage:String

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                encodeImage = Base64.getEncoder().encodeToString(byteArrayImage)
            }else{
                encodeImage = android.util.Base64.encodeToString(byteArrayImage, android.util.Base64.DEFAULT)
            }
            return encodeImage
        }

        fun decodeFromBase64ToBitmap(encodedImage: String) : Bitmap{
            val decodedString : ByteArray
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                decodedString = Base64.getDecoder().decode(encodedImage)
            }else{
                decodedString = android.util.Base64.decode(encodedImage, android.util.Base64.DEFAULT)
            }

            val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            return decodedByte
        }
    }
}