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
        if(intent.action == "android.intent.action.VIEW"){setContentView(R.layout.activity_default_browser)}

        val url:String? =
            if (intent.action == "android.intent.action.VIEW"){intent.data.toString()}
            else{intent.extras?.get("android.intent.extra.TEXT").toString()}
        Log.d("SET_URL", url+"")
        try {
            //KeySet　一覧表示
//            for(str in intent.extras?.keySet()!!){
//                Log.d(str,intent.extras?.get(str).toString())
//            }
            val uri = Uri.parse(url)
            if(url==null)throw Exception()
            if(!url.startsWith("http")) throw Exception()
            val notification = BubbleNotification(this)
            notification.makeNotificationChannel()
            val id = Math.random().toString()
            notification.sendNotification(uri.toString(),id, withExpand = true)
        }catch (e:Exception){
            Log.w("Receive Intent",e.stackTraceToString())
            Toast.makeText(this,getString(R.string.toast_from_share_intent_invalid_url)+"\n${url}",Toast.LENGTH_SHORT).show()
        }
        finishAndRemoveTask()
    }
}