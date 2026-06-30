package com.example.loginpage

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: "Pengingat Posyandu"
        val message = intent.getStringExtra("message") ?: "Waktunya cek jadwal kesehatan!"
        val fragmentTag = intent.getStringExtra("fragment_tag") ?: "HOME"

        val notificationHelper = NotificationHelper(context)
        notificationHelper.sendNotification(title, message, fragmentTag)
    }
}