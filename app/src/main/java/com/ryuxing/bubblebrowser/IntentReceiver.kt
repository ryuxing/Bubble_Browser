package com.ryuxing.bubblebrowser

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import java.lang.Exception

class IntentReceiver : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.dialog_edit_bookmark)
        val url:String? = intent.extras?.get("android.intent.extra.TEXT").toString()
        try {
            for(str in intent.extras?.keySet()!!){
                Log.d(str,intent.extras?.get(str).toString())
            }
            val uri = Uri.parse(url)
            if(url==null)throw Exception()
            if(!url.startsWith("http")) throw Exception()
            val notification = BubbleNotification(this)
            notification.makeNotificationChannel()
            val id = Math.random().toString()
            notification.sendNotification(uri.toString(),id)
        }catch (e:Exception){
            Log.w("Receive Intent",e.stackTraceToString())
            Toast.makeText(this,getString(R.string.toast_from_share_intent_invalid_url)+"\n${url}",Toast.LENGTH_SHORT).show()
        }
        finishAndRemoveTask()
    }
}