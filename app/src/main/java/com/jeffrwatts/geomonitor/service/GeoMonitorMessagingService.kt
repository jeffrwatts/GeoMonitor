package com.jeffrwatts.geomonitor.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.jeffrwatts.geomonitor.MainActivity
import com.jeffrwatts.geomonitor.R

class GeoMonitorMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("GeoMonitorMessagingService", "onMessageReceived: ${remoteMessage.messageId}")

        // Assuming your notification's data payload is in the "message" key
        remoteMessage.notification?.let {
            showNotification(it.title ?: "Earthquake Alert!", it.body ?: "Check the app for more details.")
        }
    }

    private fun showNotification(title: String, body: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Notification channel ID
        val channelId = "earthquake_alerts_channel"

        // Create the notification channel - can be done multiple times, and no API to check whether it already exists.
        val channel = NotificationChannel(channelId, "Earthquake Alerts", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)

        // Intent that starts the MainActivity
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        // Build the notification
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.earth)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        // Notify
        notificationManager.notify(999, notification) // 999 is just an example ID
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("GeoMonitorMessagingService", "onNewToken: $token")
    }
}
