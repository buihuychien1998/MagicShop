package com.hidero.test.ui.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.mychatapp.notifications.Token
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.hidero.test.R
import com.hidero.test.ui.activities.MainActivity
import com.hidero.test.util.CHANNEL_ID
import com.hidero.test.util.CHANNEL_NAME
import com.squareup.picasso.Picasso
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(s: String) {
        super.onNewToken(s)
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser != null) {
            updateToken(s)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val sent = remoteMessage.data.get("sented")
        val user = remoteMessage.data.get("user")
        val preferences: SharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE)
        val currentUser = preferences.getString("currentuser", "none")
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser != null && sent == firebaseUser.uid) {
            if (currentUser != user) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    sendOreoNotification(remoteMessage)
                }
            }
        }

        if (remoteMessage.notification != null) {
            val photoUrl: String?
            if (remoteMessage.data != null) {
                photoUrl = remoteMessage.data["image"]
                if (!TextUtils.isEmpty(photoUrl)) {
                    Handler(Looper.getMainLooper()).post {
                        Picasso.get().load(photoUrl).into(object : com.squareup.picasso.Target {
                            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

                            }

                            override fun onBitmapFailed(
                                e: java.lang.Exception?,
                                errorDrawable: Drawable?
                            ) {
                            }

                            override fun onBitmapLoaded(
                                bitmap: Bitmap?,
                                from: Picasso.LoadedFrom?
                            ) {
                                sendNotification(
                                    remoteMessage.notification!!.title!!,
                                    remoteMessage.notification!!.body!!,
                                    bitmap
                                )
                            }

                        })
                    }
                } else {
                    sendNotification(
                        remoteMessage.notification!!.title!!, remoteMessage.notification!!.body!!,
                        null
                    )
                }

            } else {
                sendNotification(
                    remoteMessage.notification!!.title!!, remoteMessage.notification!!.body!!,
                    null
                )
            }

        }
    }

    private fun updateToken(refreshToken: String) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val reference = FirebaseDatabase.getInstance().getReference("Tokens")
        val token = Token(refreshToken)
        if (firebaseUser != null) {
            reference.child(firebaseUser.uid).setValue(token)
        }
    }

//    private fun sendOreoNotification(remoteMessage: RemoteMessage) {
//        val user = remoteMessage.data["user"]
////        val photoUrl = remoteMessage.data["image"]
//        val title = remoteMessage.data["title"]
//        val body = remoteMessage.data["body"]
//        val category = remoteMessage.category
////        val j = user?.replace("[\\D]".toRegex(), "")?.toInt()!!
//        val intent = Intent(this, MessageActivity::class.java)
//        val bundle = Bundle()
//        bundle.putString("userid", user)
//        intent.putExtras(bundle)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        val pendingIntent =
//            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
//        val defaultSound = RingtoneManager.getDefaultUri(
//            RingtoneManager.TYPE_RINGTONE
//        )
//        val oreoNotification = OreoNotification(this)
//        val photoUrl = category?.icon
//        val futureTarget = Glide.with(this)
//            .asBitmap()
//            .load(photoUrl)
//            .submit()
//        val icon = futureTarget.get()
//        val builder = oreoNotification.getOreoNotification(
//            title, body, pendingIntent,
//            defaultSound, icon
//        )
//        Glide.with(this).clear(futureTarget)
//        val i = 0
////        if (j > 0) {
////            i = j
////        }
//        oreoNotification.manager?.notify(i, builder.build())
//    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun sendNotification(title: String, messageBody: String, bitmap: Bitmap?) {
//        getString(R.string.project_id)
//        var largeIcon = getBitmapFromUrl(photoUrl)
        var largeIcon = bitmap
        if (largeIcon == null) {
            largeIcon = BitmapFactory.decodeResource(
                resources,
                R.drawable.ic_message
            )
        }
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        val notificationBuilder = NotificationCompat.Builder(
            this,
            CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_message)
            .setLargeIcon(
                largeIcon
            )
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setDefaults(Notification.DEFAULT_ALL)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .addAction(
                NotificationCompat.Action(
                    R.drawable.ic_message,
                    "Cancel",
                    PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
                )
            )
            .addAction(
                NotificationCompat.Action(
                    R.drawable.ic_message,
                    "OK",
                    PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
                )
            )
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Since android Oreo category channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }

    fun getBitmapFromUrl(imageUrl: String?): Bitmap? {
        return try {
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.getInputStream()
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) { // TODO Auto-generated catch block
            e.printStackTrace()
            null
        }
    }
}
