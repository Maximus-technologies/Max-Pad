package com.max.firebase.messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.view.View
import android.webkit.URLUtil
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.max.firebase.R
import java.io.IOException
import java.io.InputStream
import java.lang.IllegalArgumentException
import java.net.MalformedURLException
import java.net.URL

class MyFirebaseMsgService : FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage) {
        if (p0.data.isNotEmpty()) {
            sendNotification(
                p0.data["icon"],
                p0.data["title"], p0.data["short_desc"].toString(),
                p0.data["long_desc"], p0.data["app_url"].toString(), p0.data["feature"]
            )
        } else if (p0.notification != null) {
            sendNotification(
                p0.notification?.imageUrl?.toString(),
                p0.notification?.title, p0.notification?.body.toString(),
                "", "", p0.notification?.imageUrl?.toString()
            )
        }
    }

    private fun Context.isPackageInstalled(packageName: String): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        } catch (e: Exception) {
            false
        }

    }

    private fun sendNotification(
        u: String?,
        t: String?,
        sd: String,
        ld: String?,
        url: String,
        feature: String?
    ) {
        var temp = url
        if (temp != "" && temp.contains("https://play.google.com/store/apps/details?id="))
            temp = temp.replace("https://play.google.com/store/apps/details?id=", "")
        val appInstalled = isPackageInstalled(temp)
        var pendingIntent: PendingIntent? = null
        if (!appInstalled) {
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            pendingIntent = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        }
//        else{
//            val launchIntent = packageManager.getLaunchIntentForPackage(temp)
//            pendingIntent = PendingIntent.getActivity(
//                this, 0, launchIntent,
//                PendingIntent.FLAG_IMMUTABLE
//            )
//        }
//        val uri = Uri.parse(url)
//        val intent = Intent(Intent.ACTION_VIEW, uri)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            PendingIntent.getActivity(
//                this, 0, intent,
//                PendingIntent.FLAG_IMMUTABLE)
//        } else {
//            PendingIntent.getActivity(
//                this, 0, intent,
//                PendingIntent.FLAG_ONE_SHOT)
//        }
        val channelId = com.max.firebase.BuildConfig.application_id
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val remoteViews = RemoteViews(packageName, R.layout.firebase_notification)
        val icon = getBitmapFromURL(u)
        val imgUrl = getBitmapFromURL(feature)
        remoteViews.setTextViewText(R.id.txtVTitle, t)
        remoteViews.setTextViewText(R.id.txtVShortDesc, sd)
        remoteViews.setTextViewText(R.id.txtVLDesc, ld)
        if (icon != null)
            remoteViews.setImageViewBitmap(R.id.imVIcon, icon)
        if (imgUrl != null)
            remoteViews.setImageViewBitmap(R.id.imgVFeature, imgUrl)
        if (!ld.isNullOrEmpty()) {
            remoteViews.setViewVisibility(R.id.txtVLDesc, View.VISIBLE)
        } else {
            remoteViews.setViewVisibility(R.id.txtVLDesc, View.GONE)
        }
        if (!feature.isNullOrEmpty()) {
            remoteViews.setViewVisibility(R.id.imgVFeature, View.VISIBLE)
        } else {
            remoteViews.setViewVisibility(R.id.imgVFeature, View.GONE)
        }
        if (!appInstalled) {
            val notification = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setCustomContentView(remoteViews)
                .setCustomBigContentView(remoteViews)
                .setSound(defaultSoundUri)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()


            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    "TRANSLATOR NOTIFICATION CHANNEL",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationManager.createNotificationChannel(channel)
            }
            notificationManager.notify(0, notification)
        }
    }

    private fun getBitmapFromURL(strURL: String?): Bitmap? {
        if (URLUtil.isValidUrl(strURL)) {
            val inputStream: InputStream
            try {
                inputStream = URL(strURL).openStream()
                return BitmapFactory.decodeStream(inputStream)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return null
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }
}
