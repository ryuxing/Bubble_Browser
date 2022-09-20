package com.ryuxing.bubblebrowser

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import java.util.*

class BubbleNotification(parentContext :Context){
    val context: Context
    val notificationManager:NotificationManager
    init {
        context = parentContext
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
    fun makeNotificationChannel(){
        notificationManager.createNotificationChannel(
            NotificationChannel("Notification_Bubble",context.getText(R.string.notification_channel_bubble),
                NotificationManager.IMPORTANCE_DEFAULT).also{it.setAllowBubbles(true)})

    }
    fun sendNotification(url:String,shortcut:String,isUpdate:Boolean=false,favicon: IconCompat? =null,withExpand:Boolean=false,title:String ="Ongoing Browser"){
        Log.v("url", url)
        val pendingIntent = PendingIntent
            .getActivity(
                context,
                shortcut.hashCode(),
                Intent(context,BrowserActivity::class.java).also{
                    it.putExtra("shortcutId",shortcut)
                    it.putExtra("url", url)
                },
                PendingIntent.FLAG_MUTABLE
            )
        val icon:IconCompat =
            favicon ?: IconCompat.createWithResource(context,R.drawable.ic_baseline_language_24)
        val bubble = NotificationCompat.BubbleMetadata.Builder(pendingIntent, icon)
            .setSuppressNotification(isUpdate)
            .setAutoExpandBubble(withExpand)
            .setDesiredHeight(1000)

        //Update Shortcuts
        pushShortcut(shortcut,icon,title)

        val builder = NotificationCompat.Builder(context,"Notification_Bubble")
            .setCategory(Notification.CATEGORY_MESSAGE)
            .setStyle(NotificationCompat.MessagingStyle(App.person).also{ messaging ->
                messaging.addMessage("New Browser", Date().time,App.person)
            })
            .setAutoCancel(true)
            .setShortcutId(shortcut)
            .setContentIntent(pendingIntent)
            .setBubbleMetadata(bubble.build())
            .setOnlyAlertOnce(false)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
        notificationManager.notify(shortcut.hashCode(), builder.build())
        Log.d("notify","send Notification")
    }

    fun pushShortcut(shortcutId:String, icon: IconCompat,title: String) :String{
        val shortcut = ShortcutInfoCompat.Builder(context,shortcutId)
            .setActivity(ComponentName(context,MainActivity::class.java))
            .setShortLabel(title)
            .setIcon(icon)
            .setLongLived(true)
            .setPerson(App.person)
            .setIntent(Intent(context,BrowserActivity::class.java).setAction(Intent.ACTION_VIEW))
            .build()
        ShortcutManagerCompat.pushDynamicShortcut(context,shortcut)
        return shortcut.id
    }

    fun cancelNotification(shortcutId: String) {
        notificationManager.cancel(shortcutId.hashCode())
    }


}