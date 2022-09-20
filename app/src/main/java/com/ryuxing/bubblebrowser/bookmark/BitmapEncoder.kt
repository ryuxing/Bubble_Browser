package com.ryuxing.bubblebrowser.bookmark

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

class BitmapEncoder {
    @TypeConverter
    fun Bitmap.toEncodedString():String{
        val bos = ByteArrayOutputStream().also {
            if (!compress(Bitmap.CompressFormat.PNG, 50, it)) return ""
        }
        //Log.d("Encode",Base64.encodeToString(bos.toByteArray(),Base64.DEFAULT))
        return Base64.encodeToString(bos.toByteArray(),Base64.DEFAULT)
    }
    @TypeConverter
    fun String.toBitmap():Bitmap{
        return Base64.decode(this,Base64.DEFAULT).let {
            BitmapFactory.decodeByteArray(it,0,it.size)
        }
    }
}