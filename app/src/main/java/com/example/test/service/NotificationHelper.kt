package com.example.test.service

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.test.R

const val BOOKS_CHANNEL_ID = "books_channel"
const val NOTIFICATION_REQ_CODE = 40

fun createChannel(context: Context) {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
    if (notificationManager != null && notificationManager.getNotificationChannel(BOOKS_CHANNEL_ID) == null) {
        val channel = NotificationChannel(
            BOOKS_CHANNEL_ID,
            "Books notification",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = context.getString(R.string.channel_desc)

        notificationManager.createNotificationChannel(channel)
    }
}

fun requestNotifPermission(activity: Activity) {
    // Check if it is android 13
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val hasPermission = ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                NOTIFICATION_REQ_CODE
            )
        }
    }
}

fun showNotification(context: Context, title: String, content: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
        // Don't show notification if permission is not allowed
        if (!hasPermission) {
            return
        }
    }
    val builder = NotificationCompat.Builder(context, BOOKS_CHANNEL_ID)
        .setContentTitle(title)
        .setContentText(content)
        .setStyle(NotificationCompat.BigTextStyle().bigText(content)) // Allow expandable notification
        .setSmallIcon(R.mipmap.ic_launcher)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    val manager = NotificationManagerCompat.from(context)
    manager.notify(System.currentTimeMillis().toInt(), builder.build())
}