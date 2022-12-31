package com.example.mytodolist

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

class NotificationHelper(context : Context?) : ContextWrapper(context) {
    private val channelID = "channelID"
    private val channelName = "channelName"

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        var channel = NotificationChannel(channelID, channelName,
        NotificationManager.IMPORTANCE_DEFAULT)
        //이 채널에 게시된 알림이 해당 기능을 지원하는 장치에서 알림 표시등을 표시할지 여부를 설정합니다.
        channel.enableLights(true)
        //이 채널에 게시된 알림이 해당 기능을 지원하는 장치에서 진동 등을 표시할지 여부를 설정합니다.
        channel.enableVibration(true)
        //이 채널에 게시된 알림에 대한 알림 표시등 색상을 설정
        channel.lightColor = Color.GREEN
        //이 채널에 게시된 알림이 전체 또는 수정된 형태로 잠금 화면에 표시되는지 여부를 설정
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

        getManager().createNotificationChannel(channel)
    }

    fun getManager() : NotificationManager {
        return getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    fun getChannelNotification() : NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, channelID)
            .setContentTitle("제목")
            .setContentText("내용")
            .setSmallIcon(R.drawable.ic_launcher_background)
    }
}