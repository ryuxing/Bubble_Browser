package com.ryuxing.bubblebrowser

import android.content.Intent
import android.service.quicksettings.TileService

class MyTileService :TileService(){
    override fun onClick() {
        super.onClick()
        val notification = BubbleNotification(this)
        notification.sendNotification(
            "https://google.com",
            Math.random().toString(),
            withExpand = true
        )
        startActivityAndCollapse(Intent(this,BrowserActivity::class.java).also { it.flags = Intent.FLAG_ACTIVITY_NEW_TASK })
    }

}