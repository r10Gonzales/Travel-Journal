package com.pmb.traveljournal.workers

import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.pmb.traveljournal.activities.AddTravelNoteActivity
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import com.pmb.traveljournal.R
import android.app.PendingIntent

class ReminderWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        sendNotification()
        return Result.success()
    }

    private fun sendNotification() {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "reminder_channel"
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Reminder Notifications", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Travel Journal Reminder")
            .setContentText("Don't forget to add your travel notes!")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(
                Intent(applicationContext, AddTravelNoteActivity::class.java).let { PendingIntent.getActivity(applicationContext, 0, it, PendingIntent.FLAG_UPDATE_CURRENT) }
            )
            .build()

        notificationManager.notify(1, notification)
    }
}
