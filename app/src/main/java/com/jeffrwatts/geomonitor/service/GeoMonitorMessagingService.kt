package com.jeffrwatts.geomonitor.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class GeoMonitorMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        // Handle FCM messages here.
        // Example: Log the message or update UI.
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Send the new token to your server if necessary.
    }
}
