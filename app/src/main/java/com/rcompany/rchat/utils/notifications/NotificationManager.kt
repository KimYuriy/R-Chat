package com.rcompany.rchat.utils.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.rcompany.rchat.R
import com.rcompany.rchat.utils.databases.chats.dataclasses.messages.incoming.MessageDataClass
import com.rcompany.rchat.utils.databases.chats.enums.ChatTypes
import com.rcompany.rchat.windows.messages.MessagesWindow

class NotificationManager(private val context: Context) {
    private var notificationManager: NotificationManager? = null
    private var notificationChannel: NotificationChannel? = null
    private val channelID = "NEW_MESSAGES"
    private val description = "Message Notifications"

    /**
     * Конструктор - создает канал для уведомлений сообщений.
     */
    init {
        notificationChannel = NotificationChannel(channelID, description, NotificationManager.IMPORTANCE_HIGH)
        notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager?.createNotificationChannel(notificationChannel!!)
    }

    fun showMessageNotification(data: MessageDataClass) {
        val intent = Intent(context, MessagesWindow::class.java).apply {
            putExtra("chat_id", data.chat.id)
            putExtra("chat_name", data.chat.name)
            putExtra("chat_type", data.chat.type)
            putExtra("chat_avatar", data.chat.avatar_photo_url)
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val shownTitle = if (data.chat.type == ChatTypes.PRIVATE.value) data.chat.name else "${data.chat.name}: ${data.sender.name}"
        val collapsedNotification = RemoteViews(context.packageName, R.layout.notification_item).apply {
            setTextViewText(R.id.tvChatName, shownTitle)
            setTextViewText(R.id.tvMessage, data.message_text)
        }
        val expandedNotification = RemoteViews(context.packageName, R.layout.notification_expanded_item).apply {
            setTextViewText(R.id.tvChatName, shownTitle)
            setTextViewText(R.id.tvMessage, data.message_text)
        }
        val builder = NotificationCompat.Builder(context, channelID).apply {
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setCustomContentView(collapsedNotification)
            setCustomBigContentView(expandedNotification)
            setContentIntent(pendingIntent)
            setAutoCancel(true)
        }
        notificationManager?.notify(data.chat.id.hashCode(), builder.build())
    }
}