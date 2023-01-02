package com.example.mytodolist

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationCompat

class AlertReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        var notificationHelper : NotificationHelper = NotificationHelper(context)

        var bundle = intent?.getBundleExtra("bundle")

        var time = bundle?.getString("time")
        var content = bundle?.getString("content")

        var nb : NotificationCompat.Builder = notificationHelper.getChannelNotification(time, content)

        //알림 호출 코드
        notificationHelper.getManager().notify(1, nb.build())
    }
}