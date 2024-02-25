package com.sumit.myswipeproduct

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentActivity

object Utils {
    @RequiresApi(Build.VERSION_CODES.O)
    fun showNotification(
        context: Context,
        title: String,
        content: String,
        activity: FragmentActivity
    ) {
        createNotificationChannel(context)

        val intent = Intent(context, activity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )


        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_products_wine)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)


        val notificationManager = NotificationManagerCompat.from(context)
        if (!notificationManager.areNotificationsEnabled()) {
            val action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            val intent = Intent(action)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            startActivity(context, intent, null)
        } else {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(
                    context,
                    "Please grant notification permission",
                    Toast.LENGTH_SHORT
                )
                    .show()
                return
            }
            notificationManager.notify(NOTIFICATION_ID, builder.build())
        }


    }


    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "product Adding Notification Channel"
            val descriptionText = "It is required when you add a product"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    private const val CHANNEL_ID = "SwipeProductChannelId"
    private const val NOTIFICATION_ID = 123

}