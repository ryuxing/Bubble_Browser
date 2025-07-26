package com.ryuxing.bubblebrowser

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class InitialActivity : AppCompatActivity() {
    lateinit var notification:BubbleNotification
    var permissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { permission ->
        if(permission) {
            Toast.makeText(this@InitialActivity,R.string.toast_granted,Toast.LENGTH_SHORT).show()
        } else{
            startActivity(Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            })
            Toast.makeText(this, R.string.toast_turn_on_notification, Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)
        notification = BubbleNotification(this)
        findViewById<Button>(R.id.init_button_notification)?.setOnClickListener(notificationClickListener)
        findViewById<Button>(R.id.init_button_app_notification)?.setOnClickListener(appNotiOnClickListener)
        findViewById<Button>(R.id.init_start)?.setOnClickListener {
            finish()
            startActivity(Intent(this,MainActivity::class.java))

        }
    }
    val notificationClickListener = View.OnClickListener {
        if(notification.notificationManager.areNotificationsEnabled()){
            notification.makeNotificationChannel()
            Toast.makeText(this,R.string.toast_granted,Toast.LENGTH_SHORT).show()
            return@OnClickListener
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&!(shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS))) {
            permissionRequest.launch(Manifest.permission.POST_NOTIFICATIONS)
        }else{
            notification.makeNotificationChannel()
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            Toast.makeText(this, R.string.toast_turn_on_notification, Toast.LENGTH_LONG).show()
        }
    }
    val appNotiOnClickListener = View.OnClickListener {
        val intent = Intent().also {
            it.action = Settings.ACTION_APP_NOTIFICATION_BUBBLE_SETTINGS
            it.putExtra(Settings.EXTRA_APP_PACKAGE,packageName)
        }
        startActivity(intent)
        Toast.makeText(this,R.string.toast_allow_bubble,Toast.LENGTH_LONG).show()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isEmpty()) super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            1 -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, R.string.toast_granted, Toast.LENGTH_SHORT).show()
                } else {
                    startActivity(Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                        putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                    })
                    Toast.makeText(this, R.string.toast_turn_on_notification, Toast.LENGTH_LONG).show()

                }

            }
            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }
}