package com.ryuxing.bubblebrowser

import android.annotation.SuppressLint
import android.app.StatusBarManager
import android.content.ComponentName
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.ryuxing.bubblebrowser.bookmark.BookmarkViewModel
import com.ryuxing.bubblebrowser.bookmark.BookmarkViewModelFactory

class SettingsActivity : AppCompatActivity() {
    private val bookmarkViewModel : BookmarkViewModel by viewModels{
        BookmarkViewModelFactory((application as App).repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                finishAndRemoveTask()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    class SettingsFragment : PreferenceFragmentCompat() {

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            findPreference<Preference>("version")?.summary = BuildConfig.VERSION_NAME
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU){
                findPreference<Preference>("register_quick_setting")?.isVisible =false
            }
        }

        @SuppressLint("WrongConstant")
        override fun onPreferenceTreeClick(preference: Preference?): Boolean {
            Log.d(preference?.key,preference.toString())
            when(preference?.key){
                "reset_notification" ->{
                    BubbleNotification(requireContext()).notificationManager.cancelAll()
                    BubbleNotification(requireContext()).makeNewNotificationChannel()
                    ShortcutManagerCompat.removeAllDynamicShortcuts(requireContext())
                    Toast.makeText(requireContext(),R.string.toast_settings_reset_notificatios,Toast.LENGTH_SHORT).show()
                }
                "clear_bookmarks" ->{
                    AlertDialog.Builder(requireContext())
                        .setTitle(R.string.settings_title_reset)
                        .setMessage(R.string.dialog_delete_bookmarks_message)
                        .setPositiveButton(R.string.dialog_edit_delete_bookmark){ _, _ ->
                            (activity as SettingsActivity).bookmarkViewModel.deleteAll()
                            Toast.makeText(requireActivity().baseContext,R.string.toast_deletebookmarks_notificatios,Toast.LENGTH_SHORT).show()
                        }.setNegativeButton(R.string.dialog_edit_cancel) {_, _ ->
                        }.show()
                }
                "register_quick_setting"->{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                        var sbm = activity?.getSystemService(STATUS_BAR_SERVICE) as StatusBarManager
                        sbm.requestAddTileService(
                            ComponentName(
                                "com.ryuxing.bubblebrowser",
                                "com.ryuxing.bubblebrowser.MyTileService"
                            ),
                            "New Bubble 🌐",
                            Icon.createWithResource(context,R.drawable.ic_baseline_open_in_new_24),
                            {},
                            {}
                        )
                        Toast.makeText(context,R.string.toast_tile_added_or_fail,Toast.LENGTH_SHORT).show()

                    }else{
                        Toast.makeText(context,R.string.toast_version_to_add_tile,Toast.LENGTH_SHORT).show()
                    }
                }
            }
            return super.onPreferenceTreeClick(preference)
        }
    }
}