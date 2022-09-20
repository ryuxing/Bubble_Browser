package com.ryuxing.bubblebrowser

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.graphics.drawable.IconCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ryuxing.bubblebrowser.bookmark.Bookmark
import com.ryuxing.bubblebrowser.bookmark.BookmarkDB
import java.util.*

class BookmarkAdapter(notification: BubbleNotification,dialog:EditDialog,val supportFragmentManager: FragmentManager) :RecyclerView.Adapter<BookmarkViewHolder>() {
    var bookmarks = listOf<Bookmark>()
    val notify = notification
    val dialog = dialog
    lateinit var onLongClickListener:View.OnLongClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bookmark_item,parent,false)
        val holder = BookmarkViewHolder(view)

        return holder
    }
    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        val bookmark = bookmarks[position]
        holder.favicon.setImageBitmap(bookmark.favicon)
        holder.title.text = bookmark.title
        holder.url.text = bookmark.url
        holder.wrapper.setOnClickListener {
            notify.sendNotification(bookmark.url,Math.random().toString(), favicon = IconCompat.createWithBitmap(bookmark.favicon), withExpand = true)
        }
        holder.wrapper.setOnLongClickListener{
            val bundle = Bundle()
            bundle.putInt("id",bookmark.id)
            dialog.arguments = bundle
            dialog.show(supportFragmentManager,"dialog")
            return@setOnLongClickListener true
        }
    }
    val longClickListener = View.OnLongClickListener { v ->

        return@OnLongClickListener true
    }
    fun getBookmarks(){
        bookmarks = BookmarkDB.getAll()
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int = bookmarks.size

    inner class  DiffBookmarkCallback(old:List<Bookmark>,new:List<Bookmark>):DiffUtil.Callback(){
        val old = old
        val new = new
        override fun getOldListSize(): Int = old.size
        override fun getNewListSize(): Int = new.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            (old[oldItemPosition].id == new[newItemPosition].id)
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            (old[oldItemPosition] == new[newItemPosition])
    }
}