package uk1.ac.tees.mad.d3846810.fcm

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import uk1.ac.tees.mad.d3846810.R
import uk1.ac.tees.mad.d3846810.activities.MainActivity

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService : FirebaseMessagingService() {

    val TAG = String::class.java.simpleName

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

//         Not getting messages here? See why this may be: https://goo.gl/39bRNJ
//        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
//        remoteMessage.data.isNotEmpty().let {
//            Log.d(TAG, "Message data payload: " + remoteMessage.data)
//
//            // Compose and show notification
//            if (remoteMessage.data.isNotEmpty()) {
//                val title: String = remoteMessage.data["title"].toString()
//                val msg: String = remoteMessage.data["message"].toString()
//                sendNotification(title, msg)
//            }
//
//        }
//
//        // Check if message contains a notification payload.
//        remoteMessage.notification?.let {
//            sendNotification(remoteMessage.notification?.title, remoteMessage.notification?.body)
//        }

    }

    private fun sendNotification(title: String?, messageBody: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder =
            NotificationCompat.Builder(this, channelId).setSmallIcon(R.drawable.icon)
                .setContentTitle(title).setContentText(messageBody).setAutoCancel(true)
                .setSound(defaultSoundUri).setContentIntent(pendingIntent)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        // https://developer.android.com/training/notify-user/build-notification#Priority
        val channel = NotificationChannel(
            channelId, "Car24Admin", NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(0, notificationBuilder.build())
    }


}