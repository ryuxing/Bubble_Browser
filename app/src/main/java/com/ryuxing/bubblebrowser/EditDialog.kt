package com.ryuxing.bubblebrowser

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.ryuxing.bubblebrowser.bookmark.Bookmark
import com.ryuxing.bubblebrowser.bookmark.BookmarkDB
import com.ryuxing.bubblebrowser.bookmark.BookmarkViewModel
import com.ryuxing.bubblebrowser.bookmark.BookmarkViewModelFactory

class EditDialog: DialogFragment() {
    lateinit var bookmark: Bookmark
    lateinit var root: View
    lateinit var mainActivity:MainActivity
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val id = arguments?.getInt("id")?: throw IllegalStateException("Bundle cannot null")
        bookmark = BookmarkDB.getOne(id)[0]
        return activity?.let {
            var builder = AlertDialog.Builder(it)
            var inflater = requireActivity().layoutInflater
            root = inflater.inflate(R.layout.dialog_edit_bookmark, null)
            builder.setView(root)
                .setPositiveButton(R.string.dialog_edit_save, onSaveClickListener)
                .setNegativeButton(R.string.dialog_edit_cancel,onCancelClickListener)
                .setNeutralButton(R.string.dialog_edit_delete_bookmark,onDeleteClickListener)
            root.findViewById<EditText>(R.id.dialog_edit_url).setText(bookmark.url)
            root.findViewById<EditText>(R.id.dialog_edit_name).setText(bookmark.title)
             builder.create()
        } ?: throw IllegalStateException("Activity cannot null")
    }
    val onSaveClickListener = DialogInterface.OnClickListener { dialog, which ->
        val urlView = root.findViewById<EditText>(R.id.dialog_edit_url)
        val titleView= root.findViewById<EditText>(R.id.dialog_edit_name)
        bookmark = Bookmark(
            bookmark.id,
            titleView.text.toString(),
            urlView.text.toString(),
            bookmark.favicon
        )
        dialog.dismiss()
        mainActivity.updateBookmark(bookmark)
        Toast.makeText(activity, R.string.dialog_edit_save, Toast.LENGTH_SHORT).show()
    }
    val onCancelClickListener = DialogInterface.OnClickListener { dialog, _ ->
        dialog.cancel()
    }
    val onDeleteClickListener = DialogInterface.OnClickListener { dialog, _ ->
        dialog.dismiss()
        AlertDialog.Builder(activity)
            .setTitle(R.string.dialog_edit_delete_bookmark)
            .setMessage(getString(R.string.dialog_confirm_delete_message,bookmark.title))
            .setNegativeButton(R.string.dialog_edit_cancel, DialogInterface.OnClickListener { dialog,_ -> dialog.cancel()})
            .setPositiveButton(R.string.dialog_edit_delete_bookmark, DialogInterface.OnClickListener { _, _ ->
                mainActivity.deleteBookmark(bookmark)
                Toast.makeText(mainActivity,R.string.toast_edit_delete,Toast.LENGTH_SHORT).show()
            }).create().show()
            
    }
}