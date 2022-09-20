package com.ryuxing.bubblebrowser

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class BookmarkViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
    val wrapper = itemView.findViewById<ConstraintLayout>(R.id.bookmark_item_view)
    val url = itemView.findViewById<TextView>(R.id.bookmark_item_url)
    val title = itemView.findViewById<TextView>(R.id.bookmark_item_title)
    val favicon = itemView.findViewById<ImageView>(R.id.bookmark_item_favicon)
}