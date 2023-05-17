package com.ryuxing.bubblebrowser

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import android.util.Log
import androidx.core.app.Person
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

    }
}