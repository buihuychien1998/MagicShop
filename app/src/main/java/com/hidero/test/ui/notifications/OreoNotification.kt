package com.hidero.test.ui.notifications

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.hidero.test.R
import com.hidero.test.util.*

class OreoNotification(context: Context?) :
    ContextWrapper(context) {
    private var notificationManager: NotificationManager? = null
    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.enableLights(false)
        channel.enableVibration(true)
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        manager!!.createNotificationChannel(channel)
    }

    val manager: NotificationManager?
        get() {
            if (notificationManager == null) {
                notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }
            return notificationManager
        }

    @TargetApi(Build.VERSION_CODES.O)
    fun getOreoNotification(
        title: String?, body: String?,
        pendingIntent: PendingIntent?, soundUri: Uri?, icon: Bitmap
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(
            applicationContext,
            CHANNEL_ID
        )
            .setContentIntent(pendingIntent)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_message)
            .setLargeIcon(icon)
            .setSound(soundUri)
            .setAutoCancel(true)
    }



    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
    }
}