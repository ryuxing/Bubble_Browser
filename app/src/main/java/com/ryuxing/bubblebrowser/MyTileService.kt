package com.ryuxing.bubblebrowser

import android.app.Dialog
import android.content.Intent
import android.service.quicksettings.TileService

class MyTileService :TileService(){
    override fun onClick() {
        super.onClick()
        var url =App.pref.getString("defaultUri",App.googleUrl)
        if(url.isNullOrBlank())url = App.googleUrl
        val notification = BubbleNotification(this)
        notification.sendNotification(
            url,
            Math.random().toString(),
            withExpand = true
        )
        showDialog(Dialog(this).also {
            it.setOnShowListener {
                it.cancel()
            }
        })
    }

}