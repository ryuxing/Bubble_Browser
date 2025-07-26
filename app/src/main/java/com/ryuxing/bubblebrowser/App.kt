package com.ryuxing.bubblebrowser

import android.app.*
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.preference.PreferenceManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.core.graphics.drawable.IconCompat
import com.ryuxing.bubblebrowser.bookmark.BookmarkDB
import com.ryuxing.bubblebrowser.bookmark.BookmarkRepository

class App:Application() {
    companion object{
        lateinit var person:Person
        lateinit var context: Context
        lateinit var pref:SharedPreferences
        val googleUrl = "https://google.com"

    }
    val db by lazy {BookmarkDB.db()}
    val repository by lazy{BookmarkRepository(db.dao())}
    override fun onCreate() {
        Log.d("App_Start","Before Displayed")
        super.onCreate()
        context = applicationContext
        person = Person.Builder()
            .setName("BubbleBrowser")
            .build()
        pref = PreferenceManager.getDefaultSharedPreferences(context)
        Log.d("App_Finish","Before Displayed")
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notiEnable = notificationManager.areNotificationsEnabled()

        //更新時の通知
        if(pref.getString("version","1.1.1") != BuildConfig.VERSION_NAME && notiEnable){
            pref.edit().also { ed ->
                ed.putString("version", BuildConfig.VERSION_NAME)
                ed.commit()
            }
            //sendUpdateNotification()
        }
    }
    fun sendUpdateNotification(){
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(
            NotificationChannel(
                "Notification_Bubble_Update",
                context.getText(R.string.notification_channel_update),
                NotificationManager.IMPORTANCE_DEFAULT
            )
        )
        val builder = NotificationCompat.Builder(context,"Notification_Bubble_Update")
            .setCategory(Notification.CATEGORY_SOCIAL)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(context.getString(R.string.app_update_title))
            .setContentText(context.getString(R.string.app_update_text))
            .setContentIntent(PendingIntent.getActivity(
                context,
                0,
                Intent(context,SettingsActivity::class.java),
                PendingIntent.FLAG_IMMUTABLE
            ))
        notificationManager.notify(0,builder.build())
    }
}