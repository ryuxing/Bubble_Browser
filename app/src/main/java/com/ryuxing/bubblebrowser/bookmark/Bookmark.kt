package com.ryuxing.bubblebrowser.bookmark

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Bookmark(
    @PrimaryKey(autoGenerate = true) val id : Int,
    val title:String,
    val url: String,
    val favicon:Bitmap
)
