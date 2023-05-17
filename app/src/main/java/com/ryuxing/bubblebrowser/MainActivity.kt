package com.ryuxing.bubblebrowser

import android.app.NotificationManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.ryuxing.bubblebrowser.bookmark.Bookmark
import com.ryuxing.bubblebrowser.bookmark.BookmarkViewModel
import com.ryuxing.bubblebrowser.bookmark.BookmarkViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var notification:BubbleNotification
    private lateinit var adapter: BookmarkAdapter
    private val bookmarkViewModel :BookmarkViewModel by viewModels{
        BookmarkViewModelFactory((application as App).repository)
    }
    val editDialog = EditDialog()
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("onCreate_Start","before Displayed")
        super.onCreate(savedInstanceState)
        Log.d("onCreate_Start","before Displayed  Super Called")
        notification = BubbleNotification(this)
        val notiEnable = notification.notificationManager.areNotificationsEnabled()
        val bubbleEnabled = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            notification.notificationManager.bubblePreference==NotificationManager.BUBBLE_PREFERENCE_ALL
        } else {
            notification.notificationManager.areBubblesAllowed()
        }
        if(!(notiEnable && bubbleEnabled)) {
            startActivity(Intent(this,InitialActivity::class.java))
            finish()
            return
        }
        notification.makeNotificationChannel()
        setContentView(R.layout.activity_main)
        var url = App.pref.getString("defaultUri",App.googleUrl)
        if(url.isNullOrBlank())url = App.googleUrl
        findViewById<EditText>(R.id.main_url_edit_text).setText(url)

        findViewById<Button>(R.id.main_go_button).setOnClickListener(clickListener)
        val rv = findViewById<RecyclerView>(R.id.bookmark_recycler)
        editDialog.mainActivity =this
        adapter = BookmarkAdapter(notification,editDialog,supportFragmentManager)
        val layout = LinearLayoutManager(this)
        rv.setHasFixedSize(true)
        rv.layoutManager = layout
        rv.adapter = adapter
        bookmarkViewModel.bookmarks.observe(this){ bookmarks ->
            Log.d("OBSERVER", "Working")

            val diff = DiffUtil.calculateDiff(
                adapter.DiffBookmarkCallback(
                    adapter.bookmarks,
                    bookmarks
                )
            )
            adapter.bookmarks = bookmarks
            diff.dispatchUpdatesTo(adapter)
            Log.d("OBSERVER", bookmarks.toString())
        }
    }

    override fun onResume() {
        super.onResume()
        //adapter.getBookmarks()


        MobileAds.initialize(this){}
        val adView = findViewById<AdView>(R.id.adView)
        adView.loadAd(AdRequest.Builder().build())
    }

    fun deleteBookmark(bookmark: Bookmark) = bookmarkViewModel.delete(bookmark)
    fun updateBookmark(bookmark: Bookmark) = bookmarkViewModel.update(bookmark)

    val clickListener = View.OnClickListener{
        val id = Math.random().toString()
        var url = findViewById<EditText>(R.id.main_url_edit_text).text?.toString() ?:""

        if(url.isNullOrBlank())Toast.makeText(this,R.string.toast_empty_url,Toast.LENGTH_SHORT).show()
        else notification.sendNotification(url,id,false, withExpand = true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_activity,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return  when(item.itemId){
            R.id.menu_settings ->{
                startActivity(Intent(this,SettingsActivity::class.java))
                return true
            }
            else ->super.onOptionsItemSelected(item)
        }
    }
}